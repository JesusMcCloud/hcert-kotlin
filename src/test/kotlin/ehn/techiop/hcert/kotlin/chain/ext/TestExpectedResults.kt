package ehn.techiop.hcert.kotlin.chain.ext

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestExpectedResults(
    @SerialName("EXPECTEDVALIDOBJECT")
    val validObject: Boolean? = null,
    @SerialName("EXPECTEDSCHEMAVALIDATION")
    val schemaValidation: Boolean? = null,
    @SerialName("EXPECTEDENCODE")
    val encode: Boolean? = null,
    @SerialName("EXPECTEDDECODE")
    val decode: Boolean? = null,
    @SerialName("EXPECTEDSIGN")
    val sign: Boolean? = null,
    @SerialName("EXPECTEDVERIFY")
    val verify: Boolean? = null,
    @SerialName("EXPECTEDUNPREFIX")
    val unprefix: Boolean? = null,
    @SerialName("EXPECTEDVALIDJSON")
    val validJson: Boolean? = null,
    @SerialName("EXPECTEDB45DECODE")
    val base45decode: Boolean? = null,
    @SerialName("EXPECTEDPICTUREDECODE")
    val pictureDecode: Boolean? = null,
    @SerialName("EXPTECTEDEXPIRED")
    val expired: Boolean? = null
)