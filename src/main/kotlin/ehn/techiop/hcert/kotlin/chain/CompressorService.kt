package ehn.techiop.hcert.kotlin.chain

import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream
import java.util.zip.InflaterInputStream

class CompressorService {

    /**
     * Compresses input with ZLIB = deflating
     */
    fun encode(input: ByteArray): ByteArray {
        return DeflaterInputStream(input.inputStream(), Deflater(9)).readAllBytes()
    }

    /**
     * *Optionally* decompresses input with ZLIB = inflating.
     *
     * If the [input] does not start with ZLIB magic numbers (0x78), no decompression happens
     */
    fun decode(input: ByteArray): ByteArray {
        if (input.size >= 2 && input[0] == 0x78.toByte()) { // ZLIB magic headers
            if (input[1] == 0x01.toByte() || // Level 1
                input[1] == 0x5E.toByte() || // Level 2 - 5
                input[1] == 0x9C.toByte() || // Level 6
                input[1] == 0xDA.toByte()    // Level 7 - 9
            ) {
                return InflaterInputStream(input.inputStream()).readAllBytes()
            }
        }
        return input
    }

}