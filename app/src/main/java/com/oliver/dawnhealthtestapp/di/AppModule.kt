package com.oliver.dawnhealthtestapp.di

import com.oliver.dawnhealthtestapp.data.remote.AppConfig.BASE_URL
import com.oliver.dawnhealthtestapp.data.remote.GithubRepositoriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor =
            HttpLoggingInterceptor { message -> Timber.d("HttpCall $message") }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun okHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubRepositoriesApi {
        return retrofit.create(GithubRepositoriesApi::class.java)
    }
}