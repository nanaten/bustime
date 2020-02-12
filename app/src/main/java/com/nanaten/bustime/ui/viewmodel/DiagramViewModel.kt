/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.DiagramEntity
import com.nanaten.bustime.network.entity.RemotePdf
import com.nanaten.bustime.network.usecase.DiagramUseCase
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
    val nowSecond = MutableLiveData<Long>(0L)
    private val oldDate = MutableLiveData<String>()
    val startTime = MutableLiveData<String>("")
    val arrivalTime = MutableLiveData<String>("")
    val isLoading = MutableLiveData<Boolean>(false)
    val pdfUrl = MutableLiveData<RemotePdf>()

    /**
     * 次のバスまでの時間を2つのLiveDataから割り出す
     * nowSecond: 現在の時間(sec)
     * diagrams: 本日の時刻表
     */
    val next: LiveData<Long> =
        combine(0L, nowSecond, diagrams) { _, sec, diagrams ->
            val nearDiagram = diagrams.firstOrNull { it.second >= sec } ?: Diagram(DiagramEntity())
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
    val appIsActive = ObservableField<Boolean>(false)

    fun getCalendar() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // 既にカレンダー取得済みならreturnする
        if (calendar.value != null && calendar.value?.date == today) {
            calendar.postValue(calendar.value)
            return
        }
        viewModelScope.launch {
            val cal = useCase.getTodayCalendar()
            cal.collect {
                calendar.postValue(it)
            }
        }
    }

    fun getDiagrams(target: String) {
        isLoading.postValue(true)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (oldDate.value == today) {
            diagrams.postValue(diagrams.value)
            isLoading.postValue(false)
            return
        }
        viewModelScope.launch {
            try {
                val diagram = calendar.value?.diagram ?: return@launch
                val diagramName = "$diagram$target"
                val now = nowSecond.value ?: 0L
                val list = useCase.getDiagrams(diagramName, now)
                list.collect {
                    diagrams.postValue(it)
                }
                oldDate.postValue(today)
            } catch (e: Exception) {
                e.printStackTrace()
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
        pdfUrl.value ?: try {
            viewModelScope.launch {
                useCase.getPdfUrl()
                    .collect {
                        pdfUrl.postValue(it)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}