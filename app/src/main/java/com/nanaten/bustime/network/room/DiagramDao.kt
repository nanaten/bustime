/*
 * Created by m.coder on 2020/5/7.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nanaten.bustime.network.entity.DiagramEntity

interface DiagramDao {
    @Insert
    fun addDiagram(diagram: DiagramEntity)

    @Query("SELECT * FROM DiagramEntity")
    fun getDiagrams(): List<DiagramEntity>

    @Delete
    fun deleteDiagram(diagramEntity: DiagramEntity)
}