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
import javax.inject.Singleton


@Module
internal object DiagramModule {
    @Singleton
    @Provides
    fun provideRepository(context: App): DiagramRepository =
        DiagramRepositoryImpl(
            FirebaseObserver(),
            Room.databaseBuilder(context, DiagramDatabase::class.java, "diagram-database").build()
        )

    @Singleton
    @Provides
    fun provideUseCase(repository: DiagramRepository): DiagramUseCase =
        DiagramUseCaseImpl(repository)
}