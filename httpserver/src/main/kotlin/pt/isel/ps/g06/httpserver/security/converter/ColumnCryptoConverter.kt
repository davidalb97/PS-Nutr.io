package pt.isel.ps.g06.httpserver.security.converter

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.springConfig.bean.InsulinProfilesSecret
import java.util.*
import javax.crypto.Cipher

@Component
class ColumnCryptoConverter(private val insulinProfilesSecret: InsulinProfilesSecret) {

    fun convertToDatabaseColumn(plainText: String?): String {
        val cipher = Cipher.getInstance(insulinProfilesSecret.transformation)
        cipher.init(Cipher.ENCRYPT_MODE, insulinProfilesSecret.key)
        return encrypt(cipher, plainText)
    }

    fun convertToEntityAttribute(encryptedText: String?): String {

        try {
            val cipher = Cipher.getInstance(insulinProfilesSecret.transformation)
            cipher.init(Cipher.DECRYPT_MODE, insulinProfilesSecret.key)
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