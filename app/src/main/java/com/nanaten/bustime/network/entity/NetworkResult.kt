/*
 * Created by m.coder on 2020/2/12.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

sealed class NetworkResult {
    object Success : NetworkResult()
    object Error : NetworkResult()
}