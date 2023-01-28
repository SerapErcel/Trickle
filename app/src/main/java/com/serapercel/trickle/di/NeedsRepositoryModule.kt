package com.serapercel.trickle.di

import com.serapercel.trickle.data.repository.NeedsRepositoryImpl
import com.serapercel.trickle.domain.repository.NeedsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NeedsRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideNeedsRepository(
        needsRepositoryImpl: NeedsRepositoryImpl
    ): NeedsRepository
}