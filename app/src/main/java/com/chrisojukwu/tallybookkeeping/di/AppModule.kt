package com.chrisojukwu.tallybookkeeping.di

import android.app.Application
import android.content.Context
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
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}