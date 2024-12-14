package com.thesua7.map7.ui.screen
import com.google.android.gms.maps.model.LatLng


sealed interface LocationScreenState {
    data object Loading : LocationScreenState
    data class Success(val location: LatLng?) : LocationScreenState
    data object RevokedPermissions : LocationScreenState
}