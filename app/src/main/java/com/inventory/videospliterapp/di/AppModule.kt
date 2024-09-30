package com.inventory.videospliterapp.di

import com.inventory.videospliterapp.mvvm.GalleryRepository
import android.content.Context
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
    fun provideGalleryRepository(@ApplicationContext context: Context): GalleryRepository {
        return GalleryRepository(context)
    }
}
