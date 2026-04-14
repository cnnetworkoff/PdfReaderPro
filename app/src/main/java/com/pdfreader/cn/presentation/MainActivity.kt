package com.pdfreader.cn.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.pdfreader.cn.domain.repository.PreferencesRepository
import com.pdfreader.cn.presentation.navigation.Home
import com.pdfreader.cn.presentation.navigation.Onboarding
import com.pdfreader.cn.presentation.navigation.PdfReaderNavGraph
import com.pdfreader.cn.presentation.navigation.Reader
import com.pdfreader.cn.presentation.theme.PdfReaderProTheme
import com.pdfreader.cn.presentation.theme.ThemeMode
import com.pdfreader.cn.util.FileOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val preferencesRepository: PreferencesRepository by inject()

    private var isReady by mutableStateOf(false)
    private var startDestination: Any by mutableStateOf(Home)
    private var hasCompletedOnboarding by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep splash visible while loading preferences
        splashScreen.setKeepOnScreenCondition { !isReady }

        // Clean up old cached PDFs in background
        lifecycleScope.launch(Dispatchers.IO) {
            FileOperations.cleanupOldCachedPdfs(this@MainActivity)
        }

        // Determine start destination (check for incoming intent first)
        lifecycleScope.launch {
            val prefs = preferencesRepository.preferences.first()
            hasCompletedOnboarding = prefs.hasCompletedOnboarding
            // Check if opened via intent with PDF
            val intentPdfPath = handleIncomingIntent(intent)

            startDestination = when {
                // If we have an intent with PDF, go directly to reader
                intentPdfPath != null -> Reader(path = intentPdfPath, fromIntent = true)
                // Normal app launch flow
                hasCompletedOnboarding -> Home
                else -> Onboarding
            }
            isReady = true
        }

        setContent {
            // Observe theme preference
            val preferences by preferencesRepository.preferences.collectAsState(
                initial = com.pdfreader.cn.domain.model.AppPreferences()
            )
            val themeMode = when (preferences.themeMode) {
                com.pdfreader.cn.domain.model.ThemeMode.LIGHT -> ThemeMode.LIGHT
                com.pdfreader.cn.domain.model.ThemeMode.DARK -> ThemeMode.DARK
                com.pdfreader.cn.domain.model.ThemeMode.SYSTEM -> ThemeMode.SYSTEM
            }

            PdfReaderProTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isReady) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            PdfReaderNavGraph(startDestination = startDestination)
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles incoming intents (ACTION_VIEW, ACTION_SEND) with PDF files.
     * Returns the local file path if successful, null otherwise.
     */
    private suspend fun handleIncomingIntent(intent: Intent?): String? {
        if (intent == null) return null

        val uri: Uri? = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }
            else -> null
        }

        if (uri == null) return null

        return withContext(Dispatchers.IO) {
            try {
                val path = FileOperations.resolveUriToPath(this@MainActivity, uri)
                if (path == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Failed to open PDF", Toast.LENGTH_SHORT).show()
                    }
                }
                path
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                null
            }
        }
    }
}
