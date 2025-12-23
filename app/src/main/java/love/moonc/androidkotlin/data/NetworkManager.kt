package love.moonc.androidkotlin.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    // 确保末尾有斜杠
    private const val BASE_URL = "https://m1.apifoxmock.com/m1/7603864-7342593-default/"

    // 回归极简：直接构建 Retrofit
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}