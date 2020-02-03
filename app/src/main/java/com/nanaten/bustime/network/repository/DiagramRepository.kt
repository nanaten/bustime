/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DiagramRepository {
    suspend fun getTodayCalendar(): Flow<CalendarEntity>
}

class DiagramRepositoryImpl @Inject constructor(private val firebaseObserver: FirebaseObserver) :
    DiagramRepository {
    override suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return firebaseObserver.getTodayCalendar()
    }
}