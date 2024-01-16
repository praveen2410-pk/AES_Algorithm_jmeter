import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import java.util.Base64
import org.apache.commons.codec.digest.DigestUtils

def md5 = vars.get("Channel" + vars.get("channel")).toString() // get values from channel and pass to generate main key with crypto lib
def initVector = "BSonK88FyMYQGSAi" // IV value
def requestBody = sampler.getArguments().getArgument(0).getValue()
//def mainKey = calculateMD5(md5) // KEY will generate from the channel values
def mainKey = "d41d8cd98f00b204e9800998ecf8427e" // KEY Hardcode values

def encrypted = encryptAES(requestBody, mainKey, initVector)
// Generate timestamp and calculate SHA256 hash for checksum values
def time = String.valueOf(new Date().getTime())
def hash = DigestUtils.sha256Hex(requestBody + time)

// Update request body and set checksum in the sampler's arguments
sampler.getArguments().removeAllArguments()
sampler.addNonEncodedArgument("", "{\"request\":\"" + encrypted + "\"}", "")
sampler.getArguments().getArgument(0).setName("")
vars.put("checksum", time + "=" + hash) // Pass the encryption values on the Header

// Function to calculate MD5 for KEY Generation using your Channl values
def calculateMD5(input) {
    def md = MessageDigest.getInstance("MD5")
    md.update(input.getBytes())
    def digest = md.digest()
    def result = new BigInteger(1, digest).toString(16)
    while (result.length() < 32) {
        result = "0" + result
    }
    return result
}
// Function to encrypt using AES
def encryptAES(data, key, initVector) {
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
/*
// Uncomment the following lines for debugging
 log.info("Original Request Body: " + requestBody)
 log.info("Main Key: " + mainKey)
 log.info("Encrypted Body: " + encrypted)
 log.info("Timestamp: " + time)
 log.info("Hash: " + hash) */