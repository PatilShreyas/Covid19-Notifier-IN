package dev.shreyaspatil.covid19notify.ui.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.shreyaspatil.covid19notify.model.StateDistrictDetails
import dev.shreyaspatil.covid19notify.repository.CovidIndiaRepository
import dev.shreyaspatil.covid19notify.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateViewModel(private val covidIndiaRepository: CovidIndiaRepository) : ViewModel() {

    private val _stateCovidLiveData = MutableLiveData<State<StateDistrictDetails>>()

    val stateCovidLiveData: LiveData<State<StateDistrictDetails>> = _stateCovidLiveData

    fun getDistrictData(stateName: String) {
        viewModelScope.launch {
            covidIndiaRepository.getStateDistrictData().collect {

                val mStateDistrictDetails: StateDistrictDetails
                when (it) {
                    is State.Success -> {
                        val statesData = it.data
                        for (stateData in statesData) {
                            if (stateData.state == stateName) {
                                mStateDistrictDetails = stateData
                                _stateCovidLiveData.postValue(State.success(mStateDistrictDetails))
                                break
                            }
                        }

                    }
                    is State.Loading -> {

                    }
                    is State.Error -> {

                    }
                }
            }
        }

    }
}