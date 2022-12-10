package com.nanaten.bustime.ui


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.DiagramAdapter
import com.nanaten.bustime.adapter.ItemClickListener
import com.nanaten.bustime.databinding.FragmentToCollegeBinding
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.entity.NetworkResult
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.widget.CustomLinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ToCollegeFragment : Fragment(), ItemClickListener {

    private val mViewModel: DiagramViewModel by viewModels({ requireParentFragment() })
    private var _binding: FragmentToCollegeBinding? = null
    private val binding: FragmentToCollegeBinding get() = requireNotNull(_binding)

    // タブのポジション設定
    // 本当は動的に取りたい
    private val tabPosition = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_college, container, false)

        val mAdapter = DiagramAdapter(mViewModel, tabPosition)
        mAdapter.setOnItemClickListener(this)
        binding.apply {
            toCollegeRv.layoutManager = CustomLinearLayoutManager(context)
            toCollegeRv.adapter = mAdapter
            (toCollegeRv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            swipeLayout.setOnRefreshListener {
                getDiagramsClearCache()
            }
        }

        mViewModel.calendar.observe(viewLifecycleOwner) {
            mAdapter.updateCalendar(it)
        }

        mViewModel.diagrams.observe(viewLifecycleOwner) {
            mAdapter.updateDiagram(it)
        }

        mViewModel.next.observe(viewLifecycleOwner) {
            mAdapter.updateTime()
        }


        mViewModel.networkResult.observe(viewLifecycleOwner, "networkResult") {
            if (it is NetworkResult.Error) {
                showToast(getString(R.string.network_error_message))
            }
        }
        return binding.root
    }

    private fun getDiagramsClearCache() {
        mViewModel.getDiagrams(false)
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
            is Diagram -> {
                val diagram = view.tag as Diagram
                mViewModel.showRemindDialog(requireContext(), diagram)
            }
            else -> return
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
