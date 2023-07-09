package com.kuteam6.homept.restservice

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

/**
 * rest 통신 암호화를 위한 class -아직 사용x
 */
class EncryptionProcessing {
    companion object{
        const val key = "Zolfproject6team"; // 키
        const val iv = "6teamZolfproject"; // 초기화 벡터

        fun decryptAES128(encryptedText: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
            val encryptedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes, Charsets.UTF_8)
        }
        fun encryptAES128(plainText: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
            val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            val encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes)
            return encryptedBase64
        }

    }



}