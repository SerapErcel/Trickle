package com.serapercel.trickle.di

import com.serapercel.trickle.data.source.local.LocalNeedsDataSource
import com.serapercel.trickle.data.source.local.LocalNeedsDataSourceImpl
import com.serapercel.trickle.data.source.remote.RemoteNeedDataSource
import com.serapercel.trickle.data.source.remote.RemoteNeedDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun provideLocalDataSource(
        localNeedsDataSourceImpl: LocalNeedsDataSourceImpl
    ): LocalNeedsDataSource

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(
        remoteDataSourceImpl: RemoteNeedDataSourceImpl
    ): RemoteNeedDataSource
}