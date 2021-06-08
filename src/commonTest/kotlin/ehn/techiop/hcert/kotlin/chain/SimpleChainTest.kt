package ehn.techiop.hcert.kotlin.chain

import ehn.techiop.hcert.kotlin.chain.impl.PrefilledCertificateRepository
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import ehn.techiop.hcert.kotlin.data.GreenCertificate
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SimpleChainTest : StringSpec({

    "goodVaccinationEc256" {
        val service = RandomEcKeyCryptoService(256)
        verify(SampleData.vaccination, service, true)
    }
})

private fun verify(jsonInput: String, cryptoService: CryptoService, outcome: Boolean) {
    val input = Json.decodeFromString<GreenCertificate>(jsonInput)

    val encodingChain = DefaultChain.buildCreationChain(cryptoService)
    val certificateRepository = PrefilledCertificateRepository(cryptoService.getCertificate())
    val decodingChain = DefaultChain.buildVerificationChain(certificateRepository)

    val output = encodingChain.encode(input)

    val result = decodingChain.decode(output.step5Prefixed)
    (result.verificationResult.error == null) shouldBe outcome
    if (outcome) // our chain exits early on an error
        result.chainDecodeResult.eudgc shouldBe input
}
