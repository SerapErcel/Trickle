package com.serapercel.trickle.data.source.remote

import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User

interface RemoteNeedDataSource {
    suspend fun getNeeds(user: User): List<Need>
    suspend fun addNeed(need: Need, user: User): Boolean
}