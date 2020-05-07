/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.network

import androidx.room.Room
import com.nanaten.bustime.App
import com.nanaten.bustime.network.FirebaseObserver
import com.nanaten.bustime.network.repository.DiagramRepository
import com.nanaten.bustime.network.repository.DiagramRepositoryImpl
import com.nanaten.bustime.network.room.DiagramDatabase
import com.nanaten.bustime.network.usecase.DiagramUseCase
import com.nanaten.bustime.network.usecase.DiagramUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
class DiagramModule {
    @Singleton
    @Provides
    fun provideRepository(app: App): DiagramRepository =
        DiagramRepositoryImpl(
            FirebaseObserver(),
            Room.databaseBuilder(app, DiagramDatabase::class.java, "diagram-database").build()
        )

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUseCase(repository: DiagramRepository): DiagramUseCase =
        DiagramUseCaseImpl(repository)
}