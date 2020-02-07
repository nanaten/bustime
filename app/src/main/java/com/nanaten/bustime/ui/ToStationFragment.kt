package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.DiagramAdapter
import com.nanaten.bustime.databinding.FragmentToStationBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ToStationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: DiagramViewModel by viewModels { viewModelFactory }
    private var binding: FragmentToStationBinding by autoCleared()
    private val tabPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_station, container, false)
        val mAdapter = DiagramAdapter(mViewModel, tabPosition)
        binding.apply {
            toolbar.setToolbar(
                getString(R.string.to_station_label),
                backVisibility = View.GONE,
                settingVisibility = View.VISIBLE
            )
            toStationRv.layoutManager = LinearLayoutManager(context)
            toStationRv.adapter = mAdapter
            (toStationRv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        mViewModel.calendar.observe(viewLifecycleOwner, Observer {
            mAdapter.updateCalendar(it)
            getDiagrams()
        })


        mViewModel.next.observe(viewLifecycleOwner, Observer {
            mAdapter.updateTime()
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.startTimer()
        getDiagrams()
    }

    private fun getDiagrams() {
        val target = if (tabPosition == 0) "ToCollege" else "ToCollege"
        mViewModel.getDiagrams(target)
    }

    override fun onPause() {
        super.onPause()
        mViewModel.stopTimer()
    }
}
