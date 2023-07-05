package com.example.kotlinseries.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionManager(private val context: Context) {
    companion object {
        val REQUIRED_PERMISSIONS_PRE_T = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val REQUIRED_PERMISSIONS_POST_T = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    data class State(
        val hasStorageAccess: Boolean,
        val hasCameraAccess: Boolean,
        val hasLocationAccess: Boolean
    ) {
        val hasAllAccess: Boolean
            get() = hasStorageAccess && hasCameraAccess && hasLocationAccess
    }

    private val _state = MutableStateFlow(
        State(
            hasStorageAccess = hasAccess(Manifest.permission.READ_EXTERNAL_STORAGE) || hasAccess(Manifest.permission.READ_MEDIA_IMAGES),
            hasCameraAccess = hasAccess(Manifest.permission.CAMERA),
            hasLocationAccess = hasAccess(listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)),
        )
    )

    val state = _state.asStateFlow()
    val hasAllPermissions: Boolean
        get() = _state.value.hasAllAccess

    private fun hasAccess(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasAccess(permissions: List<String>): Boolean {
        return permissions.all(::hasAccess)
    }

    fun onPermissionChange(permissions: Map<String, Boolean>) {
        val hasLocationAccess = hasAccess(Manifest.permission.ACCESS_FINE_LOCATION) && hasAccess(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val hasStorageAccess = hasAccess(Manifest.permission.READ_MEDIA_IMAGES) || hasAccess(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        _state.value = State(
            hasStorageAccess = hasStorageAccess,
            hasCameraAccess = permissions[Manifest.permission.CAMERA] ?: _state.value.hasCameraAccess,
            hasLocationAccess = hasLocationAccess
        )
    }

    suspend fun checkPermissions() {
        val newState = State(
            hasStorageAccess = hasAccess(Manifest.permission.READ_EXTERNAL_STORAGE) || hasAccess(Manifest.permission.READ_MEDIA_IMAGES),
            hasCameraAccess = hasAccess(Manifest.permission.CAMERA),
            hasLocationAccess = hasAccess(Manifest.permission.ACCESS_FINE_LOCATION) && hasAccess(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        _state.emit(newState)
    }

    fun createSettingsIntent(): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.fromParts("package", context.packageName, null)
        }

        return intent
    }
}