/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity(
    @PrimaryKey
    var id: Int = 0,
    var type: Int = 0,
    val second: Int,
    @ColumnInfo(name = "set_alarm")
    var setAlarm: Boolean
) {
    companion object {
        fun setFromDiagram(diagram: Diagram): AlarmEntity {
            return AlarmEntity(diagram.id, diagram.type, diagram.second, diagram.setAlarm)
        }
    }
}