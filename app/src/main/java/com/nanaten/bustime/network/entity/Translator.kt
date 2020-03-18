/*
 * Created by m.coder on 2020/3/18.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network.entity

interface Translator<Entity, T> {
    fun convertFrom(entity: Entity): T
}