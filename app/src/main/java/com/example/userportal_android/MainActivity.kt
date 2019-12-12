package com.example.userportal_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.json.JSONObject
import com.github.nkzawa.socketio.client.Socket

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

        setSocketIOListeners()

        setOnClickListener_SignupButton()
        setOnClickListener_LoginButton()
        setOnClickListener_LogoutButton()
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

            // Log.println(Log.DEBUG, TAG, data.email)

            socket!!.emit("sign-up-request", Json.stringify(CredentialsRequestBody.serializer(), data))
        }
    }

    private fun setOnClickListener_LoginButton() {
        loginButton!!.setOnClickListener {
            val data = CredentialsRequestBody(
                emailEditText!!.text.toString(),
                passwordEditText!!.text.toString())

            // Log.println(Log.DEBUG, TAG, data.email)

            socket!!.emit("log-in-request", Json.stringify(CredentialsRequestBody.serializer(), data))
        }
    }

    private fun setOnClickListener_LogoutButton() {
        logoutButton!!.setOnClickListener {
            socket?.emit("log-out-request")
        }
    }

    private fun setSocketIOListeners() {
        val application = getApplication() as UserPortalApplication

        socket = application.socket

        socket?.on(Socket.EVENT_CONNECT, { Log.println(Log.DEBUG, TAG, "Connected") })

        socket?.connect()

        socket?.on("auth-state-changed") { args ->
            this@MainActivity.runOnUiThread {
                /*
                Log.println(Log.DEBUG, TAG, "Auth state changed! args size: " + args.size.toString())

                for (item in args)
                    item?.let { Log.println(Log.DEBUG, TAG, item.toString()) }
                 */

                if (args[0] != null) {
                    val result = Json.nonstrict.parse(LoginSuccessResponseBody.serializer(), args[0] as String)
                    Toast.makeText(applicationContext, "Logged in user: ${result.email}", Toast.LENGTH_SHORT).show();
                    currentUserTextView!!.text = result.email
                }
                else {
                    Toast.makeText(applicationContext, "Logged out.", Toast.LENGTH_SHORT).show();
                    currentUserTextView!!.text = "N/A"
                }
            }
        }

        socket?.on("sign-up-failure") { args ->
            this@MainActivity.runOnUiThread {
                /*
                Log.println(Log.DEBUG, TAG, "Sign up failure! args size: " + args.size.toString())

                for (item in args)
                    item?.let { Log.println(Log.DEBUG, TAG, item.toString()) }
                 */

                if (args[0] != null) {
                    val result = Json.nonstrict.parse(FirebaseFailureBody.serializer(), args[0] as String)
                    Toast.makeText(applicationContext, "Sign in failed: ${result.message}", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(applicationContext, "Sign in failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        socket?.on("log-in-failure") { args ->
            this@MainActivity.runOnUiThread {
                /*
                Log.println(Log.DEBUG, TAG, "Log in failure! args size: " + args.size.toString())

                for (item in args)
                    item?.let { Log.println(Log.DEBUG, TAG, item.toString()) }
                 */

                if (args[0] != null) {
                    val result = Json.nonstrict.parse(FirebaseFailureBody.serializer(), args[0] as String)
                    Toast.makeText(applicationContext, "Log in failed: ${result.message}", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(applicationContext, "Log in failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        socket?.on("log-out-failure") { args ->
            this@MainActivity.runOnUiThread {
                /*
                Log.println(Log.DEBUG, TAG, "Log in failure! args size: " + args.size.toString())

                for (item in args)
                    item?.let { Log.println(Log.DEBUG, TAG, item.toString()) }
                 */

                if (args[0] != null) {
                    val result = Json.nonstrict.parse(FirebaseFailureBody.serializer(), args[0] as String)
                    Toast.makeText(applicationContext, "Log out failed: ${result.message}", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(applicationContext, "Log out failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Serializable
    data class CredentialsRequestBody (val email : String, val password : String)

    @Serializable
    data class LoginSuccessResponseBody (val email: String)

    @Serializable
    data class FirebaseFailureBody(val code : String, val message : String)
}
