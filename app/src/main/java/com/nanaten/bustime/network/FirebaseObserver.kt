/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network

import com.google.firebase.firestore.FirebaseFirestore
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
            val listenerRegistration = firestore.collection(calendar).document(today)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        channel.close(firebaseFirestoreException)
                    } else {
                        val calendar = querySnapshot?.toObject(CalendarEntity::class.java)
                        channel.offer(calendar ?: CalendarEntity())
                    }
                }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<DiagramEntity>> {
        @UseExperimental(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        return channelFlow {
            val listenerRegistration =
                firestore.collection(diagramName).whereGreaterThanOrEqualTo(second, now)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        if (firebaseFirestoreException != null) {
                            channel.close(firebaseFirestoreException)
                        } else {
                            val diagrams =
                                querySnapshot?.documents?.mapNotNull { it.toObject(DiagramEntity::class.java) }
                            channel.offer(diagrams ?: emptyList())
                        }
                    }
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }
}