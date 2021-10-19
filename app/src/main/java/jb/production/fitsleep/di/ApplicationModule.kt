package jb.production.fitsleep.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jb.production.fitsleep.utils.GoogleFitUtils

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    fun getGoogleFitUtils(@ApplicationContext context: Context) = GoogleFitUtils(context)
}