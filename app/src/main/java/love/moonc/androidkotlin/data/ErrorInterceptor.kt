package love.moonc.androidkotlin.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject

class ErrorInterceptor(private val context: Context) : Interceptor {
    // ErrorInterceptor.kt 里的修改建议
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        try {
            val responseBodyCopy = response.peekBody(Long.MAX_VALUE)
            val bodyString = responseBodyCopy.string()

            if (bodyString.isNotEmpty()) {
                val jsonObject = JSONObject(bodyString)
                val businessCode = jsonObject.optInt("code", 200)

                if (businessCode == 500) {
                    val msg = jsonObject.optString("msg", "服务器错误")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }
}