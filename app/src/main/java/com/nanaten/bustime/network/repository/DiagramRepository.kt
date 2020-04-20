/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import com.nanaten.bustime.network.entity.RemotePdfEntity
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DiagramRepository {
    suspend fun getTodayCalendar(): Flow<CalendarEntity>
    suspend fun getDiagrams(diagramName: String): Flow<List<DiagramEntity>>
    suspend fun getPdfUrl(): Flow<RemotePdfEntity>
}

class DiagramRepositoryImpl @Inject constructor(private val firebaseObserver: FirebaseObserver) :
    DiagramRepository {
    override suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return firebaseObserver.getTodayCalendar()
    }

    override suspend fun getDiagrams(diagramName: String): Flow<List<DiagramEntity>> {
        return firebaseObserver.getDiagrams(diagramName)
    }

    override suspend fun getPdfUrl(): Flow<RemotePdfEntity> {
        return firebaseObserver.getPdfUrl()
            .map {
                withContext(Dispatchers.IO) {
                    val adapter = Moshi.Builder().build().adapter(
                        RemotePdfEntity::class.java
                    )
                    adapter.fromJson(it) ?: RemotePdfEntity()
                }
            }
    }
}