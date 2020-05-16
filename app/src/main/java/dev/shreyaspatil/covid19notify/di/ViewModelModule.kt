package dev.shreyaspatil.covid19notify.di

import dev.shreyaspatil.covid19notify.ui.details.StateDetailsViewModel
import dev.shreyaspatil.covid19notify.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { StateDetailsViewModel(get()) }
}