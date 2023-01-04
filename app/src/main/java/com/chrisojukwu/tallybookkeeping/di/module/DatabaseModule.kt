package com.chrisojukwu.tallybookkeeping.di.module

import android.content.Context
import androidx.room.Room
import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsDatabase
import com.chrisojukwu.tallybookkeeping.data.source.local.dao.RecordDao
import com.chrisojukwu.tallybookkeeping.utils.typeconverters.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): RecordsDatabase {
        return Room.databaseBuilder(
            context,
            RecordsDatabase::class.java, "RecordsDB.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(database: RecordsDatabase): RecordDao {
        return database.recordDao
    }
}