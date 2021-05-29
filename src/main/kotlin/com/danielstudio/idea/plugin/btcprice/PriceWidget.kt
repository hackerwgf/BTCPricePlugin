package com.danielstudio.idea.plugin.btcprice

import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import okhttp3.*
import okio.ByteString
import java.util.*
import javax.swing.JLabel

class PriceWidget : JLabel(), CustomStatusBarWidget {

    companion object {

        private const val WS_URL = "wss://www.b1.cab/ws/v2"

        private const val SYMBOL = "BTC-USDT"
    }

    private val mOkHttpClient = OkHttpClient.Builder().build()

    private val mRequest = Request.Builder().url(WS_URL).build()

    private var mWebSocket: WebSocket? = null

    override fun ID() = "PriceWidget"

    override fun getComponent() = this

    override fun install(statusBar: StatusBar) {
        text = "$SYMBOL --"
        connect()
    }

    private fun connect() {
        mWebSocket?.cancel()
        mOkHttpClient.newWebSocket(mRequest, WidgetWebSocketListener())
    }

    private inner class WidgetWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
            webSocket.send("""{"requestId": "${UUID.randomUUID()}", "subscribeMarketsTickerRequest":{"markets":["$SYMBOL"]}}""")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val jsonString = bytes.utf8()
            if (jsonString.contains("close\":\"")) {
                text = "$SYMBOL ${jsonString.substringAfter("close\":\"").substringBefore("\"")}"
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            try {
                Thread.sleep(5 * 1000)
            } catch (ignore: Exception) {
            }
            connect()
        }
    }

    override fun dispose() {
        mWebSocket?.cancel()
    }
}