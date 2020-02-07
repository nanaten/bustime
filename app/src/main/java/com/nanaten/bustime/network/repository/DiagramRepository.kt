/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DiagramRepository {
    suspend fun getTodayCalendar(): Flow<CalendarEntity>
    suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<DiagramEntity>>
}

class DiagramRepositoryImpl @Inject constructor(private val firebaseObserver: FirebaseObserver) :
    DiagramRepository {
    override suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return firebaseObserver.getTodayCalendar()
    }

    override suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<DiagramEntity>> {
        return firebaseObserver.getDiagrams(diagramName, now)
    }
}