/*
 * Created by m.coder on 2020/4/28.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.nanaten.bustime.network.entity.*
import com.nanaten.bustime.network.repository.DiagramRepository
import com.nanaten.bustime.network.repository.SettingsRepository
import com.nanaten.bustime.network.usecase.DiagramUseCaseImpl
import com.nanaten.bustime.network.usecase.SettingsUseCaseImpl
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DiagramViewModelTest {

    private lateinit var mockRepository: DiagramRepository
    private lateinit var mockSettingsRepository: SettingsRepository
    private lateinit var viewModel: DiagramViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        mockRepository = getMockRepository()
        mockSettingsRepository = getMockSettingRepository()
        viewModel = getViewModel()
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel.networkResult.call(null)
        viewModel.calendar.value = null
        viewModel.nowSecond.value = null
        viewModel.pdfUrl.value = null
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getCalendarNew() {
        runBlocking {
            viewModel.getCalendar()
            coVerify { mockRepository.getTodayCalendar() }
            Truth.assertThat(viewModel.calendar.value?.date).isEqualTo("2020-04-28")
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Success)
        }
    }

    @Test
    fun getCalendarFailure() {
        mockRepository = getMockRepositoryError()
        viewModel = getViewModel()

        runBlocking {
            viewModel.getCalendar()
            coVerify { mockRepository.getTodayCalendar() }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Error)
        }
    }

    @Test
    fun getDiagramsSuccess() {
        viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", false)
        runBlocking {
            viewModel.getDiagrams()
            coVerify { mockRepository.getDiagrams(any(), any()) }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Success)
            Truth.assertThat(viewModel.toCollegeDiagrams.value).isNotEmpty()
            Truth.assertThat(viewModel.toStationDiagrams.value).isNotEmpty()
            Truth.assertThat(viewModel.toStationDiagrams.value.size).isEqualTo(2)
        }
    }

    @Test
    fun getDiagramsFailure() {
        mockRepository = getMockRepositoryError()
        viewModel = getViewModel()
        viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", false)

        runBlocking {
            viewModel.getDiagrams()
            coVerify { mockRepository.getDiagrams(any(), any()) }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Error)
        }
    }

    @Test
    fun startTimer() {
        runBlocking {
            viewModel.startTimer()
            Truth.assertThat(viewModel.getAppIsActive()).isTrue()
            Truth.assertThat(viewModel.nowSecond.value).isNotNull()
        }
    }

    @Test
    fun stopTimer() {
        runBlocking {
            viewModel.stopTimer()
            Truth.assertThat(viewModel.getAppIsActive()).isFalse()
        }
    }

    @Test
    fun getPdfSuccess() {
        runBlocking {
            viewModel.getPdf()
            coVerify { mockRepository.getPdfUrl() }
            Truth.assertThat(viewModel.pdfUrl.value?.calendar)
                .isEqualTo("https://pdfurl.jp/calendar")
            Truth.assertThat(viewModel.pdfUrl.value?.timeTable)
                .isEqualTo("https://pdfurl.jp/timetable")
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Success)
        }
    }

    @Test
    fun getPdfFailure() {
        mockRepository = getMockRepositoryError()
        viewModel = getViewModel()
        viewModel.pdfUrl.value = null

        runBlocking {
            viewModel.getPdf()
            coVerify { mockRepository.getPdfUrl() }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Error)
        }
    }

    @Test
    fun switchPosition() {
        runBlocking {
            viewModel.toCollegeDiagrams.value = listOf(
                Diagram(
                    1, 0, 18, 30, 66600,
                    isLast = false, isReturn = false, isKaizu = false,
                    arrivalHour = 18, arrivalMinute = 45, arrivalSecond = 67500
                )
            )
            viewModel.toStationDiagrams.value = listOf(
                Diagram(
                    1, 0, 9, 54, 35640,
                    isLast = false, isReturn = false, isKaizu = false,
                    arrivalHour = 10, arrivalMinute = 9, arrivalSecond = 36540
                )
            )
            viewModel.switchPosition(0)
            Truth.assertThat(viewModel.diagrams.value?.get(0)?.hour).isEqualTo(9)
        }
    }

    private fun getMockRepository(): DiagramRepository {
        return mockk {
            coEvery { getTodayCalendar() } returns flowOf(
                CalendarEntity(1, "2020-04-28", "b", "Bダイヤ", false)
            )
            coEvery { getDiagrams(any(), any()) } returns flowOf(
                Pair(
                    listOf(
                        DiagramEntity(
                            1, 0, 18, 30, 66600,
                            isLast = false, isReturn = false, isKaizu = false,
                            arrivalHour = 18, arrivalMinute = 45, arrivalSecond = 67500
                        ),
                        DiagramEntity(
                            1, 0, 9, 54, 35640,
                            isLast = false, isReturn = false, isKaizu = false,
                            arrivalHour = 10, arrivalMinute = 9, arrivalSecond = 36540
                        )
                    ),
                    listOf(
                        DiagramEntity(
                            1, 1, 18, 30, 66600,
                            isLast = false, isReturn = false, isKaizu = false,
                            arrivalHour = 18, arrivalMinute = 45, arrivalSecond = 67500
                        ),
                        DiagramEntity(
                            1, 1, 9, 54, 35640,
                            isLast = false, isReturn = false, isKaizu = false,
                            arrivalHour = 10, arrivalMinute = 9, arrivalSecond = 36540
                        )
                    )
                )
            )
            coEvery { getPdfUrl() } returns flowOf(
                RemotePdfEntity(
                    "https://pdfurl.jp/calendar",
                    "https://pdfurl.jp/timetable"
                )
            )
            coEvery { getAlarm() } returns null
        }
    }

    private fun getMockRepositoryError(): DiagramRepository {
        return mockk {
            coEvery { getTodayCalendar() } throws Exception()
            coEvery { getDiagrams(any(), any()) } throws Exception()
            coEvery { getPdfUrl() } throws Exception()
            coEvery { getAlarm() } returns null
        }
    }

    private fun getMockSettingRepository(): SettingsRepository {
        return mockk {
            coEvery { getLastUpdated() } returns "2020-04-27"
            coEvery { getFirstViewSetting() } returns 0
            coEvery { setLastUpdated() } just runs
        }
    }

    private fun getViewModel(): DiagramViewModel {
        return DiagramViewModel(
            DiagramUseCaseImpl(
                mockRepository
            ),
            SettingsUseCaseImpl(
                getMockSettingRepository()
            )
        )
    }
}