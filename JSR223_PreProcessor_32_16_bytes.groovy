import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.util.Base64
@Grapes([
    @Grab(group='org.bouncycastle', module='bcprov-jdk15on', version='1.68'),
    @Grab(group='org.bouncycastle', module='bcpkix-jdk15on', version='1.68')
])
@GrabConfig(systemClassLoader=true)
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
@Grab(group='commons-codec', module='commons-codec', version='1.14')
@Grapes([
    @Grab(group='org.apache.ivy', module='ivy', version='2.5.0')
])
@GrabConfig(systemClassLoader=true)

@Grab(group='org.apache.jmeter', module='ApacheJMeter_core', version='5.4.1')
@Grab(group='org.apache.jmeter', module='ApacheJMeter_http', version='5.4.1')

@Grab(group='org.apache.jmeter', module='ApacheJMeter_java', version='5.4.1')
@Grab(group='org.apache.jmeter', module='ApacheJMeter_jdbc', version='5.4.1')

@Grab(group='org.apache.jmeter', module='ApacheJMeter_functions', version='5.4.1')
@Grab(group='org.apache.jmeter', module='ApacheJMeter_jms', version='5.4.1')

import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
def requestBody = sampler.getArguments().getArgument(0).getValue()
def encryptedValue = encryptHeader("${requestBody}", "88509819877EE4F2FF07C4B6406E94379412433E798D418C46A1DA7FCC17D38C", "885928D9E7BB4483C6C501578AF0ACEE") //Key and IV
println("Encrypted Value: " + encryptedValue)
log.info("Encrypted Value: " + encryptedValue)

String encryptHeader(String parameter, String keyStr, String ivStr) {
    try {
        def keyBytes = keyStr.getBytes("UTF-8").toList().subList(0, Math.min(32, keyStr.length())).toArray(Byte[]) // 32 bytes
        def key = new SecretKeySpec(keyBytes as byte[], "AES")
        def ivBytes = ivStr.getBytes("UTF-8").toList().subList(0, Math.min(16, ivStr.length())).toArray(Byte[]) // 16 bytes
        def iv = new IvParameterSpec(ivBytes as byte[])
        def cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)
        def encryptedBytes = cipher.doFinal(parameter.getBytes(StandardCharsets.UTF_8))
        def encryptedString = Base64.getEncoder().encodeToString(encryptedBytes)
        return encryptedString
    } catch (Exception e) {
        e.printStackTrace()
        log.error("Error during encryption: " + e.getMessage())
        return "Something Wrong"
    }
}

