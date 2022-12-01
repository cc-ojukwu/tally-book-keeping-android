package com.chrisojukwu.tallybookkeeping.di

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesStorageModule {

    val Context.dataStore by preferencesDataStore(
        name = DataStorePreferenceStorage.PREFS_NAME,
        produceMigrations = { context ->
            listOf(
                SharedPreferencesMigration(
                    context,
                    DataStorePreferenceStorage.PREFS_NAME
                )
            )
        }
    )

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage {
        return DataStorePreferenceStorage(context.dataStore)
    }
}