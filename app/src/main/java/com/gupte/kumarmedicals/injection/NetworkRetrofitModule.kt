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
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkRetrofitModule {

    @[Provides Singleton]
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient
    ) : Retrofit.Builder {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .client(okHttpClient)
    }

//    @[Provides Singleton]
//    fun provideRetrofit(
//        retrofitBuilder: Retrofit.Builder
//    ): Retrofit {
//        return retrofitBuilder
//            .baseUrl(AppConstants.BASE_URL)
//            .build()
//    }
//
    @[Provides Singleton]
    fun provideCsvRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}