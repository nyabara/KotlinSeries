package com.example.kotlinseries.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kotlinseries.R
import com.example.kotlinseries.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var textEmail: EditText
    private lateinit var textPassword: EditText
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Capturing data from views using databinding
        textEmail = binding.emailEditText
        textPassword = binding.passwordEditText
        progressBar = binding.progressBar

        textEmail.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
        textPassword.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = sharedViewModel
            // TODO: initialize the LoginFragment variables
            loginInFragment = this@LoginFragment
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

    fun goToNextScreen(){
        val myMap = mutableListOf<Map<String,String>>(mutableMapOf("password" to "123","email" to "ayomagilbert@gmail.com")
            ,mutableMapOf("password" to "123","email" to "ayomagilbert@gmail.com"))
        val validUser:(String,String) -> String ={email, password ->
            var result =""
            for(user in myMap){
                val passwordInMap = user.get("password")
                val emailInMap = user.get("email")
                if (password == passwordInMap && emailInMap == email){
                    result = "Success"
                    //Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT)
                }else{
                    result = "Does not exist"
                }
            }

               result
        }

        val finalResult = isValidUser(textEmail.text.toString(),textPassword.text.toString(),validUser)

        Log.d("finalResult",finalResult)
        if (finalResult == "Success"){
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }else{
            Toast.makeText(requireActivity(),finalResult,Toast.LENGTH_SHORT)
            return

        }

    }

    fun goToRegistrationScreen(){

    }

    fun isValidUser(email:String,password:String,validUser:(String,String)->String):String{
        return validUser(email,password)
    }
}