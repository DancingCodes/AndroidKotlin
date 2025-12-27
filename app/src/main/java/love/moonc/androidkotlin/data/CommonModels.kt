package love.moonc.androidkotlin.data

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

data class UploadResponse(
    val url: String
)