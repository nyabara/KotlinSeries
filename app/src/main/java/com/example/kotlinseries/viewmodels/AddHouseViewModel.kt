package com.example.kotlinseries.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.kotlinseries.MainApplication
import com.example.kotlinseries.data.MediaRepository
import com.example.kotlinseries.data.PhotoSaverRepository
import com.example.kotlinseries.database.HouseEntry
import com.example.kotlinseries.database.KotlinSeriesDatabase
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHouseViewModel(application: Application,
                        private val photoSaver: PhotoSaverRepository
) : AndroidViewModel(application) {
    // region ViewModel setup
    private val context: Context
        get() = getApplication()

    private val mediaRepository = MediaRepository(context)
    private val db = KotlinSeriesDatabase.getDatabase(context)
    // endregion

    // region UI state
    data class UiState1(
        val hasLocationAccess: Boolean,
        val hasCameraAccess: Boolean,
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
        val date: Long,
        val electricity_type: String? = null,
        val has_watchman: Boolean = false,
        val has_water: Boolean = false,
        val house_type: String? = null,
        val place: String? = null,
        val own_compound: Boolean = false,
        val savedPhotos: List<File> = emptyList(),
        val localPickerPhotos: List<Uri> = emptyList()
    )

    private var _uiState = MutableStateFlow(UiState1(
        hasLocationAccess = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION),
        hasCameraAccess = hasPermission(Manifest.permission.CAMERA),
        date = getTodayDateInMillis(),
        savedPhotos = photoSaver.getPhotos()
    ))
    val uiState: StateFlow<UiState1> = _uiState.asStateFlow()

    fun isValid(): Boolean {
        return uiState.value.house_type != null && !photoSaver.isEmpty() && !uiState.value.isSaving
    }

    private fun getTodayDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun getIsoDate(timeInMillis: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(timeInMillis)
    }

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                _uiState = MutableStateFlow(_uiState.value.copy(hasLocationAccess = isGranted))
            }
            Manifest.permission.CAMERA -> {
                _uiState = MutableStateFlow(_uiState.value.copy(hasCameraAccess = isGranted))
            }
            else -> {
                Log.e("Permission change", "Unexpected permission: $permission")
            }
        }
    }

    fun onDateChange(dateInMillis: Long) {
        _uiState = MutableStateFlow(_uiState.value.copy(date = dateInMillis))
    }

    // endregion

    // region Location management
    @SuppressLint("MissingPermission")
    fun fetchLocation() {
        try {

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location ?: return@addOnSuccessListener

                val geocoder = Geocoder(context, Locale.getDefault())

                if (Build.VERSION.SDK_INT >= 33)
                {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                        val address = addresses.firstOrNull()
                        val place = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                        ?: address?.countryName
                        _uiState = MutableStateFlow(_uiState.value.copy(place = place))
                    }
                }
                else {
                    val address =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                            ?: return@addOnSuccessListener
                    val place =
                        address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
                        ?: return@addOnSuccessListener

                    _uiState = MutableStateFlow(_uiState.value.copy(place = place))
                }
            }
        }catch (exception:Exception){

        }

    }
    // endregion

    fun loadLocalPickerPictures() {
        viewModelScope.launch {
            val localPickerPhotos = mediaRepository.fetchImages().map { it.uri }.toList()
            _uiState = MutableStateFlow(_uiState.value.copy(localPickerPhotos = localPickerPhotos))
        }
    }

    fun onLocalPhotoPickerSelect(photo: Uri) {
        viewModelScope.launch {
            photoSaver.cacheFromUri(photo)
            refreshSavedPhotos()
        }
    }

    fun onPhotoPickerSelect(photos: List<Uri>) {
        viewModelScope.launch {
            photoSaver.cacheFromUris(photos)
            refreshSavedPhotos()
        }
    }

    // region Photo management

    fun canAddPhoto() = photoSaver.canAddPhoto()

    fun refreshSavedPhotos() {
        _uiState = MutableStateFlow(_uiState.value.copy(savedPhotos = photoSaver.getPhotos()))
    }

    fun onPhotoRemoved(photo: File) {
        viewModelScope.launch {
            photoSaver.removeFile(photo)
            refreshSavedPhotos()
        }
    }

    fun createLog(electricity_type: String,
                  has_watchman: Boolean,
                  has_water: Boolean,
                  house_type: String,
                  own_compound: Boolean,) {
        if (!isValid()) {
            return
        }

        _uiState = MutableStateFlow(_uiState.value.copy(isSaving = true))

        viewModelScope.launch {
            val photos = photoSaver.savePhotos()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = uiState.value.date
            Log.e("date is ", uiState.value.date.toString())

            val house = HouseEntry(
                date = getIsoDate(uiState.value.date),
                electricity_type = electricity_type,
                has_watchman = has_watchman,
                has_water = has_water,
                house_type = house_type,
                own_compound = own_compound,
                place = uiState.value.place!!,
                photo1 = photos[0].name,
                photo2 = photos.getOrNull(1)?.name,
                photo3 = photos.getOrNull(2)?.name,
                photo4 = photos.getOrNull(3)?.name,
                photo5 = photos.getOrNull(4)?.name
            )

            db.appDao.insertHouse(house)
            _uiState = MutableStateFlow(_uiState.value.copy(isSaved = true))
        }
    }
    // endregion
}

//class AddHouseViewModelFactory : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
//        val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication
//        return AddHouseViewModel(app, app.photoSaver) as T
//    }
//}