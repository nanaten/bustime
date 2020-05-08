/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DiagramEntity(
    @PrimaryKey
    var id: Int = 1,
    // type 0 = toStation, 1 = toCollege
    var type: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var second: Int = 0,
    @ColumnInfo(name = "is_last")
    @field:JvmField
    var isLast: Boolean = false,
    @ColumnInfo(name = "is_return")
    @field:JvmField
    var isReturn: Boolean = false,
    @ColumnInfo(name = "is_kaizu")
    @field:JvmField
    var isKaizu: Boolean = false,
    @ColumnInfo(name = "arrival_hour")
    var arrivalHour: Int = 0,
    @ColumnInfo(name = "arrival_minute")
    var arrivalMinute: Int = 0,
    @ColumnInfo(name = "arrival_second")
    var arrivalSecond: Int = 0,
    @ColumnInfo(name = "set_alarm")
    var setAlarm: Boolean = false
)