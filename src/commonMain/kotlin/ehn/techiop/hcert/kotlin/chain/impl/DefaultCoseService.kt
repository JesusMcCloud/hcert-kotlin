package ehn.techiop.hcert.kotlin.chain.impl

import ehn.techiop.hcert.kotlin.chain.CoseService
import ehn.techiop.hcert.kotlin.chain.CryptoService
import ehn.techiop.hcert.kotlin.chain.Error
import ehn.techiop.hcert.kotlin.chain.VerificationResult
import ehn.techiop.hcert.kotlin.crypto.CoseHeaderKeys
import ehn.techiop.hcert.kotlin.trust.CoseAdapter
import ehn.techiop.hcert.kotlin.trust.CoseCreationAdapter


/**
 * Encodes/decodes input as a Sign1Message according to COSE specification (RFC8152)
 */
open class DefaultCoseService(private val cryptoService: CryptoService) : CoseService {

    override fun encode(input: ByteArray): ByteArray {
        val coseAdapter = CoseCreationAdapter(input)
        cryptoService.getCborHeaders().forEach {
            coseAdapter.addProtectedAttribute(it.first, it.second)
        }
        coseAdapter.sign(cryptoService.getCborSigningKey())
        return coseAdapter.encode()
    }

    override fun decode(input: ByteArray, verificationResult: VerificationResult): ByteArray {
        try {
            val coseAdapter = CoseAdapter(input)
            val kid = coseAdapter.getProtectedAttributeByteArray(CoseHeaderKeys.KID.intVal)
                ?: coseAdapter.getUnprotectedAttributeByteArray(CoseHeaderKeys.KID.intVal)
                ?: throw IllegalArgumentException("KID not found").also {
                    verificationResult.error = Error.KEY_NOT_IN_TRUST_LIST
                }
            //val algorithm = coseAdapter.getProtectedAttributeInt(CoseHeaderKeys.Algorithm.value)
            // TODO is the algorithm relevant?
            if (!coseAdapter.validate(kid, cryptoService, verificationResult)) {
                throw IllegalArgumentException("Not validated").also {
                    verificationResult.error = Error.SIGNATURE_INVALID
                }
            }
            return coseAdapter.getContent()
        } catch (e: Throwable) {
            throw e.also {
                verificationResult.error = Error.SIGNATURE_INVALID
            }
        }
    }

}
