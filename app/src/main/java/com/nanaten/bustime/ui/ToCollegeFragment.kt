package com.nanaten.bustime.ui


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.nanaten.bustime.Const
import com.nanaten.bustime.R
import com.nanaten.bustime.adapter.DiagramAdapter
import com.nanaten.bustime.adapter.ItemClickListener
import com.nanaten.bustime.databinding.FragmentToCollegeBinding
import com.nanaten.bustime.di.viewmodel.ViewModelFactory
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel
import com.nanaten.bustime.util.autoCleared
import com.nanaten.bustime.util.setToolbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class ToCollegeFragment : DaggerFragment(), ItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: DiagramViewModel by viewModels { viewModelFactory }
    private var binding: FragmentToCollegeBinding by autoCleared()
    // タブのポジション設定
    // 本当は動的に取りたい
    private val tabPosition = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_college, container, false)

        val mAdapter = DiagramAdapter(mViewModel, tabPosition)
        mAdapter.setOnItemClickListener(this)
        binding.apply {
            toolbar.setToolbar(
                getString(R.string.to_collage_label),
                backVisibility = View.GONE,
                settingVisibility = View.VISIBLE
            )
            toCollegeRv.layoutManager = LinearLayoutManager(context)
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
            getDiagrams()
        })

        mViewModel.diagrams.observe(viewLifecycleOwner, Observer {
            mAdapter.updateDiagram(it.filter { it.second >= mViewModel.nowSecond.value ?: 0L })
        })

        mViewModel.next.observe(viewLifecycleOwner, Observer {
            mAdapter.updateTime()
        })

        mViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (!it) binding.swipeLayout.isRefreshing = false
        })


        return binding.root
    }

    private fun getDiagrams() {
        val target = if (tabPosition == 0) Const.TO_STA else Const.TO_COL
        mViewModel.getDiagrams(target)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.startTimer()
        mViewModel.getCalendar()
        mViewModel.getPdf()
    }

    override fun onPause() {
        super.onPause()
        mViewModel.stopTimer()
    }

    override fun onItemClick(index: Int, view: View) {
        when (view.tag) {
            "Calendar" -> {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(view.context, Uri.parse(mViewModel.pdfUrl.value?.calendar))
            }
            "TimeTable" -> {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(view.context, Uri.parse(mViewModel.pdfUrl.value?.timeTable))
            }
            else -> return
        }
    }
}
