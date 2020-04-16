/*
 * Created by m.coder on 2020/4/16.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager


/**
 * No Predictive Animations LinearLayoutManager
 */
class CustomLinearLayoutManager : LinearLayoutManager {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    constructor(
        context: Context?,
        orientation: Int
    ) : super(context, orientation, false) {
    }

    constructor(
        context: Context?
    ) : super(context) {
    }
}