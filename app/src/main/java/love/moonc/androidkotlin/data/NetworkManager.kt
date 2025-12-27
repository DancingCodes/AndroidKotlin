package love.moonc.androidkotlin.data

import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "http://192.168.110.143:3006/"
    lateinit var api: ApiService

    // 定义一个变量存 Token，避免每次拦截器都去读磁盘
    @Volatile
    var currentToken: String? = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun init(context: Context) {
        val userPreferences = UserPreferences(context)

        // 1. 异步监听 Token 变化（只要 DataStore 更新，这里会自动同步）
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            userPreferences.token.collect {
                currentToken = it
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                val token = runBlocking {
                    UserPreferences(context).token.firstOrNull()
                }
                if (!token.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(ErrorInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)
    }
}