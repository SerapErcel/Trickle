package com.serapercel.trickle.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.serapercel.trickle.data.entity.Need

@Database(entities = arrayOf(Need::class), version = 1, exportSchema = false)
abstract class NeedDatabase : RoomDatabase(){

    abstract fun getNeedsDao(): NeedDao

}