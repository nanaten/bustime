/*
 * Created by m.coder on 2020/5/26.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.network

import com.nanaten.bustime.App
import com.nanaten.bustime.SharedPrefImpl
import com.nanaten.bustime.network.repository.SettingsRepository
import com.nanaten.bustime.network.repository.SettingsRepositoryImpl
import com.nanaten.bustime.network.usecase.SettingsUseCase
import com.nanaten.bustime.network.usecase.SettingsUseCaseImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
class SettingsModule {
    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideRepository(app: App): SettingsRepository =
        SettingsRepositoryImpl(
            SharedPrefImpl(app)
        )

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideUseCase(repository: SettingsRepository): SettingsUseCase =
        SettingsUseCaseImpl(repository)
}