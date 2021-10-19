package jb.production.fitsleep.di

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import jb.production.fitsleep.screens.adapters.VisualizationTabAdapter

@Module
@InstallIn(FragmentComponent::class)
object VisualizationModule {

    @Provides
    fun getVisualizationTabAdapter(fragment: Fragment) =
        VisualizationTabAdapter(fragment)

}