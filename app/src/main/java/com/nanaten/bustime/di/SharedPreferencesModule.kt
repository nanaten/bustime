/*
 * Created by m.coder on 2020/5/25.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di

import com.nanaten.bustime.App
import com.nanaten.bustime.SharedPref
import com.nanaten.bustime.SharedPrefImpl
import dagger.Module
import dagger.Provides

@Module
@Suppress("UNUSED")
class SharedPreferencesModule {
    @Provides
    fun provideSharedPref(app: App): SharedPref {
        return SharedPrefImpl(app)
    }
}