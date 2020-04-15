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
    var isKaizu: Boolean = false,
    val arrivalHour: Int,
    val arrivalMinute: Int,
    val arrivalSecond: Int
) : Parcelable {

    companion object DiagramFactory : Translator<DiagramEntity, Diagram> {
        override fun convertFrom(entity: DiagramEntity): Diagram {
            return Diagram(
                entity.hour,
                entity.minute,
                entity.second,
                entity.isLast,
                entity.isReturn,
                entity.isKaizu,
                entity.arrivalHour,
                entity.arrivalMinute,
                entity.arrivalSecond
            )
        }

        fun createEmptyData(): Diagram {
            return convertFrom(DiagramEntity())
        }
    }
}