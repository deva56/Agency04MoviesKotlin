package com.example.agency04movieskotlin.Network

import com.example.agency04movieskotlin.Constants.Companion.movie_api_base_url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitBuilder {

    @Provides
    @Singleton
    fun getRetrofit(): RetrofitInterface {
        return Retrofit.Builder()
            .baseUrl(movie_api_base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitInterface::class.java)
    }
}