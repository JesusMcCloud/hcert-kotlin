package ehn.techiop.hcert.kotlin.chain.ext

import ehn.techiop.hcert.data.Eudgc
import ehn.techiop.hcert.kotlin.data.EudgcSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestInput(
    @SerialName("JSON")
    @Serializable(with = EudgcSerializer::class)
    val content: Eudgc? = null,
    @SerialName("CBOR")
    val cbor: String? = null,
    @SerialName("COSE")
    val cose: String? = null,
    @SerialName("BASE45")
    val base45: String? = null,
    @SerialName("PREFIX")
    val withPrefix: String? = null,
    @SerialName("2DCODE")
    val encodedQrCode: String? = null,
    @SerialName("TESTCTX")
    val context: TestContext
)