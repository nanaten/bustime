/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nanaten.bustime.R
import com.nanaten.bustime.ui.ToCollegeFragment
import com.nanaten.bustime.ui.ToStationFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ScaffoldPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return HomeTabs.values().size
    }

    @ExperimentalCoroutinesApi
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HomeTabs.TO_STATION.value -> ToStationFragment()
            else -> ToCollegeFragment()
        }
    }
}

enum class HomeTabs(val value: Int, val resId: Int) {
    TO_STATION(0, R.id.to_station),
    TO_COLLAGE(1, R.id.to_college)
}