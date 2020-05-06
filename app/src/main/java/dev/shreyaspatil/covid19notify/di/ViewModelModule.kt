package dev.shreyaspatil.covid19notify.di

import dev.shreyaspatil.covid19notify.ui.main.MainViewModel
import dev.shreyaspatil.covid19notify.ui.state.StateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { StateViewModel(get()) }
}