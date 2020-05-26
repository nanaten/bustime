/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import androidx.lifecycle.ViewModel
import com.nanaten.bustime.di.viewmodel.ViewModelKey
import com.nanaten.bustime.ui.ToStationFragment
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@Suppress("UNUSED")
@ExperimentalCoroutinesApi
abstract class ToStationFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DiagramViewModel::class)
    abstract fun bindViewModel(viewModel: DiagramViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun provideFragment(): ToStationFragment

}