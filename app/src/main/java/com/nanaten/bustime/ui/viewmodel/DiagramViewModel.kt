/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaten.bustime.SharedPref
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.NetworkResult
import com.nanaten.bustime.network.entity.RemotePdf
import com.nanaten.bustime.network.usecase.DiagramUseCase
import com.nanaten.bustime.service.AlarmReceiver
import com.nanaten.bustime.util.LiveEvent
import com.nanaten.bustime.util.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DiagramViewModel @Inject constructor(private val useCase: DiagramUseCase) : ViewModel() {
    val calendar = MutableLiveData<Calendar>()
    val diagrams = MutableLiveData<List<Diagram>>()
    val toCollegeDiagrams = MutableLiveData<List<Diagram>>()
    val toStationDiagrams = MutableLiveData<List<Diagram>>()
    val nowSecond = MutableLiveData<Long>(0L)
    private val lastUpdated = MutableLiveData<String>()
    val startTime = MutableLiveData<String>("")
    val arrivalTime = MutableLiveData<String>("")
    val isLoading = MutableLiveData<Boolean>(false)
    val nextDiagram = MutableLiveData<Diagram>()
    val pdfUrl = MutableLiveData<RemotePdf>()
    val networkResult = LiveEvent<NetworkResult>()
    private val appIsActive = ObservableField<Boolean>(false)
    val position = MutableLiveData<Int>()

    /**
     * 次のバスまでの時間を2つのLiveDataから割り出す
     * nowSecond: 現在の時間(sec)
     * diagrams: 本日の時刻表
     */
    val next: LiveData<Long> =
        combine(0L, nowSecond, diagrams) { _, sec, diagrams ->
            val nearDiagram = diagrams.firstOrNull { it.second >= sec } ?: Diagram.createEmptyData()
            nextDiagram.postValue(nearDiagram)
            startTime.postValue(String.format("%02d:%02d", nearDiagram.hour, nearDiagram.minute))
            arrivalTime.postValue(
                String.format(
                    "%02d:%02d",
                    nearDiagram.arrivalHour,
                    nearDiagram.arrivalMinute
                )
            )

            val nearSecond = nearDiagram.second
            if (nearSecond != 0) nearSecond - sec else 0
        }

    fun getCalendar() {
        viewModelScope.launch {
            try {
                val cal = useCase.getTodayCalendar()
                cal.collect {
                    calendar.postValue(it)
                    networkResult.call(NetworkResult.Success)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                networkResult.call(NetworkResult.Error)
            }
        }
    }

    fun getDiagrams(cache: Boolean = true) {
        isLoading.postValue(true)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (lastUpdated.value == today) {
            diagrams.postValue(diagrams.value)
            isLoading.postValue(false)
            return
        }
        viewModelScope.launch {
            try {
                val diagram = calendar.value?.diagram
                val isSuspend = calendar.value?.isSuspend ?: false
                // 運休かどうか判定
                if (diagram == null || isSuspend) {
                    isLoading.postValue(false)
                    return@launch
                }
                val list = useCase.getDiagrams(diagram, cache)
                list.collect {
                    toCollegeDiagrams.postValue(it.first)
                    toStationDiagrams.postValue(it.second)
                }
                lastUpdated.postValue(today)
                networkResult.call(NetworkResult.Success)
            } catch (e: Exception) {
                networkResult.call(NetworkResult.Error)
            }
            isLoading.postValue(false)
        }
    }

    fun startTimer() {
        if (appIsActive.get() == true) return
        appIsActive.set(true)
        viewModelScope.launch {
            while (appIsActive.get() == true) {
                val cal = java.util.Calendar.getInstance()
                val now = (
                        cal.get(java.util.Calendar.HOUR_OF_DAY) * 3600
                                + cal.get(java.util.Calendar.MINUTE) * 60
                                + cal.get(java.util.Calendar.SECOND)
                        ).toLong()
                nowSecond.postValue(now)
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        if (appIsActive.get() != true) return
        appIsActive.set(false)
    }

    fun getPdf() {
        pdfUrl.value ?: viewModelScope.launch {
            try {
                useCase.getPdfUrl()
                    .collect {
                        networkResult.call(NetworkResult.Success)
                        pdfUrl.postValue(it)
                    }
            } catch (e: Exception) {
                networkResult.call(NetworkResult.Error)
                e.printStackTrace()
            }
        }
    }

    fun switchPosition(position: Int) {
        this.position.postValue(position)
        when (position) {
            HomeTabs.TO_COLLAGE.value -> {
                diagrams.postValue(toCollegeDiagrams.value)
            }
            else -> {
                diagrams.postValue(toStationDiagrams.value)
            }
        }
    }

    fun getOldDate(): String? = lastUpdated.value
    fun setOldDate(old: String?) {
        lastUpdated.postValue(old)
    }


    fun showRemindDialog(context: Context, diagram: Diagram) {
        val dialog = AlertDialog.Builder(context).create()
        dialog.setTitle("リマインダー設定")
        dialog.setMessage(
            String.format(
                "%02d:%02dのバスにリマインダーを設定しますか？\n" +
                        "(5分前に通知が来ます)", diagram.hour, diagram.minute
            )
        )
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            setAlarm(context, diagram)
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "キャンセル") { _, _ -> dialog.dismiss() }
        dialog.show()
    }

    private fun setAlarm(context: Context, diagram: Diagram) {
        setAlarmStatus(diagram)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = getTodayZeroTimeCalendar()
        calendar.add(java.util.Calendar.SECOND, (diagram.second - 300)) // 到着時間の5分前にリマインダーをセット
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.putExtra("Time", String.format("%02d:%02d", diagram.hour, diagram.minute))
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext, 0, intent, 0)
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(calendar.timeInMillis, null),
            pendingIntent
        )
        SharedPref(context).setAlarmSec(position.value ?: 0, diagram.second)
        setAlarmStatus(diagram)
    }

    private fun getTodayZeroTimeCalendar(): java.util.Calendar {
        val cal = java.util.Calendar.getInstance()
        cal.set(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH),
            cal.get(java.util.Calendar.DAY_OF_MONTH),
            0,
            0,
            0
        )
        return cal
    }

    // アラームの状態をViewに反映する
    // TODO: 現状日付を跨いだ時の処理が出来ていない。Roomでキャッシュを取るようにする
    private fun setAlarmStatus(diagram: Diagram) {
        when (position.value) {
            HomeTabs.TO_STATION.value -> {
                toStationDiagrams.value?.find { it.second == diagram.second }?.setAlarm = true
                toStationDiagrams.value?.filter { it.second != diagram.second }
                    ?.map { it.setAlarm = false }
                toCollegeDiagrams.value?.map { it.setAlarm = false }
                diagrams.postValue(
                    toStationDiagrams.value
                )
            }
            else -> {
                toCollegeDiagrams.value?.find { it.second == diagram.second }?.setAlarm = true
                toCollegeDiagrams.value?.filter { it.second != diagram.second }
                    ?.map { it.setAlarm = false }
                toStationDiagrams.value?.map { it.setAlarm = false }
                diagrams.postValue(toCollegeDiagrams.value)
            }
        }
    }

    fun initAlarmStatus(context: Context) {
        val second = SharedPref(context).getAlarmSec()
        when (SharedPref(context).getAlarmPos()) {
            HomeTabs.TO_STATION.value -> {
                toStationDiagrams.value?.find { it.second == second }?.setAlarm = true
            }
            else -> {
                toCollegeDiagrams.value?.find { it.second == second }?.setAlarm = true
            }
        }
    }

    fun getOldDate(): String? = oldDate.value
    fun setOldDate(old: String?) {
        lastUpdated.postValue(old)
    }

    fun getAppIsActive(): Boolean? = appIsActive.get()
}
