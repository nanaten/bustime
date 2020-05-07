/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CalendarEntity(
    @PrimaryKey
    val id: Int = 1,
    var date: String = "",
    var diagram: String = "",
    @ColumnInfo(name = "diagram_name")
    var diagramName: String = "",
    @ColumnInfo(name = "is_suspend")
    var isSuspend: Boolean = false
)