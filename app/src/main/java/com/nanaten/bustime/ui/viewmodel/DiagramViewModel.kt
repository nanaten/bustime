/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.usecase.DiagramUseCase
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DiagramViewModel @Inject constructor(private val useCase: DiagramUseCase) : ViewModel() {
    val calendar: LiveData<Calendar> = liveData {
        val cal = useCase.getTodayCalendar()
        cal.collect {
            emit(it)
        }
    }
}