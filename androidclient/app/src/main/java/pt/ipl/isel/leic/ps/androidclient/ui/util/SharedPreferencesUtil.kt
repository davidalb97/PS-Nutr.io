package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.content.SharedPreferences
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.encryptedSharedPreferences
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant.DARK_MODE
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT

// Application shared preferences keys
const val FIRST_TIME = "isFirstTime"
const val USERNAME_KEY = "username"
const val PASSWORD_KEY = "password"
const val EMAIL_KEY = "email"
const val IMAGE_KEY = "image"
const val JWT_KEY = "jwt"
const val GLUCOSE_UNITS_KEY = "insulin_units"
const val WEIGHT_UNIT_KEY = "weight_units"

/**
 * Saves the user credentials into the encrypted shared preferences
 * and the JSON Web Token into the shared preferences.
 *
 * @param jwt - The JSON Web Token
 * @param username - The username
 * @param password - The password
 */
fun saveSession(jwt: String, email: String, username: String, password: String) {
    encryptedSharedPreferences
        .edit()
        .putString(EMAIL_KEY, email)
        .putString(USERNAME_KEY, username)
        .putString(PASSWORD_KEY, password)
        .apply()
    NutrioApp.sharedPreferences.edit()
        .putString(JWT_KEY, jwt)
        .apply()
}

fun deleteSession() {
    encryptedSharedPreferences.edit()
        .clear()
        .apply()
    NutrioApp.sharedPreferences.edit()
        .clear()
        .apply()
}

fun getUserSession(): UserSession? = NutrioApp.sharedPreferences
    .getJwt()
    ?.let(::UserSession)

fun requireUserSession() = requireNotNull(getUserSession())

fun SharedPreferences.getJwt() = getString(JWT_KEY, null)

fun SharedPreferences.getIsFirstTime() = getBoolean(FIRST_TIME, true)

fun SharedPreferences.Editor.setIsFirstTime(boolean: Boolean): SharedPreferences.Editor =
    putBoolean(FIRST_TIME, boolean)

fun SharedPreferences.getIsNightMode() = getBoolean(DARK_MODE, false)

fun SharedPreferences.Editor.setIsNightMode(defaultValue: Boolean): SharedPreferences.Editor =
    putBoolean(DARK_MODE, defaultValue)

fun SharedPreferences.getPassWord() = getString(PASSWORD_KEY, null)

fun SharedPreferences.getUsername() = getString(USERNAME_KEY, null)

fun SharedPreferences.getEmail() = getString(EMAIL_KEY, null)

fun SharedPreferences.getGlucoseUnitOrDefault(): String =
    getString(GLUCOSE_UNITS_KEY, DEFAULT_GLUCOSE_UNIT.toString())!!

fun SharedPreferences.getWeightUnitOrDefault() =
    getString(WEIGHT_UNIT_KEY, DEFAULT_WEIGHT_UNIT.toString())!!