package com.example.kotlinseries

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.example.kotlinseries.data.PhotoSaverRepository
import com.example.kotlinseries.util.PermissionManager

class MainApplication : Application(), CameraXConfig.Provider {
//    lateinit var photoSaver: PhotoSaverRepository
//    lateinit var permissions: PermissionManager
//
//    override fun onCreate() {
//        super.onCreate()
//
//        photoSaver = PhotoSaverRepository(this, this.contentResolver)
//        //permissions = PermissionManager(this)
//    }

    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setMinimumLoggingLevel(Log.ERROR).build()
    }
}