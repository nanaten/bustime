/*
 * Created by m.coder on 2020/2/12.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

data class RemotePdf(
    val calendar: String,
    val timeTable: String
) {
    companion object RemotePdfFactory : Translator<RemotePdfEntity, RemotePdf> {
        override fun convertFrom(entity: RemotePdfEntity): RemotePdf {
            return RemotePdf(
                entity.calendar,
                entity.timeTable
            )
        }
    }
}