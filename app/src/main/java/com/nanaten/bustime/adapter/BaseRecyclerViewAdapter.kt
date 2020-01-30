/*
 * Created by m.coder on 2020/1/30.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener

    fun setOnItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun getItemClickListener(): ItemClickListener {
        return itemClickListener
    }
}