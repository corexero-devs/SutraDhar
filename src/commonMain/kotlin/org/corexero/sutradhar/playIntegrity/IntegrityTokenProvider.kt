package org.corexero.sutradhar.playIntegrity

interface IntegrityTokenProvider {
    suspend fun getToken(requestHashB64Url: String): String
}