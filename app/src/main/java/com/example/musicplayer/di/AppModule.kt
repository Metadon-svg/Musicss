package com.example.musicplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.data.YouTubeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRepo(): YouTubeRepo = YouTubeRepo()

    @Provides
    @Singleton
    fun providePlayer(@ApplicationContext context: Context): ExoPlayer = ExoPlayer.Builder(context).build()
}