package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.nanaten.bustime.R
import com.nanaten.bustime.SharedPref
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ScaffoldFragment : DaggerFragment(), ViewPager.OnPageChangeListener {

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
            viewModel = mViewModel
            viewPager.adapter = ScaffoldPagerAdapter(childFragmentManager)
            bottomNavigation.setOnNavigationItemSelectedListener { menu ->
                viewPager.currentItem =
                    HomeTabs.values().firstOrNull { it.resId == menu.itemId }?.value
                        ?: return@setOnNavigationItemSelectedListener false
                return@setOnNavigationItemSelectedListener true
            }
        }
        binding.viewPager.addOnPageChangeListener(this)
        val page = SharedPref(requireContext()).getFirstViewSetting()
        binding.viewPager.currentItem = page

        mViewModel.calendar.observe(viewLifecycleOwner, Observer {
            getDiagrams()
        })

        if (binding.viewPager.currentItem == HomeTabs.TO_COLLAGE.value) {
            mViewModel.toCollegeDiagrams.observe(viewLifecycleOwner, Observer {
                mViewModel.switchPosition(binding.viewPager.currentItem)
            })
        } else {
            mViewModel.toStationDiagrams.observe(viewLifecycleOwner, Observer {
                mViewModel.switchPosition(binding.viewPager.currentItem)
            })
        }
        return binding.root
    }

    // UNUSED
    override fun onPageScrollStateChanged(state: Int) {
    }

    // UNUSED
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        binding.bottomNavigation.menu.getItem(position).isChecked = true
        mViewModel.switchPosition(position)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.startTimer()
        mViewModel.getCalendar()
        mViewModel.getPdf()
    }

    override fun onPause() {
        mViewModel.stopTimer()
        super.onPause()
    }

    private fun getDiagrams() {
        mViewModel.getDiagrams()
    }
}
