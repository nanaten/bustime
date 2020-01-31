/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nanaten.bustime.network.usecase.DiagramUseCase
import javax.inject.Inject

class DiagramViewModel @Inject constructor(private val useCase: DiagramUseCase) : ViewModel() {

}