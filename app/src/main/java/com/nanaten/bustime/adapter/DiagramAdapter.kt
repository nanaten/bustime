/*
 * Created by m.coder on 2020/2/3.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nanaten.bustime.R
import com.nanaten.bustime.databinding.ListItemDiagramBinding
import com.nanaten.bustime.databinding.ListItemPdfBinding
import com.nanaten.bustime.databinding.ListItemRecentBusBinding
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel

class DiagramAdapter(private val viewModel: DiagramViewModel, private val tabPosition: Int) :
    BaseRecyclerViewAdapter() {
    private var list: List<Diagram> = listOf()
    private var calendar: Calendar? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.list_item_diagram -> DiagramItemViewHolder(parent)
            R.layout.list_item_pdf -> PdfItemViewHolder(parent)
            else -> RecentItemViewHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.list_item_diagram
            1 -> R.layout.list_item_pdf
            else -> R.layout.list_item_recent_bus
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DiagramItemViewHolder -> {
                holder.bind(calendar)
                holder.setViewModel(viewModel)
                holder.setIcon(tabPosition)
            }
            is PdfItemViewHolder -> {
                holder.binding.calendarLayout.setOnClickListener {
                    it.tag = "Calendar"
                    getItemClickListener().onItemClick(position, it)
                }
                holder.binding.timeTableLayout.setOnClickListener {
                    it.tag = "TimeTable"
                    getItemClickListener().onItemClick(position, it)
                }
            }
            is RecentItemViewHolder -> {
                holder.bind(list[position])
            }
            else -> return
        }
    }

    fun updateDiagram(list: List<Diagram>) {
        this.list = list
        list.forEachIndexed { i, _ ->
            notifyItemChanged(i + 2)
        }
    }

    fun updateCalendar(item: Calendar) {
        this.calendar = item
        notifyItemChanged(0)
    }

    fun updateTime() {
        notifyItemChanged(0)
    }

    class DiagramItemViewHolder(
        parent: ViewGroup, private val binding: ListItemDiagramBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_diagram,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Calendar?) {
            item ?: return
            binding.calendar = item
        }

        fun setViewModel(viewModel: DiagramViewModel) {
            binding.viewModel = viewModel
        }

        fun setIcon(pos: Int) {
            if (pos == 0) {
                binding.startPlace.setImageResource(R.drawable.ic_school)
                binding.arrivalPlace.setImageResource(R.drawable.ic_train)
                binding.startLabel.text = "大学発"
                binding.arrivalLabel.text = "浄水駅着"
            } else {
                binding.startPlace.setImageResource(R.drawable.ic_train)
                binding.arrivalPlace.setImageResource(R.drawable.ic_school)
                binding.startLabel.text = "浄水駅発"
                binding.arrivalLabel.text = "大学着"
            }
        }
    }

    class PdfItemViewHolder(
        parent: ViewGroup, val binding: ListItemPdfBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_pdf,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
    }

    class RecentItemViewHolder(
        parent: ViewGroup, private val binding: ListItemRecentBusBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_recent_bus,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diagram) {
            binding.item = item
        }
    }
}