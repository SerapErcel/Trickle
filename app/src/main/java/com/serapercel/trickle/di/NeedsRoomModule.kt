package com.serapercel.trickle.di

import android.content.Context
import androidx.room.Room
import com.serapercel.trickle.data.source.database.NeedDatabase
import com.serapercel.trickle.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NeedsRoomModule{

    @Provides
    @Singleton
    fun provideNeedsDatabase(
        context: Context
    )= Room.databaseBuilder(
        context,
        NeedDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideNeedsDao(database: NeedDatabase)= database.getNeedsDao()

}