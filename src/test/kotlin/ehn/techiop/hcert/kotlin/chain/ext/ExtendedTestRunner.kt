package ehn.techiop.hcert.kotlin.chain.ext

import ehn.techiop.hcert.kotlin.chain.Chain
import ehn.techiop.hcert.kotlin.chain.DecisionService
import ehn.techiop.hcert.kotlin.chain.VerificationDecision
import ehn.techiop.hcert.kotlin.chain.VerificationResult
import ehn.techiop.hcert.kotlin.chain.fromBase64
import ehn.techiop.hcert.kotlin.chain.impl.DefaultBase45Service
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCborService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCompressorService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultContextIdentifierService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCoseService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultTwoDimCodeService
import ehn.techiop.hcert.kotlin.chain.impl.PrefilledCertificateRepository
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import ehn.techiop.hcert.kotlin.chain.toHexString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.time.Clock
import java.time.ZoneId

class ExtendedTestRunner {

    @ParameterizedTest
    @MethodSource("verificationProvider")
    fun verification(case: TestCase) {
        println("Executing verification test case ${case.context.description}")
        val clock = case.context.validationClock?.let {
            Clock.fixed(it.toInstant(), ZoneId.systemDefault())
        } ?: Clock.systemDefaultZone()
        val decisionService = DecisionService(clock)
        if (case.context.certificate == null) throw IllegalArgumentException("certificate")
        val certificateRepository = PrefilledCertificateRepository(case.context.certificate)
        val verificationResult = VerificationResult()
        val decodingChain = Chain.buildVerificationChain(certificateRepository)
        val qrCodeContent = if (case.qrCodePng != null) {
            DefaultTwoDimCodeService(350).decode(case.qrCodePng.fromBase64())
        } else {
            case.base45WithPrefix
        } ?: throw IllegalArgumentException("Input")

        val chainResult = decodingChain.decodeExtended(qrCodeContent, verificationResult)
        val decision = decisionService.decide(verificationResult)

        case.expectedResult.verifyQrDecode?.let {
            assertThat(qrCodeContent, equalTo(case.base45WithPrefix))
        }
        case.expectedResult.verifyPrefix?.let {
            assertThat(chainResult.step4Encoded, equalTo(case.base45))
        }
        case.expectedResult.verifyBase45Decode?.let {
            assertThat(verificationResult.base45Decoded, equalTo(it))
            assertThat(chainResult.step2Cose.toHexString(), equalTo(case.coseHex))
        }
        case.expectedResult.verifyCoseSignature?.let {
            assertThat(verificationResult.coseVerified, equalTo(it))
            if (!it) assertThat(decision, equalTo(VerificationDecision.FAIL))
        }
        case.expectedResult.verifyCborDecode?.let {
            assertThat(chainResult.step1Cbor.toHexString(), equalTo(case.cborHex))
            assertThat(verificationResult.cborDecoded, equalTo(it))
        }
        case.expectedResult.verifyJson?.let {
            assertThat(chainResult.eudgc, equalTo(case.eudgc))
        }
        // TODO Implement schema validation
        case.expectedResult.verifySchemaValidation?.let {
            assertThat(verificationResult.cborDecoded, equalTo(it))
        }
        case.expectedResult.expired?.let {
            if (it) assertThat(decision, equalTo(VerificationDecision.FAIL))
        }
    }

    @ParameterizedTest
    @MethodSource("generationProvider")
    fun generation(case: TestCase) {
        println("Executing generation test case ${case.context.description}")
        if (case.eudgc == null) throw IllegalArgumentException("eudgc")
        val clock = case.context.validationClock?.let {
            Clock.fixed(it.toInstant(), ZoneId.systemDefault())
        } ?: Clock.systemDefaultZone()
        val creationChain = Chain(
            DefaultCborService(clock = clock),
            DefaultCoseService(RandomEcKeyCryptoService(clock = clock)),
            DefaultContextIdentifierService(),
            DefaultCompressorService(),
            DefaultBase45Service()
        )

        val chainResult = creationChain.encode(case.eudgc)

        case.expectedResult.verifySchemaGeneration?.let {
            // TODO Implement schema verification
        }
        case.expectedResult.verifyEncodeGeneration?.let {
            assertThat(chainResult.step1Cbor.toHexString(), equalTo(case.cborHex))
        }
    }

    companion object {

        @JvmStatic
        @Suppress("unused")
        fun verificationProvider(): List<TestCase> {
            val testcaseFiles = listOf(
                "src/test/resources/testcase01.json",
                "src/test/resources/testcase02.json",
                "src/test/resources/testcase03.json",
            )
            return testcaseFiles.map { Json.decodeFromString(File(it).bufferedReader().readText()) }
        }

        @JvmStatic
        @Suppress("unused")
        fun generationProvider(): List<TestCase> {
            val testcaseFiles = listOf(
                "src/test/resources/gentestcase01.json",
                "src/test/resources/gentestcase02.json",
                "src/test/resources/gentestcase03.json",
                "src/test/resources/gentestcase04.json",
            )
            return testcaseFiles.map { Json.decodeFromString(File(it).bufferedReader().readText()) }
        }

    }

}
