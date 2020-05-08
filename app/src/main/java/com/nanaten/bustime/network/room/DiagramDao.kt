/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanaten.bustime.network.entity.DiagramEntity

@Dao
interface DiagramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDiagram(diagram: DiagramEntity)

    @Query("SELECT * FROM DiagramEntity ORDER BY second ASC")
    fun getDiagramsAll(): List<DiagramEntity>

    @Query("UPDATE DiagramEntity SET set_alarm = :setAlarm")
    fun setAlarmAll(setAlarm: Boolean)

    @Query("DELETE FROM DiagramEntity")
    fun deleteDiagramAll()
}