package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanaten.bustime.R
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
    private val viewModel: DiagramViewModel by viewModels { viewModelFactory }
    private var binding: FragmentToStationBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_station, container, false)
        binding.toolbar.setToolbar(
            getString(R.string.to_station_label),
            backVisibility = View.GONE,
            settingVisibility = View.VISIBLE
        )
        binding.toStationRv.layoutManager = LinearLayoutManager(context)
        return binding.root
    }


}
