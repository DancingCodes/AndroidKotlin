package love.moonc.androidkotlin.data

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    lateinit var api: ApiService

    fun init(context: Context) {
        val errorInterceptor = ErrorInterceptor(context)

        val client = OkHttpClient.Builder()
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