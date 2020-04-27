package dev.shreyaspatil.covid19notify.ui.main

import androidx.lifecycle.*
import dev.shreyaspatil.covid19notify.model.StateResponse
import dev.shreyaspatil.covid19notify.repository.CovidIndiaRepository
import dev.shreyaspatil.covid19notify.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainViewModel(private val repository: CovidIndiaRepository) : ViewModel() {

    private val _covidLiveData = MutableLiveData<State<StateResponse>>()

    val covidLiveData: LiveData<State<StateResponse>> = _covidLiveData

    fun getData() {
        viewModelScope.launch {
            repository.getData().collect {
                _covidLiveData.value = it
            }
        }
    }
}