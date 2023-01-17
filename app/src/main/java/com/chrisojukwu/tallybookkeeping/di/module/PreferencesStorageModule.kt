package com.chrisojukwu.tallybookkeeping.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
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

    private val Context.dataStore by preferencesDataStore(
        name = DataStorePreferenceStorage.PREFS_NAME
    )

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage {
        return DataStorePreferenceStorage(context.dataStore)
    }
}