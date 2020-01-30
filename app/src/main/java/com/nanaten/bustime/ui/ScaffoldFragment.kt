package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import dagger.android.support.DaggerFragment


class ScaffoldFragment : DaggerFragment() {

    lateinit var binding: FragmentScaffoldBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scaffold, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewPager.adapter = ScaffoldPagerAdapter(childFragmentManager)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menu ->
            binding.viewPager.currentItem =
                HomeTabs.values().firstOrNull() { it.resId == menu.itemId }?.value
                    ?: return@setOnNavigationItemSelectedListener false
            return@setOnNavigationItemSelectedListener true
        }
        return binding.root
    }
}
