package pt.isel.ps.g06.httpserver.security.converter

import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter

private const val ALGORITHM = "AES/ECB/PKCS5Padding"
private const val AES = "AES"

@Converter
abstract class AColumnCryptoConverter<T>(
        private val insulinProfilesSecret: InsulinProfilesSecret
) : AttributeConverter<T, String> {

    /**
     * Encrypts the plaintext in order to be stored into the database.
     * @param plainText - Raw data input to be encrypted.
     */
    override fun convertToDatabaseColumn(plainText: T?): String? {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return encrypt(cipher, plainText)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * Decrypts the data that came from the database
     * @param encData - The encrypted data which source is the database.
     */
    override fun convertToEntityAttribute(encData: String?): T? {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)

        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return decrypt(cipher, encData)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun encrypt(cipher: Cipher, plainData: T?): String? {
        val bytesToEncrypt = typeToString(plainData ?: return null).toByteArray()
        return encoder().encodeToString(doFinal(cipher, bytesToEncrypt))
    }

    private fun decrypt(cipher: Cipher, encryptedData: String?): T {
        return stringToType(String(doFinal(cipher, decoder().decode(encryptedData))))
    }

    private fun encoder(): Base64.Encoder = Base64.getEncoder()

    private fun decoder(): Base64.Decoder = Base64.getDecoder()

    private fun doFinal(cipher: Cipher, bytes: ByteArray): ByteArray = cipher.doFinal(bytes)

    abstract fun stringToType(str: String) : T

    abstract fun typeToString(type: T): String
}