package es.kix2902.santoral.data

import Model
import java.util.*

object DataRepository {

    private val networkRepository: NetworkRepository = NetworkRepository

    fun getDay(calendar: Calendar, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        networkRepository.getDay(calendar, onResult, onError)
    }

    fun getName(name:String, onResult: (List<Model.ApiResponse>) -> Unit, onError: (Int) -> Unit) {
        networkRepository.getName(name, onResult, onError)
    }
}