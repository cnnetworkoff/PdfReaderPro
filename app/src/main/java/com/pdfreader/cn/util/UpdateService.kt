package com.pdfreader.cn.util

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UpdateServiceState(
    val isDownloading: Boolean = false,
    val isDownloaded: Boolean = false,
    val promptNonce: Int = 0
)

class UpdateService(
    private val activity: ComponentActivity
) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)

    private val _state = MutableStateFlow(UpdateServiceState())
    val state: StateFlow<UpdateServiceState> = _state.asStateFlow()

    private val updateLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        checkForUpdate()
    }

    private val installListener = InstallStateUpdatedListener { installState ->
        when (installState.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                _state.value = _state.value.copy(
                    isDownloading = true,
                    isDownloaded = false
                )
            }

            InstallStatus.DOWNLOADED -> {
                publishReadyToInstall()
            }

            InstallStatus.INSTALLED,
            InstallStatus.CANCELED,
            InstallStatus.FAILED,
            InstallStatus.UNKNOWN -> {
                _state.value = UpdateServiceState()
            }

            else -> Unit
        }
    }

    init {
        appUpdateManager.registerListener(installListener)
    }

    fun checkForUpdate() {
        try {
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { info ->
                    when {
                        info.installStatus() == InstallStatus.DOWNLOADED -> {
                            publishReadyToInstall()
                        }

                        info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                            info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                            startUpdate(info)
                        }

                        else -> Unit
                    }
                }
                .addOnFailureListener { error ->
                    Log.w(TAG, "Flexible update check failed", error)
                }
        } catch (error: Exception) {
            Log.w(TAG, "Unable to query in-app update state", error)
        }
    }

    fun startUpdate(appUpdateInfo: AppUpdateInfo) {
        try {
            val updateIntent = appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateLauncher,
                com.google.android.play.core.appupdate.AppUpdateOptions
                    .newBuilder(AppUpdateType.FLEXIBLE)
                    .build()
            )

            if (!updateIntent) {
                Log.d(TAG, "Flexible update flow was not started")
            }
        } catch (error: Exception) {
            Log.w(TAG, "Unable to start flexible update flow", error)
        }
    }

    fun completeUpdate() {
        try {
            appUpdateManager.completeUpdate()
                .addOnFailureListener { error ->
                    Log.w(TAG, "Unable to complete flexible update", error)
                }
        } catch (error: Exception) {
            Log.w(TAG, "Flexible update completion failed", error)
        }
    }

    fun handleResume() {
        checkForUpdate()
    }

    fun dispose() {
        appUpdateManager.unregisterListener(installListener)
    }

    private fun publishReadyToInstall() {
        _state.value = _state.value.copy(
            isDownloading = false,
            isDownloaded = true,
            promptNonce = _state.value.promptNonce + 1
        )
    }

    companion object {
        private const val TAG = "UpdateService"
    }
}
