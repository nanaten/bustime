/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.network

import android.content.Context
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
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DiagramModule {
    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideRepository(
        @ApplicationContext context: Context,
        firebaseObserver: FirebaseObserver,
    ): DiagramRepository =
        DiagramRepositoryImpl(
            firebaseObserver,
            Room.databaseBuilder(context, DiagramDatabase::class.java, "diagram-database").build()
        )

    @Provides
    @Singleton
    fun provideFirebaseObserver(): FirebaseObserver = FirebaseObserver()

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUseCase(impl: DiagramUseCaseImpl): DiagramUseCase = impl
}