package com.example.kotlinseries.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.kotlinseries.Injection
import com.example.kotlinseries.R
import com.example.kotlinseries.databinding.FragmentAboutBinding
import com.example.kotlinseries.viewmodels.KotlinSeriersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerText: TextView
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    //viewmodel instance
    private val viewModel: KotlinSeriersViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, Injection.provideViewModelFactory(
                context = activity,
                owner = this,
            )
        )
            .get(KotlinSeriersViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datePickerFragment = DatePickerFragment()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = sharedViewModel

            aboutFragment = this@AboutFragment
        }
        datePickerText = binding.datePicker
        try{

            datePickerText.setOnClickListener {
                datePickerFragment.show(childFragmentManager, "datePicker")
//                lifecycleScope.launch {
//                    repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//                        viewModel.uiState.collectLatest { uiState ->
//                            if (uiState.dob == null) {
//                                binding.dob.text = "Date of Birth Not Selected"
//                            } else {
//                                binding.dob.text = uiState.dob
//                            }
//                        }
//                    }
//                }
            }

        }catch (e:Exception){
            Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
        }

        binding.ageCalculator.setOnClickListener {
            binding.dob.text = datePickerFragment.returnedAge
        }





    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }




}