package com.example.userportal_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var currentUserTextView : TextView? = null;
    private var signupButton : Button? = null;
    private var loginButton : Button? = null;
    private var logoutButton : Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()

        setOnClickListener_SignupButton()
        setOnClickListener_LoginButton()
        setOnClickListener_LogoutButton()
    }

    private fun initializeComponents() {
        currentUserTextView = findViewById(R.id.Main_TextView_CurrentUser_Name)
        signupButton = findViewById(R.id.Main_Button_Signup)
        loginButton = findViewById(R.id.Main_Button_Login)
        logoutButton = findViewById(R.id.Main_Button_Logout)
    }

    private fun setOnClickListener_SignupButton() {
        signupButton!!.setOnClickListener {
            Log.println(Log.DEBUG, TAG, "Hello signup.")
        }
    }

    private fun setOnClickListener_LoginButton() {
        loginButton!!.setOnClickListener {
            Log.println(Log.DEBUG, TAG, "Hello login.")
        }
    }

    private fun setOnClickListener_LogoutButton() {
        logoutButton!!.setOnClickListener {
            Log.println(Log.DEBUG, TAG, "Hello logout.")
        }
    }
}
