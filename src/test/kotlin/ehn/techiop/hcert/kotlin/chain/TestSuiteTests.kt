package ehn.techiop.hcert.kotlin.chain

import ehn.techiop.hcert.kotlin.chain.faults.FaultyBase45Service
import ehn.techiop.hcert.kotlin.chain.faults.FaultyCborService
import ehn.techiop.hcert.kotlin.chain.faults.FaultyCompressorService
import ehn.techiop.hcert.kotlin.chain.faults.FaultyCoseService
import ehn.techiop.hcert.kotlin.chain.faults.FaultyValSuiteService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultBase45Service
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCborService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCompressorService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultCoseService
import ehn.techiop.hcert.kotlin.chain.impl.DefaultValSuiteService
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class TestSuiteTests {

    private val cryptoService = RandomEcKeyCryptoService()
    private val cborService = DefaultCborService()
    private val coseService = DefaultCoseService(cryptoService)
    private val valSuiteService = DefaultValSuiteService()
    private val compressorService = DefaultCompressorService()
    private val base45Service = DefaultBase45Service()
    private val chainCorrect =
        CborProcessingChain(cborService, coseService, valSuiteService, compressorService, base45Service)
    private val chainFaultyBase45 =
        CborProcessingChain(cborService, coseService, valSuiteService, compressorService, FaultyBase45Service())
    private val chainFaultyCompressor =
        CborProcessingChain(cborService, coseService, valSuiteService, FaultyCompressorService(), base45Service)
    private val chainFaultyValSuite =
        CborProcessingChain(cborService, coseService, FaultyValSuiteService(), compressorService, base45Service)
    private val chainFaultyCose =
        CborProcessingChain(
            cborService,
            FaultyCoseService(cryptoService),
            valSuiteService,
            compressorService,
            base45Service
        )
    private val chainFaultyCbor =
        CborProcessingChain(FaultyCborService(), coseService, valSuiteService, compressorService, base45Service)


    @Test
    fun vaccination() {
        val input = SampleData.vaccination
        val decodedFromInput =
            Json { isLenient = true; ignoreUnknownKeys = true }.decodeFromString<VaccinationData>(input)

        assertVerification(
            chainCorrect.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            true,
            VerificationResult().apply {
                valSuitePrefix = "HC1"; base45Decoded = true; zlibDecoded = true; cborDecoded = true; coseVerified =
                true
            })
        assertVerification(
            chainFaultyBase45.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            false,
            VerificationResult().apply { valSuitePrefix = "HC1" })
        assertVerification(
            chainFaultyValSuite.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            true,
            VerificationResult().apply {
                valSuitePrefix = null; base45Decoded = true; zlibDecoded = true; cborDecoded = true; coseVerified = true
            })
        assertVerification(
            chainFaultyCompressor.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            true,
            VerificationResult().apply {
                valSuitePrefix = "HC1"; base45Decoded = true; zlibDecoded = true; cborDecoded = true; coseVerified =
                true
            })
        assertVerification(
            chainFaultyCose.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            true,
            VerificationResult().apply {
                valSuitePrefix = "HC1"; base45Decoded = true; zlibDecoded = true; cborDecoded = true
            })
        assertVerification(
            chainFaultyCbor.process(decodedFromInput).prefixedEncodedCompressedCose,
            decodedFromInput,
            false,
            VerificationResult().apply {
                valSuitePrefix = "HC1"; base45Decoded = true; zlibDecoded = true; coseVerified = true
            })
    }

    private fun assertVerification(
        chainOutput: String,
        input: VaccinationData,
        expectDataToMatch: Boolean,
        expectedResult: VerificationResult
    ) {
        val verificationResult = VerificationResult()
        val vaccinationData = chainCorrect.verify(chainOutput, verificationResult)
        assertThat(verificationResult.base45Decoded, equalTo(expectedResult.base45Decoded))
        assertThat(verificationResult.cborDecoded, equalTo(expectedResult.cborDecoded))
        assertThat(verificationResult.coseVerified, equalTo(expectedResult.coseVerified))
        assertThat(verificationResult.zlibDecoded, equalTo(expectedResult.zlibDecoded))
        assertThat(verificationResult.valSuitePrefix, equalTo(expectedResult.valSuitePrefix))
        if (expectDataToMatch) {
            assertThat(vaccinationData, equalTo(input))
        } else {
            assertThat(vaccinationData, not(equalTo(input)))
        }
    }

}