package org.corexero.sutradhar.deeplink

import android.net.Uri
import org.corexero.sutradhar.deeplink.DeeplinkParser.ACTION
import org.corexero.sutradhar.deeplink.DeeplinkParser.DEST_ID
import org.corexero.sutradhar.deeplink.DeeplinkParser.MSG
import org.corexero.sutradhar.deeplink.DeeplinkParser.PKG
import org.corexero.sutradhar.deeplink.DeeplinkParser.SOURCE_ID
import org.corexero.sutradhar.deeplink.DeeplinkParser.URL

object DeeplinkParser {
    const val ACTION = "action"
    const val PKG = "pkg"
    const val URL = "url"
    const val MSG = "msg"
    const val SOURCE_ID = "sourceId"
    const val DEST_ID = "destId"
}

data class DeepLinkCommand(
    val action: DeepLinkAction,
    val title: String? = null,
    val message: String? = null,
    val pkg: String? = null,          // for TRIAL_REDIRECT_APP / REVIEW_PLAYSTORE (other app)
    val url: String? = null,          // for OPEN_CUSTOM_TAB
    val sourceId: String? = null,     // for OPEN_ROUTE
    val destId: String? = null        // for OPEN_ROUTE
)


fun parseDeepLink(uri: Uri?): DeepLinkCommand {
    if (uri == null) return DeepLinkCommand(DeepLinkAction.UNKNOWN)

    val rawAction =
        uri.getQueryParameter(ACTION)
            ?: uri.pathSegments.firstOrNull()
            ?: uri.host
            ?: uri.lastPathSegment
            ?: ""

    val action = DeepLinkAction.from(rawAction)

    return when (action) {
        DeepLinkAction.REVIEW_PLAYSTORE ->
            DeepLinkCommand(action, pkg = uri.getQueryParameter(PKG))

        DeepLinkAction.REVIEW_INAPP ->
            DeepLinkCommand(action)

        DeepLinkAction.TRIAL_REDIRECT_APP ->
            DeepLinkCommand(action, pkg = uri.getQueryParameter(PKG))

        DeepLinkAction.FUNNY_MESSAGE ->
            DeepLinkCommand(action, message = uri.getQueryParameter(MSG))

        DeepLinkAction.OPEN_CUSTOM_TAB ->
            DeepLinkCommand(action, url = uri.getQueryParameter(URL))

        DeepLinkAction.OPEN_ROUTE ->
            DeepLinkCommand(
                action,
                sourceId = uri.getQueryParameter(SOURCE_ID),
                destId = uri.getQueryParameter(DEST_ID)
            )

        DeepLinkAction.UNKNOWN ->
            DeepLinkCommand(action)
    }
}
