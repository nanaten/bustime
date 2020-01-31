package com.nanaten.bustime.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import com.nanaten.bustime.util.autoCleared
import dagger.android.support.DaggerFragment


class ScaffoldFragment : DaggerFragment() {

    private var binding: FragmentScaffoldBinding by autoCleared()

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
                HomeTabs.values().firstOrNull { it.resId == menu.itemId }?.value
                    ?: return@setOnNavigationItemSelectedListener false
            return@setOnNavigationItemSelectedListener true
        }
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }


            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                
            }

            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })
        return binding.root
    }
}
