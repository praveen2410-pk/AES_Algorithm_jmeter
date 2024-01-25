#  JMeter Request Body Encryption with AES Algorithm using JSR223 PreProcessor
AES (Advanced Encryption Standard) functions as a secure encryption algorithm, transforming data or messages into ciphertext. This transformation involves the use of a secret key and an initial value (IV). The process ensures that identical messages don't consistently produce the same ciphertext. Once the ciphertext is generated, it is converted into Base64 encoding with padding.

## JMeter Test Plan
Before being sent to servers, the request body is encrypted using AES in Cipher Block Chaining (CBC) mode. To implement this encryption in JMeter, utilize the JSR223 Preprocessor with the Groovy language.

## Prerequisites

Ensure that you have the Apache Commons Codec and Ivy library installed in your JMeter setup. You can download it from [Codec Plugin](https://commons.apache.org/proper/commons-codec/download_codec.cgi) and [Ivy Plugin](https://ant.apache.org/ivy/download.cgi.) Place the downloaded JAR file in the `lib` directory of your JMeter installation or download the [lib folder](./lib) copy and paste the Jar files on your jmeter lib folder.

**1.Apache Commons Codec:**

**Purpose:** This library provides implementations of common encoders and decoders, such as Base64 encoding. In this context, it is used to encode the result of AES encryption into Base64 format.

**Usage:** The Base64 class from Apache Commons Codec is used to perform Base64 encoding of the encrypted bytes.

**2.Ivy Library:**

**Purpose:** Ivy is a dependency manager for Java that helps manage project dependencies (JAR files) in a systematic way. In JMeter, it can be used to manage external libraries, making it easier to include and reference them in your test plan.

**Usage:** The Ivy library is used to manage the dependencies of the Groovy script. This is important because the script relies on classes from external libraries (e.g., javax.crypto.Cipher, org.apache.commons.codec.binary.Base64). By using Ivy, you ensure that these dependencies are available to the script during runtime.

## Usage

1. Open your JMeter test plan.

2. Add a `JSR223 PreProcessor` to your HTTP Request sampler.

3. Set the language to `groovy`.

4. Copy and paste the content of the [JSR223 PreProcessor](./JSR223PreProcessor.groovy)into the script area.
5. If have to encrypt the [32,16 bytes of Key and IV use this JSR223_PreProcessor](./JSR223_PreProcessor_32_16_bytes.groovy)

6. Save your test plan.
   
**Note:** Please find the Sample JMX file for reference.[Jmeter Script](./MM_Encryption_AES.jmx)
## Preprocessor Script

The JSR223 PreProcessor file contains the Groovy script for the JSR223 PreProcessor. The script performs the following:

- Retrieves the channel and other necessary values.
- Generates the encryption key using the MD5 hash of the channel.
- Encrypts the request body using AES.
- Updates the request body in the sampler's arguments.
- Sets the checksum in JMeter variables.
## Encryption Function
```groovy
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
## Decryption Function

```groovy
def decryptAES(encryptedData, key, initVector) {
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
        log.error("Error decrypting data: " + e.getMessage(), e)
        return null
    }
}
'''

## Configuration

Adjust the following variables in the script according to your requirements:
Your explanation is clear, but I've made a few adjustments for clarity:

- `md5`: The MD5 hash is generated from the channel.  In this case, the key is generated based on header parameter values.
- `initVector`: Initialization vector used in AES encryption. This process ensures that identical messages do not consistently produce the same ciphertext.
- `mainKey`: The encryption key. You can either generate it or use a predefined key. Consider this as your secret key.

