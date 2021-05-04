package ehn.techiop.hcert.kotlin.chain.ext

import com.fasterxml.jackson.databind.ObjectMapper
import ehn.techiop.hcert.data.Eudgc
import ehn.techiop.hcert.kotlin.chain.Chain
import ehn.techiop.hcert.kotlin.chain.ChainResult
import ehn.techiop.hcert.kotlin.chain.SampleData
import ehn.techiop.hcert.kotlin.chain.asBase64
import ehn.techiop.hcert.kotlin.chain.impl.DefaultBase45Service
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCborService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCompressorService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultContextIdentifierService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCoseService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultTwoDimCodeService
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import ehn.techiop.hcert.kotlin.chain.toHexString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.security.cert.X509Certificate
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

@Disabled("Don't want to generate test case files every time")
class ExtendedTestGenerator {

    @Test
    fun writeGen01Vaccination() {
        val eudgc = ObjectMapper().readValue(SampleData.vaccination, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            // TODO Would also need to specify validity, country code!
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createGenerationTestCaseJson(
            clock, eudgc, result, "Success, vaccination", "gentestcase01",
            TestExpectedResults(
                verifySchemaGeneration = true,
                verifyEncodeGeneration = true,
            )
        )
    }

    @Test
    fun writeGen02Recovery() {
        val eudgc = ObjectMapper().readValue(SampleData.recovery, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createGenerationTestCaseJson(
            clock, eudgc, result, "Success, recovery", "gentestcase02",
            TestExpectedResults(
                verifySchemaGeneration = true,
                verifyEncodeGeneration = true,
            )
        )
    }

    @Test
    fun writeGen03TestNaa() {
        val eudgc = ObjectMapper().readValue(SampleData.testNaa, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createGenerationTestCaseJson(
            clock, eudgc, result, "Success, test NAA", "gentestcase03",
            TestExpectedResults(
                verifySchemaGeneration = true,
                verifyEncodeGeneration = true,
            )
        )
    }

    @Test
    fun writeGen04TestRat() {
        val eudgc = ObjectMapper().readValue(SampleData.testRat, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createGenerationTestCaseJson(
            clock, eudgc, result, "Success, test RAT", "gentestcase04",
            TestExpectedResults(
                verifySchemaGeneration = true,
                verifyEncodeGeneration = true,
            )
        )
    }

    @Test
    fun write01Good() {
        val eudgc = ObjectMapper().readValue(SampleData.vaccination, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createVerificationTestCaseJson(
            clock, eudgc, result, cryptoService.getCertificate(),
            "All good", "testcase01",
            TestExpectedResults(
                verifyQrDecode = true,
                verifyPrefix = true,
                verifyBase45Decode = true,
                verifyCoseSignature = true,
                verifyCborDecode = true,
                verifyJson = true,
                verifySchemaValidation = true,
                expired = false
            )
        )
    }

    @Test
    fun write02SignatureFailed() {
        val eudgc = ObjectMapper().readValue(SampleData.vaccination, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(RandomEcKeyCryptoService(clock = clock)),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createVerificationTestCaseJson(
            clock, eudgc, result, cryptoService.getCertificate(),
            "Signature cert not in trust list", "testcase02",
            TestExpectedResults(
                verifyQrDecode = true,
                verifyPrefix = true,
                verifyBase45Decode = true,
                verifyCoseSignature = false,
                verifyCborDecode = true,
                verifyJson = true,
                verifySchemaValidation = true,
                expired = false
            )
        )
    }

    @Test
    fun write03Expired() {
        val eudgc = ObjectMapper().readValue(SampleData.vaccination, Eudgc::class.java)
        val clock = Clock.fixed(Instant.parse("2018-05-03T18:00:00Z"), ZoneId.systemDefault())
        val cryptoService = RandomEcKeyCryptoService(clock = clock)
        val chain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(cryptoService),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )
        val result = chain.encode(eudgc)

        createVerificationTestCaseJson(
            Clock.fixed(Instant.parse("2021-05-03T18:00:00Z"), ZoneId.systemDefault()),
            eudgc, result, cryptoService.getCertificate(),
            "Certificate expired", "testcase03",
            TestExpectedResults(
                verifyQrDecode = true,
                verifyPrefix = true,
                verifyBase45Decode = true,
                verifyCoseSignature = true,
                verifyCborDecode = true,
                verifyJson = true,
                verifySchemaValidation = true,
                expired = true
            )
        )
    }

    private fun createVerificationTestCaseJson(
        clock: Clock,
        eudgc: Eudgc,
        result: ChainResult,
        certificate: X509Certificate,
        description: String,
        testcaseNumber: String,
        expectedResult: TestExpectedResults
    ) {
        val context = TestContext(
            1,
            "1.0.0",
            certificate,
            OffsetDateTime.ofInstant(clock.instant(), clock.zone),
            description
        )
        val qrCode = DefaultTwoDimCodeService(350).encode(result.step5Prefixed).asBase64()
        val testcase = TestCase(
            eudgc,
            result.step1Cbor.toHexString(),
            result.step2Cose.toHexString(),
            result.step4Encoded,
            result.step5Prefixed,
            qrCode,
            context,
            expectedResult
        )
        File("src/test/resources/$testcaseNumber.json").bufferedWriter().use {
            it.write(Json { prettyPrint = true }.encodeToString(testcase))
        }
    }

    private fun createGenerationTestCaseJson(
        clock: Clock,
        eudgc: Eudgc,
        result: ChainResult,
        description: String,
        testcaseNumber: String,
        expectedResult: TestExpectedResults
    ) {
        val context = TestContext(
            1,
            "1.0.0",
            null,
            OffsetDateTime.ofInstant(clock.instant(), clock.zone),
            description
        )
        val testcase = TestCase(
            eudgc,
            result.step1Cbor.toHexString(),
            null,
            null,
            null,
            null,
            context,
            expectedResult
        )
        File("src/test/resources/$testcaseNumber.json").bufferedWriter().use {
            it.write(Json { prettyPrint = true }.encodeToString(testcase))
        }
    }


}
