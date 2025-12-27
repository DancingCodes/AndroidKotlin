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

    // ✅ 必须增加这个：告诉 Hilt 如何实例化 UserPreferences
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        userPreferences: UserPreferences
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()

            // 1. 同步获取 Token
            val token = runBlocking {
                userPreferences.token.firstOrNull()
            }

            // 2. 注入 Header (保持你的逻辑)
            val finalRequest = if (!token.isNullOrBlank()) {
                request.newBuilder()
                    .header("Authorization", "Bearer $token") // 注意 Bearer 后面通常有空格
                    .build()
            } else {
                request
            }

            val response = chain.proceed(finalRequest)

            // 3. 统一处理响应码
            when (response.code) {
                401 -> {
                    showToast(context, "登录已失效，请重新登录")
                    runBlocking {
                        userPreferences.clear()
                    }
                }
                500 -> {
                    // 如果 message 为空，给个默认提示
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