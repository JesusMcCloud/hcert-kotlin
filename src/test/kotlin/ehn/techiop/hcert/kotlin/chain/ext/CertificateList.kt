@file:UseSerializers(serializerClasses = [X509CertificateSerializer::class])

package ehn.techiop.hcert.kotlin.chain.ext

import ehn.techiop.hcert.kotlin.data.X509CertificateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.security.cert.X509Certificate

@Serializable
data class CertificateList(
    @SerialName("x5c")
    val certificates: List<X509Certificate>
)