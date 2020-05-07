/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nanaten.bustime.network.entity.DiagramEntity

@Dao
interface DiagramDao {
    @Insert
    fun addDiagram(diagram: DiagramEntity)

    @Query("SELECT * FROM DiagramEntity WHERE type = :type")
    fun getDiagrams(type: Int): List<DiagramEntity>

    @Query("DELETE FROM DiagramEntity")
    fun deleteDiagramAll()
}