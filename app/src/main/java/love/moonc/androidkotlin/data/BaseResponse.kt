package love.moonc.androidkotlin.data

data class BaseResponse<T>(
    val code: String,
    val msg: String,
    val data: T
)