package dev.shreyaspatil.covid19notify.ui.state

import android.util.Log
import android.widget.Toast
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

                val result : StateDistrictDetails
                val response = when(it){
                    is State.Success -> {
                        val checkResponse = it.data
                        Log.d("SecondActivityViewModel", checkResponse.toString())
                        for (i in checkResponse){
                            if (i.state == stateName){
                                result = i
                                Log.d("SecondActivityViewModel", result.toString())
                                _stateCovidLiveData.postValue(State.success(result))
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