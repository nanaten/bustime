/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanaten.bustime.network.entity.AlarmEntity

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAlarm(alarm: AlarmEntity)

    @Query("SELECT * FROM AlarmEntity")
    fun getAlarm(): List<AlarmEntity>

    @Query("DELETE FROM AlarmEntity")
    fun delete()
}