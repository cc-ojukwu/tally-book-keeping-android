package com.chrisojukwu.tallybookkeeping.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chrisojukwu.tallybookkeeping.di.ViewModelKey
import com.chrisojukwu.tallybookkeeping.ui.LaunchViewModel
import com.chrisojukwu.tallybookkeeping.ui.account.SignInViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
abstract class ViewModelModule {

//    @Binds
//    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindAccountViewModel(viewModel: SignInViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(LaunchViewModel::class)
    abstract fun bindLaunchViewModel(viewModel: LaunchViewModel): ViewModel


}