package com.chrisojukwu.tallybookkeeping.di.module

import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsLocalDataSource
import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsLocalDataSourceImpl
import com.chrisojukwu.tallybookkeeping.data.source.remote.RecordsRemoteDataSource
import com.chrisojukwu.tallybookkeeping.data.source.remote.RecordsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalDataSource(recordsLocalDataSourceImpl: RecordsLocalDataSourceImpl): RecordsLocalDataSource

    @Binds
    abstract fun bindRemoteDataSource(recordsRemoteDataSourceImpl: RecordsRemoteDataSourceImpl): RecordsRemoteDataSource
}