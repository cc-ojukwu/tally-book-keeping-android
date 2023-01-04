package com.chrisojukwu.tallybookkeeping.di.module

import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.data.source.repository.RecordsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(recordsRepositoryImpl: RecordsRepositoryImpl): RecordsRepository
}