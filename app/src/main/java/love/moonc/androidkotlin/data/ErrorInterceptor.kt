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

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val userPreferences = UserPreferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 1. 💡 必须包裹整个 proceed 过程，捕获 SocketTimeout, UnknownHost 等异常
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            // 这里处理：超时、断网、服务器宕机
            val errorMsg = when (e) {
                is java.net.SocketTimeoutException -> "连接服务器超时，请检查网络"
                is java.net.ConnectException -> "无法连接到服务器，请确认后端已开启"
                is java.net.UnknownHostException -> "找不到服务器地址"
                else -> "网络请求失败: ${e.localizedMessage}"
            }
            showToast(errorMsg)
            // 💡 必须抛出一个 IOException，否则 OkHttp 会认为逻辑未完成
            throw java.io.IOException(errorMsg)
        }

        // 2. 只有请求成功返回了，才进入业务状态码判断
        try {
            val responseBodyCopy = response.peekBody(Long.MAX_VALUE)
            val bodyString = responseBodyCopy.string()

            if (bodyString.isNotEmpty()) {
                val jsonObject = JSONObject(bodyString)
                val businessCode = jsonObject.optInt("code", 200)

                if (businessCode == 401) {
                    showToast("登录已过期，请重新登录")
                    scope.launch {
                        userPreferences.clear()
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
            // 解析 JSON 失败不应该弄崩 App，所以这里只打印日志
        }

        return response
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}