/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network

import com.google.firebase.firestore.FirebaseFirestore
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.Diagram
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*


class FirebaseObserver {
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        const val calendar = "calendar"
        const val date = "date"
        const val second = "second"
    }

    suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        @UseExperimental(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        return channelFlow {
            val listenerRegistration = firestore.collection(calendar).whereEqualTo(date, today)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        channel.close(firebaseFirestoreException)
                    } else {
                        val calendar =
                            querySnapshot?.documents?.map { it.toObject(CalendarEntity::class.java) }
                        channel.offer(calendar?.firstOrNull() ?: CalendarEntity())
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getDiagrams(): Flow<List<Diagram>> {
        return flow { listOf<Diagram>() }
    }
}