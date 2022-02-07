package codes.adityaa.temasyschat.di

import android.content.Context
import android.content.SharedPreferences
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
    fun providesSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("SuuzWorldSharedPref", Context.MODE_PRIVATE)
}
