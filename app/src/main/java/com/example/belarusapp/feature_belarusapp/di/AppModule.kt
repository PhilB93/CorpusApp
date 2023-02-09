package com.example.belarusapp.feature_belarusapp.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.belarusapp.R
import com.example.belarusapp.feature_belarusapp.data.remote.ApiService
import com.example.belarusapp.feature_belarusapp.data.repository.BoardGamesInfoRepositoryImpl
import com.example.belarusapp.feature_belarusapp.domain.repository.BoardGamesInfoRepository
import com.example.belarusapp.feature_belarusapp.domain.use_case.SearchBoardGamesUseCase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://krokapp.by/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideSearchBoardGamesCase(repository: BoardGamesInfoRepository):
            SearchBoardGamesUseCase = SearchBoardGamesUseCase(repository)

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        api: ApiService,
        @ApplicationContext context: Context
    ): BoardGamesInfoRepository {
        return BoardGamesInfoRepositoryImpl(api, context)
    }

    @Singleton
    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(context.cacheDir, cacheSize)
        return OkHttpClient
            .Builder()
            .cache(cache)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory
        {
          val gson = GsonBuilder().setPrettyPrinting().
          disableHtmlEscaping().create()
           return GsonConverterFactory.create(gson)
        }


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
