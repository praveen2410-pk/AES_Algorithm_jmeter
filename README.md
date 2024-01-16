# JMeter Request Body Encryption JSR223 PreProcessor

This JMeter script includes a preprocessor that encrypts the request body using AES encryption. The encryption key is generated based on the channel values.

## Prerequisites

Ensure that you have the Apache Commons Codec library installed in your JMeter setup. You can download it from [https://commons.apache.org/proper/commons-codec/download_codec.cgi](https://commons.apache.org/proper/commons-codec/download_codec.cgi). or download the [lib folder](./lib) copy and paste the Jar files on your jmeter lib folder.

Place the downloaded JAR file in the `lib` directory of your JMeter installation.

## Usage

1. Open your JMeter test plan.

2. Add a `JSR223 PreProcessor` to your HTTP Request sampler.

3. Set the language to `groovy`.

4. Copy and paste the content of the [preprocessor-script.groovy](./JSR223-PreProcessor.groovy))into the script area.

5. Save your test plan.

## Preprocessor Script

The [preprocessor-script.groovy](./preprocessor-script.groovy) file contains the Groovy script for the JSR223 PreProcessor. The script performs the following:

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
