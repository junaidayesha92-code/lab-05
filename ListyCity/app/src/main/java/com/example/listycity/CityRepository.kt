package com.example.listycity
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf<City>()

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            _cities.clear()

            snapshot?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null) {
                    _cities.add(city)
                }
            }
        }
    }

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }
    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
            .addOnFailureListener { e ->
                Log.e("CityRepository", "Failed to delete ${city.name}", e)
            }
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        if (oldCity.name != updatedCity.name) {
            citiesRef.document(oldCity.name).delete()
        }
        citiesRef.document(updatedCity.name).set(updatedCity)
    }
}