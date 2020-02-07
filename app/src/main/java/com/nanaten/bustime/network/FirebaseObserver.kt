/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.network

import com.google.firebase.firestore.FirebaseFirestore
import com.nanaten.bustime.network.entity.CalendarEntity
import com.nanaten.bustime.network.entity.DiagramEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseObserver {
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        const val calendar = "calendar"
        const val second = "second"
    }

    suspend fun getTodayCalendar(): Flow<CalendarEntity> {
        return flow {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            emit(suspendCoroutine { cont ->
                firestore.collection(calendar).document(today)
                    .get()
                    .addOnSuccessListener {
                        val calendar = it.toObject(CalendarEntity::class.java)
                        cont.resume(calendar ?: CalendarEntity())
                    }
                    .addOnFailureListener {
                        cont.resumeWithException(it)
                    }
            })
        }
    }

    suspend fun getDiagrams(diagramName: String, now: Long): Flow<List<DiagramEntity>> {
        return flow {
            emit(suspendCoroutine { cont ->
                firestore.collection(diagramName).whereGreaterThanOrEqualTo(second, now)
                    .get()
                    .addOnSuccessListener { querySnapShot ->
                        val diagrams =
                            querySnapShot.documents.mapNotNull { it.toObject(DiagramEntity::class.java) }
                        cont.resume(diagrams)
                    }
                    .addOnFailureListener {
                        cont.resumeWithException(it)
                    }
            })
        }

    }

}
