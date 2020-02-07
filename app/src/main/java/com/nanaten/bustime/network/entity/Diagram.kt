/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Diagram(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val isLast: Boolean,
    val isReturn: Boolean,
    val arrivalHour: Int,
    val arrivalMinute: Int,
    val arrivalSecond: Int
) : Parcelable {
    constructor(entity: DiagramEntity) : this(
        entity.hour,
        entity.minute,
        entity.second,
        entity.isLast,
        entity.isReturn,
        entity.arrivalHour,
        entity.arrivalMinute,
        entity.arrivalSecond
    )
}