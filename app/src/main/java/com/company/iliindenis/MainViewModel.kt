package com.company.iliindenis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    val liveDataCurrent=MutableLiveData<DayWeather>()


}