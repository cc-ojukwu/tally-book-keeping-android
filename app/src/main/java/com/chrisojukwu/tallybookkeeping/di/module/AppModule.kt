package com.chrisojukwu.tallybookkeeping.di.module

import android.app.Application
import android.content.Context
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.utils.Constants.BASE_URL
import com.chrisojukwu.tallybookkeeping.utils.typeconverters.BigDecimalAdapter
import com.chrisojukwu.tallybookkeeping.utils.typeconverters.OffsetDateTimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(BigDecimalAdapter())
        .add(OffsetDateTimeAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun providedMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi).asLenient()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(converterFactory: MoshiConverterFactory): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(BASE_URL)

        return retrofitBuilder.build()
    }

    @Provides
    @Singleton
    fun provideIncomeListUseCase(repository: RecordsRepository): GetIncomeListUseCase {
        return GetIncomeListUseCase(
            repository = repository
        )
    }

    @Provides
    @Singleton
    fun provideExpenseListUseCase(repository: RecordsRepository): GetExpenseListUseCase {
        return GetExpenseListUseCase(
            repository = repository
        )
    }
}

