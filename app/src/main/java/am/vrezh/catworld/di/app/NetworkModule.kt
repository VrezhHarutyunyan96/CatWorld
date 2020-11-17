package am.vrezh.catworld.di.app

import am.vrezh.catworld.api.CatsApi

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @AppScope
    @Provides
    internal fun provideApiService(
        client: OkHttpClient,
        gson: GsonConverterFactory,
        rxAdapter: RxJava3CallAdapterFactory
    ): CatsApi {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(gson)
            .addCallAdapterFactory(rxAdapter)
            .build()

        return retrofit.create(CatsApi::class.java)
    }

    @AppScope
    @Provides
    internal fun provideHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient()
            .newBuilder()
            .addInterceptor(logger)
        return builder.build()
    }

    @AppScope
    @Provides
    internal fun provideInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @AppScope
    @Provides
    internal fun provideRxAdapter(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Provides
    internal fun provideGsonClient(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"
    }

}

