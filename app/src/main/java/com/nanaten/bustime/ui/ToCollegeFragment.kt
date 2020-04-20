package com.nanaten.bustime.ui


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.DiagramAdapter
import com.nanaten.bustime.adapter.ItemClickListener
import com.nanaten.bustime.databinding.FragmentToCollegeBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.network.entity.NetworkResult
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import com.nanaten.bustime.widget.CustomLinearLayoutManager
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ToCollegeFragment : DaggerFragment(), ItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mViewModel: DiagramViewModel
    private var binding: FragmentToCollegeBinding by autoCleared()

    // タブのポジション設定
    // 本当は動的に取りたい
    private val tabPosition = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_college, container, false)
        mViewModel = ViewModelProvider(requireParentFragment()).get(DiagramViewModel::class.java)

        val mAdapter = DiagramAdapter(mViewModel, tabPosition)
        mAdapter.setOnItemClickListener(this)
        binding.apply {
            toolbar.setToolbar(
                getString(R.string.to_college),
                backVisibility = View.GONE,
                settingVisibility = View.VISIBLE,
                settingListener = {
                    findNavController().navigate(R.id.action_home_to_settings)
                }
            )
            toCollegeRv.layoutManager = CustomLinearLayoutManager(context)
            toCollegeRv.adapter = mAdapter
            (toCollegeRv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            swipeLayout.setOnRefreshListener {
                getDiagrams()
            }
        }

        mViewModel.calendar.observe(viewLifecycleOwner, Observer {
            mAdapter.updateCalendar(it)
        })

        mViewModel.diagrams.observe(viewLifecycleOwner, Observer {
            mAdapter.updateDiagram(it)
        })

        mViewModel.next.observe(viewLifecycleOwner, Observer {
            mAdapter.updateTime()
        })

        mViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (!it) binding.swipeLayout.isRefreshing = false
        })

        mViewModel.networkResult.observe(viewLifecycleOwner, "networkResult", Observer {
            if (it is NetworkResult.Error) {
                showToast(getString(R.string.network_error_message))
            }
        })
        return binding.root
    }

    private fun getDiagrams() {
        mViewModel.getDiagrams()
    }

    override fun onItemClick(index: Int, view: View) {
        when (view.tag) {
            "Calendar" -> {
                val url = mViewModel.pdfUrl.value?.calendar
                url?.let {
                    CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(view.context, Uri.parse(it))
                } ?: showToast(getString(R.string.network_error_message))
            }
            "TimeTable" -> {
                val url = mViewModel.pdfUrl.value?.timeTable
                url?.let {
                    CustomTabsIntent.Builder()
                        .build()
                        .launchUrl(view.context, Uri.parse(it))
                } ?: showToast(getString(R.string.network_error_message))
            }
            else -> return
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
