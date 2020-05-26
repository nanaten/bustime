/*
 * Created by m.coder on 2020/5/26.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.repository

import com.nanaten.bustime.LocalDataStore
import javax.inject.Inject

interface SettingsRepository {
    fun getFirstViewSetting(): Int
    fun setFirstViewSetting(firstView: Int)
    fun getIsDarkMode(): Boolean
    fun setIsDarkMode(isDarkMode: Boolean)
    fun getLastUpdated(): String
    fun setLastUpdated()
}

class SettingsRepositoryImpl @Inject constructor(private val localDataStore: LocalDataStore) :
    SettingsRepository {
    override fun getFirstViewSetting(): Int {
        return localDataStore.getFirstViewSetting()
    }

    override fun getLastUpdated(): String {
        return localDataStore.getLastUpdated()
    }

    override fun setFirstViewSetting(firstView: Int) {
        localDataStore.setFirstViewSetting(firstView)
    }

    override fun setLastUpdated() {
        localDataStore.setLastUpdated()
    }

    override fun getIsDarkMode(): Boolean {
        return localDataStore.getIsDarkMode()
    }

    override fun setIsDarkMode(isDarkMode: Boolean) {
        localDataStore.setIsDarkMode(isDarkMode)
    }
}