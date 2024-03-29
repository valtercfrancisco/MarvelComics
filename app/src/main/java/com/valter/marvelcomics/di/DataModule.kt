package com.valter.marvelcomics.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.valter.marvelcomics.BuildConfig
import com.valter.marvelcomics.data.database.MarvelDatabase
import com.valter.marvelcomics.data.network.*
import com.valter.marvelcomics.data.repository.MarvelRepository
import com.valter.marvelcomics.data.repository.MarvelRepositoryImpl
import com.valter.marvelcomics.dispatchers.AppDispatchersContainer
import com.valter.marvelcomics.dispatchers.DispatchersContainer
import com.valter.marvelcomics.ui.details.base.ComicDetailsViewModel
import com.valter.marvelcomics.ui.list.ComicListNavigation
import com.valter.marvelcomics.ui.list.ComicListNavigationImpl
import com.valter.marvelcomics.ui.list.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@FlowPreview
object DataModule {
    val module = module {
        single { provideMoshi() }
        single { provideLoggingInterceptor() }
        single<RequestInterceptor> { provideRequestInterceptor() }
        single<ConnectivityInterceptor> { provideConnectivityInterceptor(get()) }
        single { provideDefaultOkHttpClient(get(), get(), get()) }
        single { provideRetrofit(get(), get()) }
        single { provideDatabase(androidApplication()) }
        single { provideComicDao(get()) }
        single { provideApiService(get()) }
        single<DispatchersContainer> { AppDispatchersContainer() }
        single<MarvelRepository> { MarvelRepositoryImpl(get(), get(), get()) }
        viewModel { MainViewModel(get(), get()) }
        factory<ComicListNavigation> { (fragment: Fragment) -> ComicListNavigationImpl(fragment.findNavController()) }
        viewModel { (comicId: String) -> ComicDetailsViewModel(get(), get(), comicId) }
    }
}

fun provideMoshi(): Moshi = Moshi.Builder().build()

fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

fun provideConnectivityInterceptor(context: Context) = ConnectivityInterceptorImpl(context)

fun provideRequestInterceptor() = RequestInterceptorImpl()

fun provideDefaultOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                               requestInterceptor: RequestInterceptor,
                               connectivityInterceptor: ConnectivityInterceptor
) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(requestInterceptor)
        .addInterceptor(connectivityInterceptor)
        .build()

fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        MarvelDatabase::class.java,
        "marvel.db"
).fallbackToDestructiveMigration()
        .build()

fun provideComicDao(database: MarvelDatabase) = database.comicDao()

fun provideApiService(retrofit: Retrofit): MarvelService = retrofit.create(MarvelService::class.java)