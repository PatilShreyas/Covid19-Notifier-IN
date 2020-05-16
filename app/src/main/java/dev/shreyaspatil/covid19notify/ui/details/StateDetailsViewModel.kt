package dev.shreyaspatil.covid19notify.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shreyaspatil.covid19notify.model.StateDetailsResponse
import dev.shreyaspatil.covid19notify.repository.CovidIndiaRepository
import dev.shreyaspatil.covid19notify.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDetailsViewModel(private val covidIndiaRepository: CovidIndiaRepository) : ViewModel() {

    private val _stateCovidLiveData = MutableLiveData<State<StateDetailsResponse>>()

    val stateCovidLiveDataDetails: LiveData<State<StateDetailsResponse>> = _stateCovidLiveData

    fun getDistrictData(state: String) {
        viewModelScope.launch {
            covidIndiaRepository.getStateDetailsData(state).collect {
                _stateCovidLiveData.value = it
            }
        }
    }

}
