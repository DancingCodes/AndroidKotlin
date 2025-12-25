package love.moonc.androidkotlin.data

import android.content.Context
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkManager {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    lateinit var api: ApiService

    fun init(context: Context) {
        val userPreferences = UserPreferences(context)
        val errorInterceptor = ErrorInterceptor(context)

        val client = OkHttpClient.Builder()
            // --- 核心修改：添加 Token 拦截器 ---
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                val token: String? = kotlinx.coroutines.runBlocking {
                    userPreferences.token.firstOrNull()
                }

                val requestBuilder = originalRequest.newBuilder()
                if (!token.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            // ----------------------------------
            .addInterceptor(errorInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }
}