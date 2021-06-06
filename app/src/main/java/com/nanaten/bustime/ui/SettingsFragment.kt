/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nanaten.bustime.Const
import com.nanaten.bustime.R
import com.nanaten.bustime.databinding.FragmentSettingsBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.SettingsViewModel
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class SettingsFragment : DaggerFragment() {

    private var binding: FragmentSettingsBinding by autoCleared()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: SettingsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.apply {
            toolbar.setToolbar(
                settingVisibility = View.GONE,
                backVisibility = View.VISIBLE,
                backListener = { findNavController().popBackStack() },
                settingListener = null
            )
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            binding.darkModeSwitch.setOnClickListener {
                Toast.makeText(it.context, getString(R.string.theme_is_changed), Toast.LENGTH_SHORT)
                    .show()
                mViewModel.setDarkModeStatus()
            }
            firstViewLayout.setOnClickListener {
                changeFirstView()
            }
            thisAppLayout.setOnClickListener {
                showWebView(Const.ABOUT_APP_URL)
            }
            precautionLayout.setOnClickListener {
                showWebView(Const.PRE_CAUTIONS_URL)
            }
            privacyPolicyLayout.setOnClickListener {
                showWebView(Const.PRIVACY_POLICY_URL)
            }
        }

        mViewModel.getDarkModeIsOn()
        mViewModel.getFirstView()
        return binding.root
    }

    private fun changeFirstView() {
        context?.let {
            val pageName =
                if (mViewModel.getFirstView() == 0) getString(R.string.to_station) else getString(
                    R.string.to_college
                )
            val changePageName =
                if (mViewModel.getFirstView() == 0) getString(R.string.to_college) else getString(
                    R.string.to_station
                )
            AlertDialog.Builder(it).apply {
                val message =
                    context.getString(R.string.first_view_setting_message, pageName, changePageName)
                setTitle(getString(R.string.first_view_title))
                setMessage(message)
                setPositiveButton(R.string.switching) { _, _ ->
                    val page = if (mViewModel.getFirstView() == 0) 1 else 0
                    mViewModel.setFirstView(page)
                    AlertDialog.Builder(context).apply {
                        setTitle(R.string.first_view_title)
                        setMessage(getString(R.string.first_view_setting_is_done, changePageName))
                        setPositiveButton(R.string.ok, null)
                    }.show()
                }
                setNegativeButton(R.string.cancel, null)
                show()
            }
        }

    }

    private fun showWebView(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(requireContext(), Uri.parse(url))
    }
}
