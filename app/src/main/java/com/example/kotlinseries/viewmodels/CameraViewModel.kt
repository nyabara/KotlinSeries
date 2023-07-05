package com.example.kotlinseries.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinseries.MainApplication
import com.example.kotlinseries.data.PhotoSaverRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraViewModel(
    application: Application,
    private val photoSaver: PhotoSaverRepository
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication<MainApplication>()
    private val cameraExecutor = Executors.newSingleThreadExecutor()
     val savedFile = photoSaver.generatePhotoCacheFile()


    data class CameraState(
        val isTakingPicture: Boolean = false,
        val imageFile: File? = null,
        val captureError: ImageCaptureException? = null,
        val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
        val rotation: Int = 0,

        val cameraSelector: CameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build(),
        val metrics: Rect ? = null,
        val screenAspectRatio : Int = 0,
        val imageCapture: ImageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

    )



    var _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    suspend fun getCameraProvider(): ProcessCameraProvider {
        return suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(context).apply {
                addListener({ continuation.resume(get()) }, cameraExecutor)
            }
        }
    }

    fun setLensFacing(lesFacing: Int){
        viewModelScope.launch {
            _cameraState = MutableStateFlow(_cameraState.value.copy(lensFacing = lesFacing))
        }
    }

    fun setRotation(rotation: Int){
        viewModelScope.launch {
            _cameraState = MutableStateFlow(_cameraState.value.copy(rotation = rotation))
        }
    }

    fun setAspectRation(ratio:Int){
        viewModelScope.launch {
            _cameraState = MutableStateFlow(_cameraState.value.copy(screenAspectRatio = ratio))
        }

    }

    fun setMetrics(metrics: Rect?){
        _cameraState = MutableStateFlow(_cameraState.value.copy(metrics = metrics))
    }

    fun setCacheCapturedPhoto(savedFile: File){
        viewModelScope.launch {
            _cameraState = MutableStateFlow(_cameraState.value.copy(isTakingPicture = true))
            photoSaver.cacheCapturedPhoto(savedFile)
            _cameraState =
                MutableStateFlow(_cameraState.value.copy(imageFile = savedFile))
        }

    }

    fun takePicture() {
        viewModelScope.launch {
            _cameraState = MutableStateFlow(_cameraState.value.copy(isTakingPicture = true))

            val savedFile = photoSaver.generatePhotoCacheFile()

            _cameraState.value.imageCapture.takePicture(
                
                ImageCapture.OutputFileOptions.Builder(savedFile).build(),
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        Log.i("TakePicture", "capture succeeded")

                        photoSaver.cacheCapturedPhoto(savedFile)
                        _cameraState =
                            MutableStateFlow(_cameraState.value.copy(imageFile = savedFile))
                    }

                    override fun onError(ex: ImageCaptureException) {
                        Log.e("TakePicture", "capture failed", ex)
                    }
                }
            )
        }

    }


}

//class CameraViewModelFactory : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        val app =
//            extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication
//        return CameraViewModel(app, app.photoSaver) as T
//    }
//}