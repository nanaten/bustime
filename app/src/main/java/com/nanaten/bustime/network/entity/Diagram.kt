/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Diagram(
    val id: Int,
    val type: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val isLast: Boolean,
    val isReturn: Boolean,
    val isKaizu: Boolean,
    val arrivalHour: Int,
    val arrivalMinute: Int,
    val arrivalSecond: Int,
    var setAlarm: Boolean = false
) : Parcelable {

    companion object DiagramFactory : Translator<DiagramEntity, Diagram> {
        override fun convertFrom(entity: DiagramEntity): Diagram {
            return Diagram(
                entity.id,
                entity.type,
                entity.hour,
                entity.minute,
                entity.second,
                entity.isLast,
                entity.isReturn,
                entity.isKaizu,
                entity.arrivalHour,
                entity.arrivalMinute,
                entity.arrivalSecond,
                entity.setAlarm
            )
        }

        fun convertToEntity(diagram: Diagram): DiagramEntity {
            return DiagramEntity(
                diagram.id,
                diagram.type,
                diagram.hour,
                diagram.minute,
                diagram.second,
                diagram.isLast,
                diagram.isReturn,
                diagram.isKaizu,
                diagram.arrivalHour,
                diagram.arrivalMinute,
                diagram.arrivalSecond,
                diagram.setAlarm
            )
        }

        fun createEmptyData(): Diagram {
            return convertFrom(DiagramEntity())
        }
    }
}