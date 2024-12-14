package com.thesua7.map7.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.thesua7.map7.ui.screen.component.MapContainer
import com.thesua7.map7.ui.theme.Map7Theme
import com.thesua7.map7.util.PermissionAlert
import com.thesua7.map7.util.hasLocationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationViewModel: LocationViewModel by viewModels()
        enableEdgeToEdge()

        setContent {

            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) // for observing permission state

            val uiState by locationViewModel.locationScreenState.collectAsStateWithLifecycle()

            Map7Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    CheckPermissionContainer(permissionState, locationViewModel) // check permission status for location

                    with(uiState) {
                        when (this) {
                            LocationScreenState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                                }
                            }

                            LocationScreenState.RevokedPermissions -> {
                                AskForPermissionContainer()
                            }

                            is LocationScreenState.Success -> {
                                val currentLoc = LatLng(
                                    location?.latitude ?: 0.0, location?.longitude ?: 0.0
                                )
                                val cameraState = rememberCameraPositionState()
                                LiveContainer(currentLoc, cameraState)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LiveContainer(
        currentLoc: LatLng, cameraState: CameraPositionState, modifier: Modifier = Modifier
    ) {
        LaunchedEffect(key1 = currentLoc) {
            cameraState.centerOnLocation(currentLoc)
        }
        val currentPosition = LatLng(
            currentLoc.latitude, currentLoc.longitude
        )

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            TextField(  // text field for current lat & long
                value = "\nCurrent coordinates:\n Lat: ${currentPosition.latitude}\n Long: ${currentPosition.longitude}",
                onValueChange = { },
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledTextColor = MaterialTheme.colorScheme.onBackground
                ),
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            )

            MapContainer( // google map
                currentPosition = currentPosition, cameraState = cameraState
            )
        }


    }

    @Composable
    private fun AskForPermissionContainer() {  //Asking Permission
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Permissions are required in order to use this application")
            Button(
                onClick = {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }, enabled = !hasLocationPermission()
            ) {
                if (hasLocationPermission()) CircularProgressIndicator(
                    modifier = Modifier.size(16.dp), color = MaterialTheme.colorScheme.onBackground
                )
                else Text("Settings", fontSize = 24.sp)
            }
        }
    }




    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun CheckPermissionContainer(
        permissionState: MultiplePermissionsState, locationViewModel: LocationViewModel
    ) {
        LaunchedEffect(!hasLocationPermission()) {
            permissionState.launchMultiplePermissionRequest()
        }

        when {
            permissionState.allPermissionsGranted -> {
                LaunchedEffect(Unit) {
                    locationViewModel.handle(PermissionEvent.Granted)
                }
            }

            permissionState.shouldShowRationale -> {
                PermissionAlert(onDismiss = { }) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }

            !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                LaunchedEffect(Unit) {
                    locationViewModel.handle(PermissionEvent.Revoked)
                }
            }
        }
    }
}


private suspend fun CameraPositionState.centerOnLocation( // live movement
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location, 15f
    ), durationMs = 1500
)