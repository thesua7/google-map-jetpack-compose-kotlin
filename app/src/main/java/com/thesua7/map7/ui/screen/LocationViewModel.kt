    package com.thesua7.map7.ui.screen

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.thesua7.map7.domain.GetLocationUseCase
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @HiltViewModel
    class LocationViewModel @Inject constructor(
        private val getLocationUseCase: GetLocationUseCase
    ) : ViewModel() {

        private val _locationScreenState: MutableStateFlow<LocationScreenState> = MutableStateFlow(
            LocationScreenState.Loading
        )
        val locationScreenState = _locationScreenState.asStateFlow()

        /* This function is responsible for updating the LocationScreenState based
           on the event coming from the view */
        @RequiresApi(Build.VERSION_CODES.S)
        fun handle(event: PermissionEvent) {
            when (event) {
                PermissionEvent.Granted -> {
                    viewModelScope.launch {
                        getLocationUseCase.invoke().collect { location ->
                            _locationScreenState.value = LocationScreenState.Success(location)
                        }
                    }
                }

                PermissionEvent.Revoked -> {
                    _locationScreenState.value = LocationScreenState.RevokedPermissions
                }
            }
        }
    }

