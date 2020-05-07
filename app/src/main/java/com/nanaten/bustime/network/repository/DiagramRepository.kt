/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import androidx.room.withTransaction
import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import com.nanaten.bustime.network.entity.RemotePdfEntity
import com.nanaten.bustime.network.room.DiagramDatabase
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface DiagramRepository {
    suspend fun getTodayCalendar(): Flow<CalendarEntity>
    suspend fun getDiagrams(
        diagramName: String,
        type: Int,
        cache: Boolean
    ): Flow<List<DiagramEntity>>

    suspend fun getPdfUrl(): Flow<RemotePdfEntity>
    suspend fun saveCalendar(calendar: CalendarEntity): CalendarEntity
    suspend fun saveDiagrams(list: List<DiagramEntity>): List<DiagramEntity>
    suspend fun saveDiagram(diagram: DiagramEntity)
    suspend fun getCalendarFromCache(): CalendarEntity?
    suspend fun getDiagramsFromCache(type: Int): List<DiagramEntity>
}

class DiagramRepositoryImpl @Inject constructor(
    private val firebaseObserver: FirebaseObserver,
    private val db: DiagramDatabase
) :
    DiagramRepository {
    override suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return if (getCalendarFromCache() != null) {
            flowOf(getCalendarFromCache()!!)
        } else {
            firebaseObserver.getTodayCalendar()
                .apply { saveCalendar(this.single()) }
        }
    }

    override suspend fun getDiagrams(
        diagramName: String,
        type: Int,
        cache: Boolean
    ): Flow<List<DiagramEntity>> {
        return if (cache) {
            if (getDiagramsFromCache(type).isNullOrEmpty()) {
                firebaseObserver.getDiagrams(diagramName, type)
                    .apply { saveDiagrams(this.single()) }
            } else {
                flowOf(getDiagramsFromCache(type))
            }
        } else {
            firebaseObserver.getDiagrams(diagramName, type)
                .apply { saveDiagrams(this.single()) }
        }
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

    override suspend fun saveCalendar(calendar: CalendarEntity): CalendarEntity {
        return withContext(Dispatchers.IO) {
            db.withTransaction {
                db.calendarDao().addCalendar(calendar)
            }
            calendar
        }
    }

    override suspend fun saveDiagrams(list: List<DiagramEntity>): List<DiagramEntity> {
        return withContext(Dispatchers.IO) {
            db.withTransaction {
                db.diagramDao().deleteDiagramAll()
                list.forEach {
                    db.diagramDao().addDiagram(it)
                }
            }
            list
        }
    }

    override suspend fun saveDiagram(diagram: DiagramEntity) {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                db.diagramDao().addDiagram(diagram)
            }
        }
    }

    override suspend fun getCalendarFromCache(): CalendarEntity? {
        return withContext(Dispatchers.IO) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            db.calendarDao().getCalendar(today).firstOrNull()
        }
    }

    override suspend fun getDiagramsFromCache(type: Int): List<DiagramEntity> {
        return withContext(Dispatchers.IO) { db.diagramDao().getDiagrams(type) }
    }
}