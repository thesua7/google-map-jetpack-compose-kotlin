package com.thesua7.map7.ui.screen.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapContainer(currentPosition: LatLng, cameraState: CameraPositionState) {
    // Remember the MarkerState to avoid unnecessary recompositions
    val markerState = remember { MarkerState(position = currentPosition) }

    GoogleMap(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 10.dp,topEnd = 10.dp)),
        cameraPositionState = cameraState,
        properties = MapProperties(
            isMyLocationEnabled = true, mapType = MapType.HYBRID, isTrafficEnabled = true
        )
    ) {
        Marker(
            state = markerState, title = "Current Location", draggable = true
        )
    }
}
