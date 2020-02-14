/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nanaten.bustime.R
import com.nanaten.bustime.databinding.FragmentSettingsBinding
import com.nanaten.bustime.ui.viewmodel.SettingsViewModel
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import dagger.android.support.DaggerFragment


class SettingsFragment : DaggerFragment() {

    private var binding: FragmentSettingsBinding by autoCleared()
    private val mViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.apply {
            toolbar.setToolbar(
                title = getString(R.string.setting),
                settingVisibility = View.GONE,
                backVisibility = View.VISIBLE,
                backListener = { findNavController().popBackStack() }
            )
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            binding.darkModeSwitch.setOnClickListener {
                Toast.makeText(it.context, getString(R.string.theme_is_changed), Toast.LENGTH_SHORT)
                    .show()
                mViewModel.setIsDarkMode(it.context)
            }
            firstViewLayout.setOnClickListener {
                changeFirstView()
            }
        }

        mViewModel.getIsDarkMode(requireContext())
        mViewModel.getFirstView(requireContext())
        return binding.root
    }

    private fun changeFirstView() {
        context?.let {
            val pageName =
                if (mViewModel.getFirstView(requireContext()) == 0) getString(R.string.to_station) else getString(
                    R.string.to_college
                )
            val changePageName =
                if (mViewModel.getFirstView(requireContext()) == 0) getString(R.string.to_college) else getString(
                    R.string.to_station
                )
            AlertDialog.Builder(it).apply {
                val message = "現在「${pageName}」が最初に表示されます。「${changePageName}」に変更しますか？"
                setTitle(getString(R.string.first_view_title))
                setMessage(message)
                setPositiveButton(R.string.switching) { _, _ ->
                    val page = if (mViewModel.getFirstView(requireContext()) == 0) 1 else 0
                    mViewModel.setFirstView(requireContext(), page)
                }
                setNegativeButton(R.string.no_switching, null)
                show()
            }
        }

    }
}
