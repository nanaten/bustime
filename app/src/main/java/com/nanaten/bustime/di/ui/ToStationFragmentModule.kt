/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import com.nanaten.bustime.ui.ToCollegeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
internal abstract class ToStationFragmentModule {


    @ContributesAndroidInjector
    abstract fun provideFragment(): ToStationFragmentModule

}