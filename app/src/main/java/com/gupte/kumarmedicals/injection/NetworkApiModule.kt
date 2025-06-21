// Hilt module providing Retrofit API service dependencies
package com.gupte.kumarmedicals.injection

import com.gupte.kumarmedicals.api.IMedicalApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkApiModule {

    @[Provides Singleton]
    fun provideMedicalApiService(
        csvRetrofit: Retrofit
    ): IMedicalApiService {
        return csvRetrofit.create(IMedicalApiService::class.java)
    }
}