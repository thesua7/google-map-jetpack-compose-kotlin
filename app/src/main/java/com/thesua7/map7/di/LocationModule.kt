package com.thesua7.map7.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.thesua7.map7.service.LocationService
import com.thesua7.map7.service.LocationServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): LocationService = LocationServiceImpl(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )
}
