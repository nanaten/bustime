package com.nanaten.bustime.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ScaffoldFragment : DaggerFragment() {

    private var binding: FragmentScaffoldBinding by autoCleared()
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: DiagramViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scaffold, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            this.viewModel = mViewModel
            viewPager.adapter = ScaffoldPagerAdapter(childFragmentManager)
            bottomNavigation.setOnNavigationItemSelectedListener { menu ->
                viewPager.currentItem =
                    HomeTabs.values().firstOrNull { it.resId == menu.itemId }?.value
                        ?: return@setOnNavigationItemSelectedListener false
                return@setOnNavigationItemSelectedListener true
            }
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                    bottomNavigation.menu.getItem(position).isChecked = true
                }
            })
        }

        return binding.root
    }
}
