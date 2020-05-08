/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import android.util.Log
import androidx.room.withTransaction
import com.nanaten.bustime.Const
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.entity.AlarmEntity
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import com.nanaten.bustime.network.entity.RemotePdfEntity
import com.nanaten.bustime.network.room.DiagramDatabase
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface DiagramRepository {
    suspend fun getTodayCalendar(): Flow<CalendarEntity>
    suspend fun getDiagrams(
        diagramName: String,
        cache: Boolean
    ): Flow<Pair<List<DiagramEntity>, List<DiagramEntity>>>

    suspend fun getPdfUrl(): Flow<RemotePdfEntity>
    suspend fun saveCalendar(calendar: CalendarEntity): CalendarEntity
    suspend fun saveDiagrams(toCollege: List<DiagramEntity>, toStation: List<DiagramEntity>)
    suspend fun saveAlarm(alarm: AlarmEntity)
    suspend fun getAlarm(): AlarmEntity?
    suspend fun getCalendarFromCache(): CalendarEntity?
    suspend fun getDiagramsFromCache(): List<DiagramEntity>
    suspend fun deleteAlarm()
    suspend fun saveDiagram(diagram: DiagramEntity)
}

@ExperimentalCoroutinesApi
class DiagramRepositoryImpl @Inject constructor(
    private val firebaseObserver: FirebaseObserver,
    private val db: DiagramDatabase
) :
    DiagramRepository {
    override suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return withContext(Dispatchers.Default) {
            if (getCalendarFromCache() != null) {
                flowOf(getCalendarFromCache()!!)
            } else {
                firebaseObserver.getTodayCalendar()
                    .apply { saveCalendar(single()) }
            }
        }
    }

    override suspend fun getDiagrams(
        diagramName: String,
        cache: Boolean
    ): Flow<Pair<List<DiagramEntity>, List<DiagramEntity>>> {
        return withContext(Dispatchers.Default) {
            if (cache) {
                val list = getDiagramsFromCache()
                if (list.isNullOrEmpty()) {
                    Log.d("Firestore", "getDiagrams isNullOrEmpty")
                    getDiagramsForFirestore(diagramName)
                } else {
                    Log.d("Room", "getDiagramsFromCache")
                    flowOf(
                        Pair(
                            list.filter { it.type == HomeTabs.TO_COLLAGE.value },
                            list.filter { it.type == HomeTabs.TO_STATION.value }
                        )
                    )
                }
            } else {
                Log.d("Firestore", "getDiagrams")
                getDiagramsForFirestore(diagramName)
            }
        }
    }

    private suspend fun getDiagramsForFirestore(diagramName: String): Flow<Pair<List<DiagramEntity>, List<DiagramEntity>>> {
        return firebaseObserver.getDiagrams(
            "$diagramName${Const.TO_COL}",
            HomeTabs.TO_COLLAGE.value
        ).zip(
            firebaseObserver.getDiagrams(
                "$diagramName${Const.TO_STA}",
                HomeTabs.TO_STATION.value
            )
        ) { toCollege, toStation ->
            saveDiagrams(toCollege, toStation)
            Pair(toCollege, toStation)
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
        db.withTransaction {
            db.calendarDao().addCalendar(calendar)
        }
        return calendar
    }

    override suspend fun saveDiagrams(
        toCollege: List<DiagramEntity>,
        toStation: List<DiagramEntity>
    ) {
        db.withTransaction {
            db.diagramDao().deleteDiagramAll()
            toCollege.forEach {
                Log.d("Room saveDiagrams", it.toString())
                db.diagramDao().addDiagram(it)
            }
            toStation.forEach {
                db.diagramDao().addDiagram(it)
            }
        }
    }

    override suspend fun saveAlarm(alarm: AlarmEntity) {
        db.withTransaction {
            db.alarmDao().delete()
            db.alarmDao().addAlarm(alarm)
        }
    }

    override suspend fun getAlarm(): AlarmEntity? {
        return withContext(Dispatchers.Default) {
            db.alarmDao().getAlarm().firstOrNull()
        }
    }

    override suspend fun getCalendarFromCache(): CalendarEntity? {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return db.calendarDao().getCalendar(today).firstOrNull()
    }

    override suspend fun getDiagramsFromCache(): List<DiagramEntity> {
        return db.diagramDao().getDiagramsAll()
    }

    override suspend fun deleteAlarm() {
        withContext(Dispatchers.Default) {
            db.withTransaction {
                db.alarmDao().delete()
            }
        }
    }

    override suspend fun saveDiagram(diagram: DiagramEntity) {
        db.withTransaction {
            db.diagramDao().setAlarmAll(false)
            db.diagramDao().addDiagram(diagram)
        }
    }
}