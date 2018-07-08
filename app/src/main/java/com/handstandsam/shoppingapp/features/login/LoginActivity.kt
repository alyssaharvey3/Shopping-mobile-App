package com.handstandsam.shoppingapp.features.login

import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.AppCompatEditText
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.handstandsam.shoppingapp.R
import com.handstandsam.shoppingapp.di.AppGraph
import com.handstandsam.shoppingapp.features.home.HomeActivity
import com.handstandsam.shoppingapp.appGraph
import io.reactivex.disposables.Disposable

class LoginActivity : AppCompatActivity() {

    private val appGraph: AppGraph by lazy { application.appGraph() }

    lateinit var rememberMeCheckbox: AppCompatCheckBox

    lateinit var usernameEditText: AppCompatEditText

    lateinit var passwordEditText: AppCompatEditText

    private var disposable: Disposable? = null

    private lateinit var loginView: MyLoginView

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "Log in to Shopping App"
        passwordEditText = findViewById(R.id.password)
        usernameEditText = findViewById(R.id.username)
        rememberMeCheckbox = findViewById(R.id.remember_me)
        findViewById<View>(R.id.submit).setOnClickListener { presenter.loginClicked() }

        loginView = MyLoginView()
        presenter = LoginPresenter(
            view = loginView,
            sessionManager = appGraph.sessionGraph.sessionManager,
            userPreferences = appGraph.sessionGraph.userPreferences,
            userRepo = appGraph.networkGraph.userRepo
        )

        usernameEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            // If the event is a key-down event on the "enter" button
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                presenter.loginClicked()
                return@OnKeyListener true
            }
            false
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
        }
    }

    interface LoginView {

        fun startHomeActivity()

        var username: String

        val isRememberMeChecked: Boolean

        fun setRememberMe(rememberMe: Boolean)

        fun kickToHomeScreen()

        val password: String

        fun showToast(@StringRes stringResourceId: Int)
    }

    inner class MyLoginView : LoginView {

        override fun startHomeActivity() {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            this@LoginActivity.startActivity(intent)
        }

        override var username: String
            get() = usernameEditText.text.toString()
            set(username) = usernameEditText.setText(username)

        override val isRememberMeChecked: Boolean
            get() = rememberMeCheckbox.isChecked

        override fun setRememberMe(value: Boolean) {
            rememberMeCheckbox.isChecked = value
        }

        override fun kickToHomeScreen() {
            this@LoginActivity.startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }

        override val password: String
            get() = passwordEditText.text.toString()

        override fun showToast(@StringRes stringResourceId: Int) {
            Toast.makeText(applicationContext, stringResourceId, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }
}
