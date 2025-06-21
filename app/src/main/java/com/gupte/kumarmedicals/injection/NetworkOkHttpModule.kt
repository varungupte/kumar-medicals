// Hilt module providing OkHttp client and builder dependencies
package com.gupte.kumarmedicals.injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkOkHttpModule {
    @[Provides Singleton]
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    @[Provides Singleton]
    fun provideOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder
    ): OkHttpClient {
        return okHttpClientBuilder.apply {
        }.build()
    }
}