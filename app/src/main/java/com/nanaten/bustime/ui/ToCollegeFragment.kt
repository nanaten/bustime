package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nanaten.bustime.R
import com.nanaten.bustime.databinding.FragmentToCollegeBinding
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import dagger.android.support.DaggerFragment


class ToCollegeFragment : DaggerFragment() {

    private var binding: FragmentToCollegeBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_college, container, false)
        binding.toolbar.setToolbar(
            getString(R.string.to_collage_label),
            backVisibility = View.GONE,
            settingVisibility = View.VISIBLE
        )
        return binding.root
    }

}
