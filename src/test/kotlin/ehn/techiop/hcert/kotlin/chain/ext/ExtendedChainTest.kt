package ehn.techiop.hcert.kotlin.chain.ext

import ehn.techiop.hcert.kotlin.chain.Chain
import ehn.techiop.hcert.kotlin.chain.DecisionService
import ehn.techiop.hcert.kotlin.chain.VerificationResult
import ehn.techiop.hcert.kotlin.chain.impl.PrefilledCertificateRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.time.Clock
import java.time.ZoneId

class ExtendedChainTest {

    @ParameterizedTest
    @MethodSource("inputProvider")
    fun success(input: TestInput) {
        val decisionService =
            DecisionService(Clock.fixed(input.context.validationClock.toInstant(), ZoneId.systemDefault()))
        val certificateRepository = PrefilledCertificateRepository(*input.context.certList.certificates.toTypedArray())

        val verificationResult = VerificationResult()
        val decodingChain = Chain.buildVerificationChain(certificateRepository)
        if (input.withPrefix == null) throw IllegalArgumentException("PREFIX")
        val chainResult = decodingChain.decodeExtended(input.withPrefix, verificationResult)
        val decision = decisionService.decide(verificationResult)

        // creation input.context.expectedResult.validObject?.let { assertThat(verificationResult.cborDecoded, equalTo(it)) }
        input.context.expectedResult.schemaValidation?.let { assertThat(verificationResult.cborDecoded, equalTo(it)) }
        //creation input.context.expectedResult.encode?.let { assertThat(verificationResult.cborDecoded, equalTo(it)) }
        input.context.expectedResult.decode?.let { assertThat(verificationResult.cborDecoded, equalTo(it)) }
        //creation input.context.expectedResult.sign?.let { assertThat(verificationResult.coseVerified, equalTo(it)) }
        input.context.expectedResult.verify?.let { assertThat(verificationResult.coseVerified, equalTo(it)) }
        input.context.expectedResult.unprefix?.let { assertThat(chainResult.step4Encoded, equalTo(input.base45)) }
        input.context.expectedResult.validJson?.let { assertThat(chainResult.eudgc, equalTo(input.content)) }
        input.context.expectedResult.base45decode?.let { assertThat(chainResult.step3Compressed, equalTo(input.cose)) }
        //input.context.expectedResult.pictureDecode?.let { assertThat(chainResult.step4Encoded, equalTo(input.withPrefix)) }
        input.context.expectedResult.expired?.let { assertThat(chainResult.step4Encoded, equalTo(input.base45)) }
    }

    companion object {

        @JvmStatic
        @Suppress("unused")
        fun inputProvider(): List<TestInput> {
            val testcaseFiles = listOf(
                "src/test/resources/testcase01.json"
            )
            return testcaseFiles.map { Json.decodeFromString(File(it).bufferedReader().readText()) }
        }

    }

}
