package love.moonc.androidkotlin.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object NetworkManager {
    private const val BASE_URL = "http://192.168.110.143:3006/"

    private lateinit var apiService: ApiService

    fun init(context: Context) {
        if (::apiService.isInitialized) return
        apiService = createApi(context.applicationContext)
    }

    val api: ApiService get() = apiService

    private fun createApi(context: Context): ApiService {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()

            val token = runBlocking {
                UserPreferences(context).token.firstOrNull()
            }

            val finalRequest = if (!token.isNullOrBlank()) {
                request.newBuilder().header("Authorization", "Bearer $token").build()
            } else {
                request
            }

            val response = chain.proceed(finalRequest)

            // 统一处理响应码
            when (response.code) {
                401 -> {
                    showToast(context, "登录已失效，请重新登录")
                    runBlocking {
                        UserPreferences(context).clear()
                    }
                }
                500 -> showToast(context, response.message)
            }
            response
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}