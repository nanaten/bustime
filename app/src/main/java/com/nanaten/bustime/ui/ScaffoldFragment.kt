package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ScaffoldFragment : DaggerFragment() {

    private var binding: FragmentScaffoldBinding by autoCleared()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: DiagramViewModel by viewModels { viewModelFactory }
    private val adapter by lazy {
        ScaffoldPagerAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scaffold, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            viewPager.adapter = adapter
            bottomNavigation.setOnNavigationItemSelectedListener { menu ->
                viewPager.currentItem =
                    HomeTabs.values().firstOrNull { it.resId == menu.itemId }?.value
                        ?: return@setOnNavigationItemSelectedListener false
                return@setOnNavigationItemSelectedListener true
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true
                mViewModel.switchPosition(position)
            }
        })
        val page = mViewModel.getFirstView()
        binding.viewPager.currentItem = page

        mViewModel.calendar.observe(viewLifecycleOwner, Observer {
            mViewModel.checkAlarm()
            getDiagrams()
        })

        // toStationDiagramsの方が後にpostValueされるのでtoStationDiagramsをobserveする
        lifecycleScope.launch {
            mViewModel.toStationDiagrams.collect {
                mViewModel.switchPosition(binding.viewPager.currentItem)
            }
        }

        return binding.root
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
