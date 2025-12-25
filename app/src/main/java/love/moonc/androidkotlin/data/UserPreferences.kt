package love.moonc.androidkotlin.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    private val gson = Gson()

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val USER_JSON = stringPreferencesKey("user_json")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] ?: "" }

    val user: Flow<User?> = context.dataStore.data.map { prefs ->
        prefs[USER_JSON]?.let { gson.fromJson(it, User::class.java) }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN] = token
        }
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[USER_JSON] = gson.toJson(user)
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}