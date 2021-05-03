package ehn.techiop.hcert.kotlin.chain.ext

import ehn.techiop.hcert.kotlin.data.IsoOffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class TestContext(
    @SerialName("VERSION")
    val version: Int,
    @SerialName("SCHEMA")
    val schema: String,
    @SerialName("JWK")
    val certList: CertificateList, // TODO is JWK
    @SerialName("VALIDATIONCLOCK")
    @Serializable(with = IsoOffsetDateTimeSerializer::class)
    val validationClock: OffsetDateTime,
    @SerialName("DESCRIPTION")
    val description: String,
    @SerialName("EXPECTEDRESULTS")
    val expectedResult: TestExpectedResults
)