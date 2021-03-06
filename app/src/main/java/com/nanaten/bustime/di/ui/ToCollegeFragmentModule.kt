/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.ui

import com.nanaten.bustime.ui.ToCollegeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Module
@Suppress("UNUSED")
@ExperimentalCoroutinesApi
abstract class ToCollegeFragmentModule {
    @ContributesAndroidInjector
    abstract fun provideFragment(): ToCollegeFragment

}