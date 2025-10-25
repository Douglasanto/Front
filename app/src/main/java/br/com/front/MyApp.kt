package br.com.front

import android.app.Application
import br.com.front.data.session.UserDataStore
import br.com.front.data.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val store = UserDataStore(this)
        CoroutineScope(Dispatchers.IO).launch {
            store.loadOnce()?.let { UserSession.setUser(it) }
        }
    }
}
