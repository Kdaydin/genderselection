package com.khomeapps.gender.di

import android.content.Context
import com.khomeapps.gender.utils.SavedDataManager
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
    fun provideSavedDataManager(@ApplicationContext context: Context): SavedDataManager {
        return SavedDataManager(context)
    }

}