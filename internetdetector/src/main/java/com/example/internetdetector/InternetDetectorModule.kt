package com.example.internetdetector

import android.content.Context
import com.crazylegend.internetdetector.InternetDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object InternetDetectorModule {

    @Provides
    @Singleton
    fun provideInternetDetector(@ApplicationContext context: Context) = InternetDetector(context)
}