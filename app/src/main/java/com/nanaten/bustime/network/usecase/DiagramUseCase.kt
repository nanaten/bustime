/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.usecase

import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.RemotePdf
import com.nanaten.bustime.network.repository.DiagramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface DiagramUseCase {
    suspend fun getTodayCalendar(): Flow<Calendar>
    suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<Diagram>>
    suspend fun getPdfUrl(): Flow<RemotePdf>
}

class DiagramUseCaseImpl @Inject constructor(private val repository: DiagramRepository) :
    DiagramUseCase {
    override suspend fun getTodayCalendar(): Flow<Calendar> {
        return repository.getTodayCalendar()
            .map {
                Calendar.convertFrom(it)
            }
    }

    override suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<Diagram>> {
        return repository.getDiagrams(diagramName, now)
            .map { list ->
                list.map { Diagram.convertFrom(it) }
            }
    }

    override suspend fun getPdfUrl(): Flow<RemotePdf> {
        return repository.getPdfUrl()
            .map {
                RemotePdf.convertFrom(it)
            }
    }
}