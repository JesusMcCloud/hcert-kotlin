package ehn.techiop.hcert.kotlin.chain

import ehn.techiop.hcert.kotlin.chain.impl.FileBasedCryptoService
import ehn.techiop.hcert.kotlin.chain.impl.RandomEcKeyCryptoService
import ehn.techiop.hcert.kotlin.chain.impl.RandomRsaKeyCryptoService
import ehn.techiop.hcert.kotlin.crypto.KeyType

internal object CryptoServiceHolder {
    fun getRandomCryptoService(type: KeyType, keySize: Int) =
        when (type) {
            KeyType.EC -> RandomEcKeyCryptoService(keySize)
            else ->
                when (keySize) {
                    2048 -> FileBasedCryptoService("-----BEGIN PRIVATE KEY-----\n" +
                            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCEoba3OQUobgqB\n" +
                            "4bTBnlYgqBP2kxjfmLfyaScYBYDSrEyFE7hIZr1WipyNg5QT2g7KJmdY5EbF8s0M\n" +
                            "l/2Ig0Xwi4RfMyQ+WOuXGFQwHu9dl1YtfNHN8eym4EtmNThbAGZA+pLPMM4+AQb3\n" +
                            "43/q91ADMG+SRgO6Ylr14SV12sYlk+YZDYeEckEfKoCsorTfhl+liBaB1RlOdNwm\n" +
                            "YbwEux9oN2ZlTSoPWYGbsD/vxtjJGtZ71eekTumSznOlG47NU2A3mT9rv+Vs8JbG\n" +
                            "jS27ewVxzqIBGqugwvRryi15fOxCvvN3I/Edso35GyiLkfQcQUfAjcwXaeZQ+Doo\n" +
                            "//9h6sSxAgMBAAECggEAGqnD6Kue3NTaaeftBauGFwSTFtPVgUXbXPFEubCJiFC5\n" +
                            "BVvEhVGaKKau+EgGYcNJi9wSlK03iR3ZmPmJL4NscQIrN1Q5qgsIOZTbf90IM+Fz\n" +
                            "oqtgJi3HbHjUz5RNYwX+iHuXPe3K2G4ub3EdyyeyvyFinJ5Uq4iQTrPXawzzHqSC\n" +
                            "RbsVD0JFbgnjXuvOTQtNIOTbWo2clUgizsBosmsM/odVM0L2Dj75e0rLgF+A9Nd1\n" +
                            "Ii+SDXgyyJMDB8/FAHn4KmfoeEx5td1dDIPZD3ntTqaS+wx9NJGp2PTw4y6O+DHa\n" +
                            "3JjvfpqA/NDzbmysKK7DD+q8R4nsfBLgeuCme05nfQKBgQD7N/XVkexgEtEeAxrS\n" +
                            "oIIOWCbFvB9VZl9ybi4Cxjnj738B75VKHlZ5OzYmlNMrEvDLprEji86fi5UidWvg\n" +
                            "KBkUKY5vR55dGdkAJ0XXNxcejPnw4po46hy71D0d1elZgRhcSNasb+aj8mXiG47L\n" +
                            "ZNX8KN5yVymsDsBH5gcVah1srwKBgQCHJ/MXd2JJvCv3wgyIeqNa1sZynNkMgHwB\n" +
                            "auIHLyjQfnwfn+Y1/wVUshSnuJaoPq0zRl9OYi14bukJeIk2hRIGLLPX7cjF0PaJ\n" +
                            "0dbmiBAZ8lZmM5grCEfHkh1RY+Ssmqga5J/Y3d5DxUQ94Xdm3lcmi1myMcLmGquC\n" +
                            "dj9c4dj8nwKBgAblX1sweUOd4J2pSigz/b31D4NoCZgnikEy4xJybI5kOaFM+VUi\n" +
                            "hg8n3/GpLi7Fg1Sjy8MFCHP6uepLPN3XW/Dgvycw2RkHJ5zIdzNUMM9G4WmKXt9n\n" +
                            "FcjWJ6NVBuXNFGUcHsB3BebENaXCSeYta25TlN+gouU1NnQCzXj6A7rDAoGAT8Vz\n" +
                            "hCExgOWwab49mXwQ2He1j0YmEWvwRQHpwGXESDKvXhcJUEthwRiOemPHgCvmHEJn\n" +
                            "1CK8Rb3oi296RRSLi9tsloDBJIhuSu/wUAZ0wmu3NQE0yglMHG2QIk68VGe/2oeg\n" +
                            "FOb23bcbzQ47ZBrNA3HyEeuu5hNNsWXLhi3C3W8CgYEAhZ4nRYuLWbOSTiS6k5dQ\n" +
                            "fqXeOXnNAuEbYGO+rOSmvPGVCRy/+ExSSlIUtQSBUQp43lR2ByP/sepSK/uPQnH7\n" +
                            "kIJo5qJtkOhL6ynRRDGJK6rKnNkjtXMP1cSy3aFGN6tCZ1yylA7RNDh20XuSIf++\n" +
                            "+Q9tSvimM4TJ7gCSW37uIf8=\n" +
                            "-----END PRIVATE KEY-----\n", "-----BEGIN CERTIFICATE-----\n" +
                            "MIIC5zCCAc+gAwIBAgIEBgcp1zANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZS\n" +
                            "U0EtTWUwHhcNMjEwNTMxMTUzNzExWhcNMjEwNjMwMTUzNzExWjARMQ8wDQYDVQQD\n" +
                            "DAZSU0EtTWUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCEoba3OQUo\n" +
                            "bgqB4bTBnlYgqBP2kxjfmLfyaScYBYDSrEyFE7hIZr1WipyNg5QT2g7KJmdY5EbF\n" +
                            "8s0Ml/2Ig0Xwi4RfMyQ+WOuXGFQwHu9dl1YtfNHN8eym4EtmNThbAGZA+pLPMM4+\n" +
                            "AQb343/q91ADMG+SRgO6Ylr14SV12sYlk+YZDYeEckEfKoCsorTfhl+liBaB1RlO\n" +
                            "dNwmYbwEux9oN2ZlTSoPWYGbsD/vxtjJGtZ71eekTumSznOlG47NU2A3mT9rv+Vs\n" +
                            "8JbGjS27ewVxzqIBGqugwvRryi15fOxCvvN3I/Edso35GyiLkfQcQUfAjcwXaeZQ\n" +
                            "+Doo//9h6sSxAgMBAAGjRzBFMA4GA1UdDwEB/wQEAwIFoDAzBgNVHSUELDAqBgwr\n" +
                            "BgEEAQCON49lAQEGDCsGAQQBAI43j2UBAgYMKwYBBAEAjjePZQEDMA0GCSqGSIb3\n" +
                            "DQEBCwUAA4IBAQBWXGbcDo/SlbYhzQKZvWxhZc6/oyE+pQubZMe0BkQD2Hr52bTZ\n" +
                            "5Yep4CcfXG8AKSzPs2RPG5DnNWhSDTp8MKAubThbFtbTY0kMWEsmt3TwQb+uKbf+\n" +
                            "DKoi0ktyW2rAlIuuhzaMLmYs5G+gXHQISDULTRie0zDnyIRaZ6vxvpXhAWuCiwzu\n" +
                            "4IhaDGKEI11mLE8JjhiCQNywi0VH9zKLIYuHeuI0RRf6PcxoHJNkp96liV7zXRx9\n" +
                            "7ZHTuMdgO8eAeDsGvEotKjSxUOoQhSGS6gnj3dc7oSjSC7i1uSxW1Pa53P1xm0rJ\n" +
                            "TQ0V07hdsexsYfHgEllbNCRcaeu5eVKwQvdN\n" +
                            "-----END CERTIFICATE-----\n")
                    3072 -> FileBasedCryptoService("-----BEGIN PRIVATE KEY-----\n" +
                            "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\n" +
                            "-----END PRIVATE KEY-----", "-----BEGIN CERTIFICATE-----\n" +
                            "MIID0DCCAjqgAwIBAgIEeiJV9DALBgkqhkiG9w0BAQswETEPMA0GA1UEAxMGUlNB\n" +
                            "LU1lMB4XDTIxMDYxMDIwNTAzOVoXDTIxMDcxMDIwNTAzOVowETEPMA0GA1UEAxMG\n" +
                            "UlNBLU1lMIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEA9g/4AIvMYI+d\n" +
                            "EQwS1Yv1dhyc+4/8dr4oFB3bUC9Fzhz4U4CpyOGsGX9N7B1IKTbnM7orh4XBc7wH\n" +
                            "fmMdG0Rsic5xcGtnG/p9ycQ9Rm6B1zbYlGrfj9UEE3b37ALZFWIQ6mhWWGM7r9ld\n" +
                            "issEXQkS6oO1k1jiOMo1bCaDM0QQYUXRQyRlz9ldvvjHoR8k/vAWifhKmzlV5J7c\n" +
                            "Cnzv9RahmoVojlRTAoEwn+/ZtXozoqASqamikRHNSs7brQxG+cKmJPes4huQz3zX\n" +
                            "BbJo8+w+sWgiDupB4eTobH7YKnI6tq1r4AswH2E+KGToiEytyJAXP8Mmg6Ukfa0A\n" +
                            "jPUOpwry7EkYFbO8On2N0oxE3GSleWV/MKbFlelEMylncozfi9nrctiHA9tDtaNj\n" +
                            "uuvF2Q0zI8LTEXKD5ZnssJMKTlWyHrmRCUfvvk+uv+u9ajGPdqxnmR+TpKokK0Ra\n" +
                            "8sdkCOuIM6RVNKO9pPc79OLTtHw9X3C6mipJkBLidP6az2sL+r41AgMBAAGjNDAy\n" +
                            "MDAGA1UdJQQpMCcGCysGAQQBjjePZQEBBgsrBgEEAY43j2UBAgYLKwYBBAGON49l\n" +
                            "AQMwCwYJKoZIhvcNAQELA4IBgQC8pbmUOOVZl6sgjhwrjMlCpN8dzae7XxBAel8X\n" +
                            "iFY38O2R0sqfSgLc8w/ac0G5jxlQR/ukAsedwVkO08dwfPmQnUBpc/QcCAB1sn2F\n" +
                            "9J/fWRBdLYJ3NYX9THH32cRmZVw/Cu93DLd12NsKfAGEGSuRdGwzgE2k1jKXZKcr\n" +
                            "xLfUGrKBFvnt7qw4AY6cmghD4a/8F29EGXsOqS9bOvWdMQ7nsaOgPmw7stLyNg/N\n" +
                            "OreXhGSqgCW4Gcm5Oi17DCE0cPV+i6rrJRLi12nISgOQAbEoOIu3XOVx330qJmID\n" +
                            "aOgrtTHSPHbRVivO6T35GMqKDhyYDfWpRQxYnry+68y1zlOYO7+8/H7kodL1cor7\n" +
                            "gwrxFc6r1vDgeQ8lvf8+WKcuUpMoObYE9fny2kBmENAunq39S5YhiNAsif5pX/xA\n" +
                            "rzfLBhbr3Xgawmtdz3zzY0bwR/8NnTjdzXXS8IfIpd8fNVdnIQVF4DppU0NwHX/w\n" +
                            "WYlrdugk0FcZTqRXxesN/0xs/Ns=\n" +
                            "-----END CERTIFICATE-----\n")
                    else -> throw IllegalArgumentException("Unsupported Params: $type$keySize")
                }
        }
}