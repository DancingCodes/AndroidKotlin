package love.moonc.androidkotlin.data
import retrofit2.http.GET

interface ApiService {
    @GET("getMessage")
    suspend fun getMessage(): BaseResponse<String>
}