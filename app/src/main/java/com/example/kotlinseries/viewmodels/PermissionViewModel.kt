package com.example.kotlinseries.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinseries.MainApplication
import com.example.kotlinseries.util.PermissionManager

class PermissionViewModel(val permissions: PermissionManager) : ViewModel() {

    val uiState = permissions.state

    fun onPermissionChange(requestedPermissions: Map<String, Boolean>) {
        permissions.onPermissionChange(requestedPermissions)
    }

    fun createSettingsIntent(): Intent {
        return permissions.createSettingsIntent()
    }
}

//class PermissionViewModelFactory : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        val app =
//            extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication
//        return PermissionViewModel(app.permissions) as T
//    }
//}