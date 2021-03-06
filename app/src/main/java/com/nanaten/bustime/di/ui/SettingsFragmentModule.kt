/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import androidx.lifecycle.ViewModel
import com.nanaten.bustime.di.viewmodel.ViewModelKey
import com.nanaten.bustime.ui.SettingsFragment
import com.nanaten.bustime.ui.viewmodel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
@Suppress("UNUSED")
internal abstract class SettingsFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun provideFragment(): SettingsFragment

}