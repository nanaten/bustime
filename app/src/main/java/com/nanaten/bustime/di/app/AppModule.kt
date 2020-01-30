/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.di.app

import android.content.Context
import com.nanaten.bustime.App
import dagger.Binds
import dagger.Module


@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: App): Context

}