package com.appserba.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.appserba.app.data.model.ActionState
import com.appserba.app.data.model.AuthUser
import com.appserba.app.util.getObject
import com.appserba.app.util.putObject

class AuthPref(val context:Context) {
    private val sp: SharedPreferences by lazy {
        context.getSharedPreferences(AuthPref::class.java.name, Context.MODE_PRIVATE)
    }

    private companion object {
        const val AUTH_USER = "auth_user"
        const val IS_LOGIN = "is_login"
    }

    var authUser: AuthUser?
    get() = sp.getObject(AUTH_USER)
    private set(value) = sp.edit().putObject(AUTH_USER, value).apply()

    var isLogin: Boolean
    get() = sp.getBoolean(IS_LOGIN, false)
    private set(value) = sp.edit().putBoolean(IS_LOGIN, value).apply()

    suspend fun login(email: String, password: String): ActionState<AuthUser> {
        val user = authUser
        return if (user == null) {
            ActionState(message = "Anda Belum Terdaftar", isSuccess = false)
        } else if (email.isBlank() || password.isBlank()) {
            ActionState(message = "Email Dan Password Tidak Boleh Kosong", isSuccess = false)
        } else if (user.email == email && user.password == password) {
            isLogin = true
            ActionState(authUser, message = "Anda Berhasil Login")
        } else {
            ActionState(message = "Email Dan password Salah", isSuccess = false)
        }
    }

    suspend fun register(user: AuthUser): ActionState<AuthUser> {
        if (user.email.isBlank() || user.password.isBlank()) {
            return ActionState(message = "Email Dan Password Tidak Boleh Kosong", isSuccess = false)
        } else {

            return ActionState(user, message = "Anda Berhasil Daftar")
        }
    }

    suspend fun logout(): ActionState<Boolean> {
        isLogin = false
        return ActionState(true, message = "Anda Berhasil Logout")
    }
}