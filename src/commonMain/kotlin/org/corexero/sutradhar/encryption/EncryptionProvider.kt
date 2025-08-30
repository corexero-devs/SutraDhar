package org.corexero.sutradhar.encryption

fun interface EncryptionProvider {

    fun getEncryptionKey(): String

}