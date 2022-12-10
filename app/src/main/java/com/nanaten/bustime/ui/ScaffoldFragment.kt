package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.HomeTabs
import com.nanaten.bustime.adapter.ScaffoldPagerAdapter
import com.nanaten.bustime.databinding.FragmentScaffoldBinding
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScaffoldFragment : Fragment() {

    private var _binding: FragmentScaffoldBinding? = null
    private val binding: FragmentScaffoldBinding get() = requireNotNull(_binding)
    private val mViewModel: DiagramViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scaffold, container, false)
        binding.apply {
            toolbar.setToolbar(
                backVisibility = View.GONE,
                settingVisibility = View.VISIBLE,
                settingListener = {
                    findNavController().navigate(R.id.action_home_to_settings)
                }
            )
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel

            bottomNavigation.setOnNavigationItemSelectedListener { menu ->
                viewPager.currentItem =
                    HomeTabs.values().firstOrNull { it.resId == menu.itemId }?.value
                        ?: return@setOnNavigationItemSelectedListener false
                return@setOnNavigationItemSelectedListener true
            }
        }
        binding.viewPager.adapter = ScaffoldPagerAdapter(this)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.menu.getItem(position).isChecked = true
                mViewModel.switchPosition(position)
            }
        })
        val page = mViewModel.getFirstView()
        binding.viewPager.setCurrentItem(page, false)

        mViewModel.calendar.observe(viewLifecycleOwner) {
            mViewModel.checkAlarm()
            getDiagrams()
        }

        lifecycleScope.launch {
            mViewModel.mergedDiagrams.observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
