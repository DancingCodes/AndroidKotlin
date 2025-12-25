package love.moonc.androidkotlin.data

import android.content.Context
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "http://10.0.2.2:8088/"
    lateinit var api: ApiService

    // 定义一个变量存 Token，避免每次拦截器都去读磁盘
    @Volatile
    var currentToken: String? = ""

    @OptIn(DelicateCoroutinesApi::class)
    fun init(context: Context) {
        val userPreferences = UserPreferences(context)

        // 1. 异步监听 Token 变化（只要 DataStore 更新，这里会自动同步）
        // 这比 runBlocking 安全得多
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            userPreferences.token.collect {
                currentToken = it
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                if (!currentToken.isNullOrBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $currentToken")
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