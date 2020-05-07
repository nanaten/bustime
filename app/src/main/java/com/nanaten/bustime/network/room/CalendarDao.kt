/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanaten.bustime.network.entity.CalendarEntity

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCalendar(calendar: CalendarEntity)

    @Query("SELECT * FROM CalendarEntity WHERE date = :date")
    fun getCalendar(date: String): List<CalendarEntity>

    @Query("DELETE FROM CalendarEntity")
    fun deleteCalendar()
}