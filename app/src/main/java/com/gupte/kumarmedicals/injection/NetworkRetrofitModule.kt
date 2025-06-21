// Hilt module providing Retrofit instance with Kotlin Serialization converter
package com.gupte.kumarmedicalst.injection

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.gupte.kumarmedicals.util.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkRetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://script.google.com/")
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }
}