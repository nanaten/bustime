/*
 * Created by m.coder on 2020/2/12.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

data class RemotePdf(
    val calendar: String,
    val timeTable: String
) {
    constructor(entity: RemotePdfEntity) : this(
        entity.calendar,
        entity.timeTable
    )
}