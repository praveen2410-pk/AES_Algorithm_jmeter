# JMeter Request Body Encryption JSR223 PreProcessor

AES (Advanced Encryption Standard) serves as a secret code machine, encrypting data or messages and transforming them into ciphertext using a secret Key and an initial value (IV). This process ensures that identical messages do not consistently result in the same secret code. After generating the secret code, the value is converted into Base64 encoding with padding.

In the request body, before being sent to servers, the request is encrypted using AES in Cipher Block Chaining (CBC) mode. To handle this encryption in JMeter, use the JSR223 Preprocessor with Groovy.

## Prerequisites

Ensure that you have the Apache Commons Codec and Ivy library installed in your JMeter setup. You can download it from [Codec Plugin](https://commons.apache.org/proper/commons-codec/download_codec.cgi) and [Ivy Plugin](https://ant.apache.org/ivy/download.cgi.) or download the [lib folder](./lib) copy and paste the Jar files on your jmeter lib folder.

Place the downloaded JAR file in the `lib` directory of your JMeter installation.

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

**Note:** Uncomment the logging lines for debugging if needed.

## Configuration

Adjust the following variables in the script according to your requirements:

- `md5`: The MD5 hash is generated from the channel.
- `initVector`: Initialization vector used in AES encryption.
- `mainKey`: The encryption key. You can either generate it or use a predefined key.

## Debugging

To enable debugging, uncomment the logging lines in the script:

```groovy
// Uncomment the following lines for debugging
// log.info("Original Request Body: " + requestBody)
// log.info("Main Key: " + mainKey)
// log.info("Encrypted Body: " + encrypted)
// log.info("Timestamp: " + time)
// log.info("Hash: " + hash)
