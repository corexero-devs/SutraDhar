package org.corexero.sutradhar.security

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest

object KeystoreChecker {

    init {
        System.loadLibrary("native-lib")
    }

    // From NATIVE: allowed fingerprints (debug vs release auto-chosen by native)
    @JvmStatic
    external fun getSignerSha256(): Array<String>

    /** True if current app signer matches any allowed fingerprint from native. */
    @JvmStatic
    fun isSelfSignedWithTrustedKey(ctx: Context): Boolean {
        val allowed = runCatching { getSignerSha256().toSet() }.getOrDefault(emptySet())
        if (allowed.isEmpty()) return false

        val current = getSelfSignerSha256List(ctx)
        return current.any { it in allowed }
    }

    /** Runtime signer fingerprints via PackageManager (UPPERCASE, no colons). */
    @JvmStatic
    fun getSelfSignerSha256List(ctx: Context): List<String> {
        val sigs = getOwnSignatures(ctx) ?: return emptyList()
        return sigs.map { sha256HexUpper(it.toByteArray()) }.distinct()
    }

    // ---- internals ----
    private fun getOwnSignatures(ctx: Context): Array<Signature>? {
        val pm = ctx.packageManager
        val pkg = ctx.packageName
        val pi: PackageInfo = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                pm.getPackageInfo(
                    pkg,
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
                )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ->
                @Suppress("DEPRECATION") pm.getPackageInfo(
                    pkg,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )

            else ->
                @Suppress("DEPRECATION") pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val si = pi.signingInfo ?: return null
            if (si.hasMultipleSigners()) si.apkContentsSigners else si.signingCertificateHistory
        } else {
            @Suppress("DEPRECATION") pi.signatures
        }
    }

    private fun sha256HexUpper(data: ByteArray): String {
        val d = MessageDigest.getInstance("SHA-256").digest(data)
        return d.joinToString("") { "%02X".format(it) }
    }
}
