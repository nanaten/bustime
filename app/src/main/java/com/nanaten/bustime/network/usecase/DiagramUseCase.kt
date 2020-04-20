/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.usecase

import com.nanaten.bustime.Const
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.RemotePdf
import com.nanaten.bustime.network.repository.DiagramRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

interface DiagramUseCase {
    suspend fun getTodayCalendar(): Flow<Calendar>
    suspend fun getDiagrams(diagramName: String): Flow<Pair<List<Diagram>, List<Diagram>>>
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

    @ExperimentalCoroutinesApi
    override suspend fun getDiagrams(diagramName: String): Flow<Pair<List<Diagram>, List<Diagram>>> {
        return repository.getDiagrams("$diagramName${Const.TO_COL}")
            .zip(repository.getDiagrams("$diagramName${Const.TO_STA}")) { t1, t2 ->
                Pair(
                    t1.map {
                        Diagram.convertFrom(it)
                    },
                    t2.map {
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
}