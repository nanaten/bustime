/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.usecase.DiagramUseCase
import com.nanaten.bustime.util.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiagramViewModel @Inject constructor(private val useCase: DiagramUseCase) : ViewModel() {
    val calendar: LiveData<Calendar> = liveData {
        try {
            val cal = useCase.getTodayCalendar()
            cal.collect {
                emit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    val diagrams = MutableLiveData<List<Diagram>>()
    val todayZerotime = MutableLiveData<Long>(0L)
    val nowSecond = MutableLiveData<Long>(0L)
    val next: LiveData<Long> =
        combine(0L, todayZerotime, nowSecond, diagrams) { _, zeroTime, sec, diagrams ->
            val nearSecond = diagrams.firstOrNull { sec > it.second }?.second ?: 0
            if (nearSecond != 0) nearSecond - (sec - zeroTime) else 0
        }
    val appIsActive = ObservableField<Boolean>(false)

    fun getDiagrams(target: String) {
        viewModelScope.launch {
            try {
                val diagram = calendar.value?.diagram ?: return@launch
                val diagramName = "$diagram$target"
                val now = nowSecond.value?.minus(todayZerotime.value ?: 0L) ?: 0L
                val list = useCase.getDiagrams(diagramName, now)
                list.collect {
                    diagrams.postValue(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startTimer() {
        getZeroTime()
        if (appIsActive.get() == true) return
        appIsActive.set(true)
        viewModelScope.launch {
            while (appIsActive.get() == true) {
                val cal = java.util.Calendar.getInstance()
                val now = (cal.timeInMillis / 1000)
                nowSecond.postValue(now)
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        if (appIsActive.get() != true) return
        appIsActive.set(false)
    }

    fun getZeroTime() {
        val cal = java.util.Calendar.getInstance()
        cal.set(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH),
            cal.get(java.util.Calendar.DATE),
            0,
            0,
            0
        )
        val sec = (cal.timeInMillis / 1000)
        todayZerotime.postValue(sec)
    }
}