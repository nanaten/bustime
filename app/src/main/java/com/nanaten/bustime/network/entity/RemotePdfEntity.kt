/*
 * Created by m.coder on 2020/2/12.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RemotePdfEntity(
    var calendar: String = "",
    @field:Json(name = "time_table") var timeTable: String = ""
)