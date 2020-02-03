/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

class DiagramListEntity(
    var list: List<DiagramEntity>? = null
)

class DiagramEntity(
    var hour: Int = 0,
    var minute: Int = 0,
    var second: Int = 0,
    var isLast: Boolean = false,
    var isReturn: Boolean = false
)