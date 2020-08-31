package pt.isel.ps.g06.httpserver.security.converter

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

private const val ALGORITHM = "AES/ECB/PKCS5Padding"
private const val AES = "AES"

@Component
class ColumnCryptoConverter(private val insulinProfilesSecret: InsulinProfilesSecret) {

    fun convertToDatabaseColumn(plainText: String?): String {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return encrypt(cipher, plainText)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun convertToEntityAttribute(encryptedText: String?): String {
        val key = SecretKeySpec(insulinProfilesSecret.secret.toByteArray(), AES)

        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return decrypt(cipher, encryptedText)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun encrypt(cipher: Cipher, plainText: String?): String =
            encoder().encodeToString(doFinal(cipher, plainText!!.toByteArray()))

    private fun decrypt(cipher: Cipher, encryptedText: String?): String =
            String(doFinal(cipher, decoder().decode(encryptedText)))


    private fun encoder(): Base64.Encoder = Base64.getEncoder()

    private fun decoder(): Base64.Decoder = Base64.getDecoder()

    private fun doFinal(cipher: Cipher, bytes: ByteArray): ByteArray = cipher.doFinal(bytes)
}