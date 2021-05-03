package ehn.techiop.hcert.kotlin.chain.ext

import com.fasterxml.jackson.databind.ObjectMapper
import ehn.techiop.hcert.data.Eudgc
import ehn.techiop.hcert.kotlin.chain.Chain
import ehn.techiop.hcert.kotlin.chain.ChainResult
import ehn.techiop.hcert.kotlin.chain.SampleData
import ehn.techiop.hcert.kotlin.chain.impl.DefaultBase45Service
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCborService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCompressorService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultContextIdentifierService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCoseService
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import ehn.techiop.hcert.kotlin.chain.toHexString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.File
import java.security.cert.X509Certificate
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class ExtendedTestGenerator {

    @Test
    fun write01Good() {
        val eudgc = ObjectMapper().readValue(SampleData.vaccination, Eudgc::class.java)
        val clock = Clock.fixed(Instant.ofEpochSecond(1620058140), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createTestCaseJson(clock, eudgc, result, listOf(cryptoService.getCertificate()), "All good", "testcase01")
    }

    private fun createTestCaseJson(
        clock: Clock,
        eudgc: Eudgc,
        result: ChainResult,
        certificateList: List<X509Certificate>,
        description: String,
        testcaseNumber: String
    ) {
        val expectedResult = TestExpectedResults(validObject = true, schemaValidation = true)
        val certList = CertificateList(certificateList)
        val context = TestContext(
            1, "1.0.0", certList, OffsetDateTime.ofInstant(clock.instant(), clock.zone),
            description, expectedResult
        )
        val testcase = TestInput(
            eudgc,
            result.step1Cbor.toHexString(),
            result.step2Cose.toHexString(),
            result.step4Encoded,
            result.step5Prefixed,
            null,
            context
        )
        File("src/test/resources/$testcaseNumber.json").bufferedWriter().use {
            it.write(Json.encodeToString(testcase))
        }
    }


}
