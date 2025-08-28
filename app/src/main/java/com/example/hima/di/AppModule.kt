package com.example.hima.di

import android.content.Context
import com.example.hima.data.AssetLoader
import com.example.hima.media.AudioPlayer
import com.example.hima.media.TTSManager
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
    fun provideAssetLoader(@ApplicationContext context: Context): AssetLoader {
        return AssetLoader(context)
    }

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context): AudioPlayer {
        return AudioPlayer(context)
    }

    @Provides
    @Singleton
    fun provideTTSManager(@ApplicationContext context: Context): TTSManager {
        return TTSManager(context)
    }
}
