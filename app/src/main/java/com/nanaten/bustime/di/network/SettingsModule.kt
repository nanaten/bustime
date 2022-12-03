/*
 * Created by m.coder on 2020/5/26.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.network

import android.content.Context
import com.nanaten.bustime.App
import com.nanaten.bustime.SharedPrefImpl
import com.nanaten.bustime.network.repository.SettingsRepository
import com.nanaten.bustime.network.repository.SettingsRepositoryImpl
import com.nanaten.bustime.network.usecase.SettingsUseCase
import com.nanaten.bustime.network.usecase.SettingsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepositoryImpl(
            SharedPrefImpl(context)
        )

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUseCase(impl: SettingsUseCaseImpl): SettingsUseCase =
        impl
}