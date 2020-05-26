/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import com.nanaten.bustime.di.ActivityScope
import com.nanaten.bustime.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
abstract class MainActivityBuilder {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            ScaffoldFragmentModule::class,
            ToCollegeFragmentModule::class,
            ToStationFragmentModule::class,
            SettingsFragmentModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity
}