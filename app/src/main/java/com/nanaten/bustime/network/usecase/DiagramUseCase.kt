/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.usecase

import com.nanaten.bustime.Const
import com.nanaten.bustime.adapter.HomeTabs
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
    suspend fun getDiagrams(
        diagramName: String,
        cache: Boolean
    ): Flow<Pair<List<Diagram>, List<Diagram>>>

    suspend fun getPdfUrl(): Flow<RemotePdf>
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
        return repository.getDiagrams(
            "$diagramName${Const.TO_COL}",
            HomeTabs.TO_COLLAGE.value,
            cache
        )
            .zip(
                repository.getDiagrams(
                    "$diagramName${Const.TO_STA}",
                    HomeTabs.TO_STATION.value,
                    cache
                )
            ) { toCollege, toStation ->
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
}