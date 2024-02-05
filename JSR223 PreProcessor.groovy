import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import java.util.Base64
import org.apache.commons.codec.digest.DigestUtils

def mainKey = "f6d565e47d16e33782456a73ce30c31a"

def requestBodyArg = sampler.getArguments().getArgument(0)
def requestBody = requestBodyArg ? requestBodyArg.getValue() : ""

// Check if requestBody is not empty before proceeding with encryption
if (!requestBody.isEmpty()) {
    try {
        // Function to encrypt using AES
        def encryptAES = { data, key, initVector ->
            try {
                def keyBytes = key.getBytes(StandardCharsets.UTF_8)
                def ivBytes = initVector.getBytes(StandardCharsets.UTF_8)
                def keySpec = new SecretKeySpec(keyBytes, "AES")
                def ivSpec = new IvParameterSpec(ivBytes)
                def cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                def encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8))
                return Base64.getEncoder().encodeToString(encryptedBytes)
            } catch (Exception e) {
                log.error("Error encrypting data: " + e.getMessage(), e)
                return null
            }
        }

        def encrypted = encryptAES(requestBody, mainKey, "BSonK88FyMYQGSAi")

        // Generate timestamp and calculate SHA256 hash for checksum values
        def time = String.valueOf(new Date().getTime())
        def hash = DigestUtils.sha256Hex(requestBody + time)

        // Function to decrypt using AES
        def decryptAES = { encryptedData, key, initVector ->
            try {
                def keyBytes = key.getBytes(StandardCharsets.UTF_8)
                def ivBytes = initVector.getBytes(StandardCharsets.UTF_8)
                def keySpec = new SecretKeySpec(keyBytes, "AES")
                def ivSpec = new IvParameterSpec(ivBytes)
                def cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                def decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData))
                return new String(decryptedBytes, StandardCharsets.UTF_8)
            } catch (Exception e) {
               // log.error("Error decrypting data: " + e.getMessage(), e)
                return null
            }
        }

       // Update request body and set checksum in the sampler's arguments
def encryptedArgument = sampler.getArguments().getArgument(0)
encryptedArgument.setValue("{\"request\":\"" + encrypted + "\"}")
encryptedArgument.setName("")
vars.put("checksum", time + "=" + hash)


        def decrypted = decryptAES(encrypted, mainKey, "BSonK88FyMYQGSAi")

      //  log.info("Request Body before encryption: " + requestBody)
        //log.info("Encrypted Data: " + encrypted)
        //log.info("Timestamp: " + time)
        //log.info("Checksum Hash: " + hash)
        //log.info("Decrypted Data: " + decrypted)

    } catch (Exception ex) {
        log.error("An unexpected error occurred: " + ex.getMessage(), ex)
    }
} else {
    // Handle the case where requestBody is empty (no need for encryption)
    log.info("Request body is empty. No encryption needed.")
}
