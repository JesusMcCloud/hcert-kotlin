package ehn.techiop.hcert.kotlin.trust

import Cbor.DecodeOptions
import Cbor.Encoder
import ehn.techiop.hcert.kotlin.chain.mapToJson
import ehn.techiop.hcert.kotlin.chain.toBuffer
import ehn.techiop.hcert.kotlin.chain.toByteArray

actual class CwtCreationAdapter actual constructor() {

    private val map = mutableMapOf<Int, Any>()

    actual fun add(key: Int, value: Any) {
        if (value is ByteArray)
            map.set(key, value.toBuffer())
        else if (value is Long)
            map.set(key, value.toInt())
        else
            map.set(key, value)
    }

    actual fun addDgc(key: Int, innerKey: Int, input: ByteArray) {
        val innerMap = mapOf(
            innerKey to Cbor.Decoder.decodeFirstSync(input = input.toBuffer(), options = object : DecodeOptions {})
        )
        map.set(key, innerMap)
    }

    actual fun encode(): ByteArray {
        return Encoder.encode(map.mapToJson()).toByteArray()
    }

}
