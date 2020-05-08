/*
 * Created by m.coder on 2020/2/3.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.nanaten.bustime.R
import com.nanaten.bustime.databinding.ListItemDiagramBinding
import com.nanaten.bustime.databinding.ListItemEmptyBinding
import com.nanaten.bustime.databinding.ListItemPdfBinding
import com.nanaten.bustime.databinding.ListItemRecentBusBinding
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.ui.viewmodel.DiagramViewModel

class DiagramAdapter(private val viewModel: DiagramViewModel, private val tabPosition: Int) :
    BaseRecyclerViewAdapter() {
    private var list: List<Diagram> = listOf()
    private var allList: List<Diagram> = listOf()
    private var calendar: Calendar? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.list_item_empty -> EmptyItemViewHolder(parent)
            R.layout.list_item_diagram -> DiagramItemViewHolder(parent)
            R.layout.list_item_pdf -> PdfItemViewHolder(parent)
            else -> RecentItemViewHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return if (calendar?.isSuspend == true || isOperationEnd()) 1 else list.size + 2
    }

    private fun isOperationEnd(): Boolean {
        return allList.isNotEmpty() && list.isEmpty()
    }

    override fun getItemViewType(position: Int): Int {
        return if (calendar?.isSuspend == true || isOperationEnd()) R.layout.list_item_empty else
            when (position) {
                0 -> R.layout.list_item_diagram
                1 -> R.layout.list_item_pdf
                else -> R.layout.list_item_recent_bus
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyItemViewHolder -> {
                holder.bind(calendar)
                holder.binding.calendarButton.setOnClickListener {
                    it.tag = "Calendar"
                    getItemClickListener().onItemClick(position, it)
                }
                holder.binding.timeTableButton.setOnClickListener {
                    it.tag = "TimeTable"
                    getItemClickListener().onItemClick(position, it)
                }
            }
            is DiagramItemViewHolder -> {
                holder.bind(calendar)
                holder.setViewModel(viewModel)
                holder.setIcon(tabPosition)
            }
            is PdfItemViewHolder -> {
                holder.binding.recentHeader.visibility =
                    if (list.isEmpty()) View.GONE else View.VISIBLE
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
                val index = holder.adapterPosition - 2
                holder.bind(list[index])
                holder.binding.order.text = "${index + 1}"
                holder.itemView.setOnClickListener {
                    it.tag = list[index]
                    getItemClickListener().onItemClick(index, it)
                }
            }
            else -> return
        }
    }

    fun updateDiagram(list: List<Diagram>?) {
        list ?: return
        this.allList = list
        this.list = list.filter { it.second >= viewModel.nowSecond.value ?: 0L }
        notifyItemChanged(1)
        this.list.forEachIndexed { i, _ ->
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
                binding.startLabel.text = binding.root.context.getString(R.string.start_college)
                binding.arrivalLabel.text = binding.root.context.getString(R.string.arrival_station)
            } else {
                binding.startPlace.setImageResource(R.drawable.ic_train)
                binding.arrivalPlace.setImageResource(R.drawable.ic_school)
                binding.startLabel.text = binding.root.context.getString(R.string.start_station)
                binding.arrivalLabel.text = binding.root.context.getString(R.string.arrival_college)
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

    class EmptyItemViewHolder(
        parent: ViewGroup, val binding: ListItemEmptyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_empty,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Calendar?) {
            binding.calendar = item
        }
    }

    class RecentItemViewHolder(
        parent: ViewGroup, val binding: ListItemRecentBusBinding = DataBindingUtil.inflate(
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