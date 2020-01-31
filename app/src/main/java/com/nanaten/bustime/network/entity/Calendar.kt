/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Calendar(
    val date: String,
    val diagram: String,
    val diagramName: String
) : Parcelable {
    constructor(entity: CalendarEntity) : this(
        entity.date,
        entity.diagram,
        entity.diagramName
    )
}