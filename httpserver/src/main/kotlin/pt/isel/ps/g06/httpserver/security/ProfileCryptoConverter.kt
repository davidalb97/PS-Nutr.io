package pt.isel.ps.g06.httpserver.security

import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter

private const val ALGORITHM = "AES/ECB/PKCS5Padding"
private const val AES = "AES"

@Converter
class ProfileCryptoConverter(
        private val insulinProfilesSecret: InsulinProfilesSecret
) : AttributeConverter<String, String> {

    /**
     * Encrypts the plaintext in order to be stored into the database.
     * @param plainText - Raw data input to be encrypted.
     */
    override fun convertToDatabaseColumn(plainText: String?): String? {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText?.toByteArray()))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * Decrypts the data that came from the database
     * @param encData - The encrypted data which source is the database.
     */
    override fun convertToEntityAttribute(encData: String?): String? {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)

        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return String(cipher.doFinal(Base64.getDecoder().decode(encData)))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}