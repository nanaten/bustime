/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.usecase

import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.network.entity.AlarmEntity
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.RemotePdf
import com.nanaten.bustime.network.repository.DiagramRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DiagramUseCase {
    suspend fun getTodayCalendar(): Flow<Calendar>
    suspend fun getDiagrams(
        diagramName: String,
        cache: Boolean
    ): Flow<Pair<List<Diagram>, List<Diagram>>>

    suspend fun getPdfUrl(): Flow<RemotePdf>
    suspend fun saveAlarm(diagram: Diagram)
    suspend fun deleteAlarm()
}


@ExperimentalCoroutinesApi
class DiagramUseCaseImpl @Inject constructor(private val repository: DiagramRepository) :
    DiagramUseCase {
    override suspend fun getTodayCalendar(): Flow<Calendar> {
        return repository.getTodayCalendar()
            .map {
                Calendar.convertFrom(it)
            }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getDiagrams(
        diagramName: String,
        cache: Boolean
    ): Flow<Pair<List<Diagram>, List<Diagram>>> {
        return repository.getDiagrams(diagramName, cache)
            .map { pair ->
                val toCollege = pair.first
                val toStation = pair.second
                val alarm = repository.getAlarm()
                // アラームがセット済みの場合
                if (alarm != null) {
                    if (alarm.type == HomeTabs.TO_COLLAGE.value)
                        toCollege.firstOrNull { it.id == alarm.id }?.setAlarm = alarm.setAlarm
                    else
                        toStation.firstOrNull { it.id == alarm.id }?.setAlarm = alarm.setAlarm
                }
                Pair(
                    toCollege.map {
                        Diagram.convertFrom(it)
                    },
                    toStation.map {
                        Diagram.convertFrom(it)
                    }
                )
            }
    }

    override suspend fun getPdfUrl(): Flow<RemotePdf> {
        return repository.getPdfUrl()
            .map {
                RemotePdf.convertFrom(it)
            }
    }

    override suspend fun saveAlarm(diagram: Diagram) {
        withContext(Dispatchers.Default) {
            val diagramEntity = Diagram.convertToEntity(diagram)
            repository.saveDiagram(diagramEntity)
            val alarm = AlarmEntity.setFromDiagram(diagram)
            repository.saveAlarm(alarm)
        }
    }

    override suspend fun deleteAlarm() {
        repository.deleteAlarm()
    }
}