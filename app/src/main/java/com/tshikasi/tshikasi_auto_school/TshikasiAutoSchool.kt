package com.tshikasi.tshikasi_auto_school

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleObserver
import com.tshikasi.tshikasi_auto_school.di.DataSourceModule
import com.tshikasi.tshikasi_auto_school.di.RepositoryModule
import com.tshikasi.tshikasi_auto_school.di.UseCaseModule
import com.tshikasi.tshikasi_auto_school.di.ViewModelModule
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit


const val CERTIFICATE_APP ="91a60eacfa7644939d378cce3469981e"
const val AGORA_APP_ID = "6a20c770f681408e96e94a0afcdcf4ff"
const val AGORA_TOKEN ="007eJxTYODVf9UXVv78b7zNpza1iwW1X96bsjAsfu6189yFPQtOBLxRYDBLNDJINjc3SDOzMDQxsEi1NEu1NEk0SExLTklOM0lLM5biymgIZGT40b+QgREKQXwOhpLijMzsxOJMBgYAgKEjZA=="
const val SUPABASE_URL  = "https://yucjmzzontuplfsyipro.supabase.co"
const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl1Y2ptenpvbnR1cGxmc3lpcHJvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTU0MTE0NjQsImV4cCI6MjA3MDk4NzQ2NH0.h350ThWy5618xb5ZhYg3-3i0vUwZ7ywsa_1ltGQ7eg8" // ← SUBSTITUA AQUI
const val PIXEL_API_KEY = "05cW7VOufRFku0ZCdszODT0We56CE2S2lTqT7Tsp1pMXYJogNR396u6C"
const val PIXABAY_API_KEY = "53656120-378e22ab61face2898ed92f52"
val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "LocalStore")

@HiltAndroidApp
class TshikasiAutoSchool: Application(), LifecycleObserver {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
        var currentActivity: Activity? = null
        lateinit var supabase: SupabaseClient
    }

    private var currentName: String? = null

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TshikasiAutoSchool)
            modules(RepositoryModule, DataSourceModule, ViewModelModule, UseCaseModule)
        }
      //  Firebase.initialize(this)

        try {
            supabase = createSupabaseClient(
                supabaseUrl = SUPABASE_URL,
                supabaseKey = SUPABASE_KEY
            ) {
                install(Postgrest) {
                    defaultSchema = "db_eaonde"
                }
                install(Storage)
                install(Functions)
                install(Realtime)

                // Usar OkHttp engine (suporta WebSocket)
                httpEngine = OkHttp.create {
                    config {
                        connectTimeout(30, TimeUnit.SECONDS)
                        readTimeout(120, TimeUnit.SECONDS)
                        writeTimeout(120, TimeUnit.SECONDS)
                    }
                }
            }
            Log.d("Supabase", "✅ Supabase inicializado com OkHttp Engine (WebSocket support)!")
            Log.d("Supabase", "⏱️ Timeouts: Connect=30s, Read/Write=120s")
        } catch (e: Exception) {
            Log.e("Supabase", "❌ Erro: ${e.message}", e)
        }

        createNotificationChannel()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity === activity) {
                    currentActivity = null
                }
            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notifications da FCM",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Estas notificações vai ser recebidas"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}