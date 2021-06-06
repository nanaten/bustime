/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Calendar(
    val date: String,
    val diagram: String,
    val diagramName: String,
    val isSuspend: Boolean
) : Parcelable {

    companion object CalendarFactory : Translator<CalendarEntity, Calendar> {
        override fun convertFrom(entity: CalendarEntity): Calendar {
            return Calendar(
                entity.date,
                entity.diagram,
                entity.diagramName,
                entity.isSuspend
            )
        }
    }
}