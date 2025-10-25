package br.com.front.data.session

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.front.data.model.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserDataStore(private val context: Context) {
    private object Keys {
        val ID = longPreferencesKey("id")
        val NOME = stringPreferencesKey("nome")
        val EMAIL = stringPreferencesKey("email")
        val TOKEN = stringPreferencesKey("token")
        val LAT = doublePreferencesKey("latitude")
        val LNG = doublePreferencesKey("longitude")
    }

    val userFlow: Flow<UserResponse?> = context.dataStore.data.map { p ->
        val id = p[Keys.ID] ?: return@map null
        UserResponse(
            id = id,
            email = p[Keys.EMAIL].orEmpty(),
            nome = p[Keys.NOME].orEmpty(),
            token = p[Keys.TOKEN],
            latitude = p[Keys.LAT],
            longitude = p[Keys.LNG]
        )
    }

    suspend fun save(user: UserResponse) {
        context.dataStore.edit { p ->
            p[Keys.ID] = user.id
            p[Keys.NOME] = user.nome
            p[Keys.EMAIL] = user.email
            if (user.token != null) p[Keys.TOKEN] = user.token else p.remove(Keys.TOKEN)
            if (user.latitude != null) p[Keys.LAT] = user.latitude else p.remove(Keys.LAT)
            if (user.longitude != null) p[Keys.LNG] = user.longitude else p.remove(Keys.LNG)
        }
    }

    suspend fun clear() { context.dataStore.edit { it.clear() } }

    suspend fun loadOnce(): UserResponse? = userFlow.firstOrNull()
}
