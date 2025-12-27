package love.moonc.androidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import love.moonc.androidkotlin.ui.MainScreen
import love.moonc.androidkotlin.ui.theme.AndroidKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NetworkManager.init(this)
        setContent {
            AndroidKotlinTheme {
                MainScreen()
            }
        }
    }
}