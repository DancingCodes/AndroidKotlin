package love.moonc.androidkotlin.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject


class ErrorInterceptor(private val context: Context) : Interceptor {

    // 1. 定义协程作用域 (用于在拦截器里执行 suspend 函数)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // 2. 初始化 userPreferences
    private val userPreferences = UserPreferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        try {
            val responseBodyCopy = response.peekBody(Long.MAX_VALUE)
            val bodyString = responseBodyCopy.string()

            if (bodyString.isNotEmpty()) {
                val jsonObject = JSONObject(bodyString)
                // 业务状态码
                val businessCode = jsonObject.optInt("code", 200)

                if ( businessCode == 401) {
                    showToast("登录已过期，请重新登录")
                    scope.launch {
                        userPreferences.clear()
                        // 还可以考虑在这里把 NetworkManager.currentToken 清空
                        NetworkManager.currentToken = ""
                    }
                }

                if (businessCode == 500) {
                    val msg = jsonObject.optString("msg", "服务器内部错误")
                    showToast(msg)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }
    // 辅助函数：在主线程弹出 Toast
    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}