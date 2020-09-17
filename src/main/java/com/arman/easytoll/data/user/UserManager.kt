package id.jsl.autoloka.data.user

import com.arman.easytoll.data.Storage
import com.arman.easytoll.data.model.LoginData
import com.arman.easytoll.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    private val storage: Storage,
    private val userDataRepository: UserDataRepository
) {
    fun loginSaveUserData(loginData: LoginData){
        storage.setBoolean(Constants.PREF_IS_LOGIN, true)
//        storage.setString(Constants.PREF_TOKEN, "Bearer " + loginData.token!!)
        storage.setString(Constants.PREF_ID, loginData.id!!)
        storage.setString(Constants.PREF_EMAIL, loginData.email!!)
        storage.setString(Constants.PREF_NAME, loginData.name!!)
        storage.setString(Constants.PREF_AVATAR, loginData.photo!!)
    }

    val token: String get() = storage.getString(Constants.PREF_TOKEN)
    val isLogin: Boolean get() = storage.getBoolean(Constants.PREF_IS_LOGIN)
    val name: String get() = storage.getString(Constants.PREF_NAME)
    val email: String get() = storage.getString(Constants.PREF_EMAIL)
    val photo: String get() = storage.getString(Constants.PREF_AVATAR)
    val id: String get() = storage.getString(Constants.PREF_ID)

    fun setNumberOtp(number: String) {userDataRepository.setPhoneNumber(number)}
    fun setOtpID(otpID: String) {userDataRepository.setOtpID(otpID)}

    val numberOtp: String? get() = userDataRepository.phoneNumber
    val otpID: String get() = userDataRepository.otpID

    fun logout() {
        storage.clear()
    }


}
