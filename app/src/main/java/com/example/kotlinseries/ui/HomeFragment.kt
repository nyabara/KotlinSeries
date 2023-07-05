package com.example.kotlinseries.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.kotlinseries.R
class HomeFragment : Fragment() {
 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mywebView:WebView = view.findViewById(R.id.fWebView)
        mywebView.webViewClient = object : WebViewClient (){
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        mywebView.settings.javaScriptEnabled = true
        mywebView.loadUrl("https://www.kingdombankltd.co.ke/")
        mywebView.settings.allowContentAccess = true
        mywebView.settings.domStorageEnabled = true
        mywebView.settings.useWideViewPort = true
    }
}