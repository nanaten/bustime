/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity

@Database(entities = [CalendarEntity::class, DiagramEntity::class], version = 1)
abstract class DiagramDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
    abstract fun diagramDao(): DiagramDao
}