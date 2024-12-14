package com.thesua7.map7.service

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow


interface LocationService {
    fun requestLocationUpdates(): Flow<LatLng?>

}