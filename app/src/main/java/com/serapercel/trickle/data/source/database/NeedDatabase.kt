package com.serapercel.trickle.data.source.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.util.DATABASE_NAME


@Database(entities = arrayOf(Need::class), version = 1, exportSchema = false)
abstract class NeedDatabase : RoomDatabase(){
    abstract fun getNeedsDao(): NeedDao

    /*companion object {

        @Volatile
        private var INSTANCE: NeedDatabase? = null
        fun getDatabase(context: Context): NeedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NeedDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }*/

}