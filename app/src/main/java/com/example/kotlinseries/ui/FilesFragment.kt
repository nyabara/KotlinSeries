package com.example.kotlinseries.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.example.cameraxbasic.utils.simulateClick
import com.example.kotlinseries.R
import com.example.kotlinseries.databinding.FragmentFilesBinding
import com.example.kotlinseries.viewmodels.AddHouseViewModel



class FilesFragment : Fragment() {

    //val viewModel: AddHouseViewModel by viewModels {AddHouseViewModelFactory()}
    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!
    private var kilometer: Int? = null
    private var houseType: String? = null
    private var hasWater: Boolean = false
    private var hasWatchman: Boolean = false
    private var ownCompound: Boolean = false
    private var electricityType: String? = null

    private val imageLocationReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.getIntExtra(KEY_EVENT_EXTRA_IL, KeyEvent.KEYCODE_UNKNOWN)){
                //When the volume down button is pressed, simulate a shutter button click
//                KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                    cameraUiContainerBinding?.cameraCaptureButton!!.simulateClick()
//                }
                1 ->{}
                2 -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilesBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.setLifecycleOwner(viewLifecycleOwner)
        return view


    }




}