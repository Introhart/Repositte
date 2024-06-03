package the.hart.repositte.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import the.hart.repositte.data.api.GitHubService
import the.hart.repositte.data.api.retrofit.AuthInterceptor

@Module
@InstallIn(SingletonComponent::class)
class  NetworkModule {

    private val baseUrl = "https://api.github.com"

    @Provides
    fun provideRetrofit(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GitHubService =
        retrofit.create(GitHubService::class.java)
}