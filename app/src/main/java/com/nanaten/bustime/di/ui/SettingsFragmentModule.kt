/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import androidx.lifecycle.ViewModel
import com.nanaten.bustime.ui.SettingsFragment
import com.nanaten.bustime.ui.viewmodel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
@Suppress("UNUSED")
internal abstract class SettingsFragmentModule {

    @Binds
    abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun provideFragment(): SettingsFragment

}