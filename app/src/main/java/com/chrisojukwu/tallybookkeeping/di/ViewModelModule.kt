package com.chrisojukwu.tallybookkeeping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chrisojukwu.tallybookkeeping.ViewModelFactory
import com.chrisojukwu.tallybookkeeping.ui.LaunchViewModel
import com.chrisojukwu.tallybookkeeping.ui.account.AccountViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(viewModel: LaunchViewModel): ViewModel
}