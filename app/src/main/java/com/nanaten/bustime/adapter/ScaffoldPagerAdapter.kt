/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nanaten.bustime.R
import com.nanaten.bustime.ui.ToCollegeFragment
import com.nanaten.bustime.ui.ToStationFragment

class ScaffoldPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return HomeTabs.values().size
    }

    override fun getItem(position: Int): Fragment {
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