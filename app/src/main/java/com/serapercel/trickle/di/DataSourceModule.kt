package com.serapercel.trickle.di

import com.serapercel.trickle.data.source.local.LocalDataSource
import com.serapercel.trickle.data.source.local.LocalDataSourceImpl
import com.serapercel.trickle.data.source.remote.RemoteDataSource
import com.serapercel.trickle.data.source.remote.RemoteDataSourceImpl
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
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource
}