package com.ryall.observablecomparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //Observers will be notified when the screen rotates, meaning if we rotate our device it will be fired again
    //Livedata is lifecycle aware

    //always good to use in your ui and viewmodel if your app is lifecycle aware
    private val _liveData = MutableLiveData("Hello World")
    val liveData : LiveData<String> = _liveData

    //holds states just as livedata and its basically a flow so it has flow benefits
    //needs to be in a coroutine
    //with state flow you can map, filter the results easily
    //much easier testable than livedata because they use coroutines
    //this is a hotflow -> it will keep emitting values even if there are no collectors
    //cold flow means -> it will not emit anything if theres not a collector
    //keeps a value and wont emit the same value
    //it will emit the value if the activity is recreated
    private val _stateFlow = MutableStateFlow("Hello World")
    val stateFlow = _stateFlow.asStateFlow()

    //does not keep value in itself
    //its also a hot flow like state flow, they will send out events even if there are not collectors
    //used for more one time events
    //wont emit if the activity is recreated
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(){
        _liveData.value = "LiveData"
    }

    fun triggerStateFlow(){
        viewModelScope.launch {
            _stateFlow.emit("State Flow")
        }
    }

    fun triggerSharedFlow(){
        //viewmodelscope is used in viewmodels
        viewModelScope.launch {
            //no need for a flow builder, we can just send an event in this flow
            _sharedFlow.emit("Shared Flow")
        }
    }

    //used to emit multiple values over a period of time
    //normal flow does not contain a state
    //it will reset back to hello word once the screen rotates
    //it doesnt just fire off again once the activity is recreated, its more like a one time thing
    //if the button is clicked it will fire the flow again and when its done then its done
    fun triggerFlow(): Flow<String>{
        return flow {
            //this is a coroutine scope
            //can execute suspend functions
            repeat(5){
                emit("Item : $it")
                delay(1000L)
            }
        }
    }
}