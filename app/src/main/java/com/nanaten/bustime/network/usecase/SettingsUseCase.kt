/*
 * Created by m.coder on 2020/5/26.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.usecase

import com.nanaten.bustime.network.repository.SettingsRepository
import javax.inject.Inject

interface SettingsUseCase {
    fun getFirstViewSetting(): Int
    fun setFirstViewSetting(firstView: Int)
    fun getIsDarkMode(): Boolean
    fun setIsDarkMode(isDarkMode: Boolean)
    fun getLastUpdated(): String
    fun setLastUpdated()
}

class SettingsUseCaseImpl @Inject constructor(private val repository: SettingsRepository) :
    SettingsUseCase {
    override fun getFirstViewSetting(): Int {
        return repository.getFirstViewSetting()
    }

    override fun getIsDarkMode(): Boolean {
        return repository.getIsDarkMode()
    }

    override fun getLastUpdated(): String {
        return repository.getLastUpdated()
    }

    override fun setFirstViewSetting(firstView: Int) {
        repository.setFirstViewSetting(firstView)
    }

    override fun setIsDarkMode(isDarkMode: Boolean) {
        repository.setIsDarkMode(isDarkMode)
    }

    override fun setLastUpdated() {
        repository.setLastUpdated()
    }
}