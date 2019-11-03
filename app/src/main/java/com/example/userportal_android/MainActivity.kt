package com.example.userportal_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.github.nkzawa.emitter.Emitter

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.Json
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var currentUserTextView : TextView? = null
    private var emailEditText : EditText? = null
    private var passwordEditText : EditText? = null
    private var signupButton : Button? = null
    private var loginButton : Button? = null
    private var logoutButton : Button? = null
    private var socket : Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()

        setOnClickListener_SignupButton()
        setOnClickListener_LoginButton()
        setOnClickListener_LogoutButton()

        setSocketIOListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

        socket?.disconnect()
    }

    private fun initializeComponents() {
        currentUserTextView = findViewById(R.id.Main_TextView_CurrentUser_Name)
        emailEditText = findViewById(R.id.Main_EditText_Email)
        passwordEditText = findViewById(R.id.Main_EditText_Password)
        signupButton = findViewById(R.id.Main_Button_Signup)
        loginButton = findViewById(R.id.Main_Button_Login)
        logoutButton = findViewById(R.id.Main_Button_Logout)
    }

    private fun setOnClickListener_SignupButton() {
        signupButton!!.setOnClickListener {
            val data = CredentialsRequestBody(
                emailEditText!!.text.toString(),
                passwordEditText!!.text.toString())

            Log.println(Log.DEBUG, TAG, Json.stringify(CredentialsRequestBody.serializer(), data))
            socket?.emit("sign-up-request", Json.stringify(CredentialsRequestBody.serializer(), data))
        }
    }

    private fun setOnClickListener_LoginButton() {
        loginButton!!.setOnClickListener {
            val data = CredentialsRequestBody(
                emailEditText!!.text.toString(),
                passwordEditText!!.text.toString())

            socket?.emit("log-in-request", Json.stringify(CredentialsRequestBody.serializer(), data))
        }
    }

    private fun setOnClickListener_LogoutButton() {
        logoutButton!!.setOnClickListener {
            socket?.emit("log-out-request")
        }
    }

    private fun setSocketIOListeners() {
        socket = IO.socket("https://userportal-web.azurewebsites.net/")

        socket?.on("auth-state-changed", Emitter.Listener {
            if (it != null)
                currentUserTextView!!.setText((it[0] as JSONObject).getString("email"))
        })
    }

    @Serializable
    data class CredentialsRequestBody (val username : String, val password : String)
}
