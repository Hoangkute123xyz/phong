package vn.hexagon.vietnhat.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import vn.hexagon.vietnhat.ui.foneplace.FonePlaceActivity

@Module
abstract class FonePlaceActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun contributesFonePlaceActivity(): FonePlaceActivity
}