package love.moonc.androidkotlin.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import love.moonc.androidkotlin.data.UserPreferences
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.firstOrNull

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()

            val token = runBlocking {
                userPreferences.token.firstOrNull()
            }

            // 2. 注入 Header
            val finalRequest = if (!token.isNullOrBlank()) {
                request.newBuilder().header("Authorization", "Bearer $token").build()
            } else {
                request
            }

            val response = chain.proceed(finalRequest)

            // 3. 统一处理响应码 (按照你之前的逻辑)
            when (response.code) {
                401 -> {
                    showToast(context, "登录已失效，请重新登录")
                    runBlocking {
                        userPreferences.clear()
                    }
                }
                500 -> {
                    showToast(context, response.message)
                }
            }
            response
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.110.143:3006/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}