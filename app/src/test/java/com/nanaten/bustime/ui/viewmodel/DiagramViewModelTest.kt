/*
 * Created by m.coder on 2020/4/28.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.nanaten.bustime.network.entity.*
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.repository.DiagramRepository
import com.nanaten.bustime.network.usecase.DiagramUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class DiagramViewModelTest {

    private lateinit var mockRepository: DiagramRepository
    private lateinit var viewModel: DiagramViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockRepository = getMockRepository()
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
    fun getCalendarAvailable() {
        runBlocking {
            viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", true)
            viewModel.getCalendar()
            coVerify(exactly = 0) { mockRepository.getTodayCalendar() }
            Truth.assertThat(viewModel.calendar.value?.isSuspend).isTrue()
            Truth.assertThat(viewModel.networkResult.value).isNull()
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
        viewModel.oldDate.value = null
        viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", false)
        runBlocking {
            viewModel.getDiagrams()
            coVerify { mockRepository.getDiagrams(any()) }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Success)
            Truth.assertThat(viewModel.toCollegeDiagrams.value).isNotEmpty()
            Truth.assertThat(viewModel.toStationDiagrams.value).isNotEmpty()
            Truth.assertThat(viewModel.toStationDiagrams.value?.size).isEqualTo(2)
        }
    }

    @Test
    fun getDiagramsOldDateAvailable() {
        viewModel.oldDate.value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", false)
        runBlocking {
            viewModel.getDiagrams()
            coVerify(exactly = 0) { mockRepository.getDiagrams(any()) }
        }
    }

    @Test
    fun getDiagramsFailure() {
        mockRepository = getMockRepositoryError()
        viewModel = getViewModel()
        viewModel.oldDate.value = null
        viewModel.calendar.value = Calendar("2020-04-28", "b", "Bダイヤ", false)

        runBlocking {
            viewModel.getDiagrams()
            coVerify { mockRepository.getDiagrams(any()) }
            Truth.assertThat(viewModel.networkResult.value).isEqualTo(NetworkResult.Error)
        }
    }

    @Test
    fun startTimer() {
        runBlocking {
            viewModel.startTimer()
            Truth.assertThat(viewModel.appIsActive.get()).isTrue()
            Truth.assertThat(viewModel.nowSecond.value).isNotNull()
        }
    }

    @Test
    fun stopTimer() {
        runBlocking {
            viewModel.stopTimer()
            Truth.assertThat(viewModel.appIsActive.get()).isFalse()
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
                    18, 30, 66600,
                    isLast = false, isReturn = false, isKaizu = false,
                    arrivalHour = 18, arrivalMinute = 45, arrivalSecond = 67500
                )
            )
            viewModel.toStationDiagrams.value = listOf(
                Diagram(
                    9, 54, 35640,
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
                CalendarEntity("2020-04-28", "b", "Bダイヤ", false)
            )
            coEvery { getDiagrams(any()) } returns flowOf(
                listOf(
                    DiagramEntity(
                        18, 30, 66600,
                        isLast = false, isReturn = false, isKaizu = false,
                        arrivalHour = 18, arrivalMinute = 45, arrivalSecond = 67500
                    ),
                    DiagramEntity(
                        9, 54, 35640,
                        isLast = false, isReturn = false, isKaizu = false,
                        arrivalHour = 10, arrivalMinute = 9, arrivalSecond = 36540
                    )
                )
            )
            coEvery { getPdfUrl() } returns flowOf(
                RemotePdfEntity(
                    "https://pdfurl.jp/calendar",
                    "https://pdfurl.jp/timetable"
                )
            )
        }
    }

    private fun getMockRepositoryError(): DiagramRepository {
        return mockk {
            coEvery { getTodayCalendar() } throws Exception()
            coEvery { getDiagrams(any()) } throws Exception()
            coEvery { getPdfUrl() } throws Exception()
        }
    }

    private fun getViewModel(): DiagramViewModel {
        return DiagramViewModel(
            DiagramUseCaseImpl(
                mockRepository
            )
        )
    }
}