/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.*
import com.nanaten.bustime.network.entity.CalendarEntity

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCalendar(calendar: CalendarEntity)

    @Query("SELECT 1 FROM CalendarEntity")
    fun getCalendar(): CalendarEntity

    @Delete
    fun deleteCalendar(calendar: CalendarEntity)
}