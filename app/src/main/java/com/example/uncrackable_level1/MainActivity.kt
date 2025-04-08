package com.example.uncrackable_level1

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uncrackable_level1.ui.theme.UnCrackableLevel1Theme
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class MainActivity : ComponentActivity() {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Log.i(null, "Set content")

        setContent {
            UnCrackableLevel1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CallInsecureServers()
                }
            }
        }

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val view = LinearLayout(this)
        view.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

        class MyWebViewClient : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                var message = "SSL Certificate error."
                when (error.getPrimaryError()) {
                    SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
                    SslError.SSL_EXPIRED -> message = "The certificate has expired."
                    SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
                    SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
                    SslError.SSL_DATE_INVALID -> message = "The date of the certificate is invalid"
                }
                Log.w(null, "SSL errors onReceivedSslError: ".plus(message))
                Log.w(null, error.toString())

                handler.proceed()
            }
        }

        var webView = WebView(this)
        webView.webViewClient = MyWebViewClient()
        var params = WindowManager.LayoutParams()
        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100
        view.addView(webView)
        webView.loadUrl("https://tlsexpired.no")
        //webView.loadUrl("https://tlsrevocation.org")
        //webView.loadUrl("https://tlsrevoked.no")
        //webView.loadUrl("https://tlsbadsubjectaltname.no")
        windowManager.addView(view, params)

    }


}

fun Builder(): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
    try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                // N / A
                Log.w(null, "Client not trusted")
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                // N / A
                Log.w(null, "Server not trusted")
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                Log.w(null, "All issuers accepted")
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory


        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        val allowallhostnameverifier = HostnameVerifier { hostname, session ->
            Log.w(null, "Do not verify host, allow: ".plus(hostname))
            true
        }
        builder.hostnameVerifier(allowallhostnameverifier)

    } catch (e: Exception) {
        Log.w(null, e)
    }
    return builder
}

@Composable
fun CallInsecureServers(modifier: Modifier = Modifier) {
    val content: String
    val tlsExpired: String
    val tlsRevoked: String
    val tlsBadSubjectAltName: String

    val tlsRevocationRequest = Request.Builder()
        .header("User-Agent", "COOL APP 9000")
        //.connectTimeout(10, TimeUnit.MILLISECONDS)
        .url("https://tlsrevocation.org").build()
    Builder().connectTimeout(0, TimeUnit.MILLISECONDS).build().newCall(tlsRevocationRequest).execute().use { response ->
        content = response.body!!.string()
    }

    val tlsExpiredRequest = Request.Builder()
        .header("User-Agent", "COOL APP 9000")
        //.connectTimeout(10, TimeUnit.MILLISECONDS)
        .url("https://tlsexpired.no").build()
    Builder().connectTimeout(0, TimeUnit.MILLISECONDS).build().newCall(tlsExpiredRequest).execute().use { response ->
        Log.w(null, "Call tlsexpired.no")
        tlsExpired = response.body!!.string()
    }

    val tlsRevokedRequest = Request.Builder()
        .header("User-Agent", "COOL APP 9000")
        //.connectTimeout(10, TimeUnit.MILLISECONDS)
        .url("https://tlsrevoked.no").build()
    Builder().connectTimeout(0, TimeUnit.MILLISECONDS).build().newCall(tlsRevokedRequest).execute().use { response ->
        Log.w(null, "Call tlsrevoked.no")
        tlsRevoked = response.body!!.string()
    }

    val tlsBadSubjectAltNamerequest = Request.Builder()
        .header("User-Agent", "COOL APP 9000")
        //.connectTimeout(10, TimeUnit.MILLISECONDS)
        .url("https://tlsbadsubjectaltname.no").build()
    Builder().connectTimeout(0, TimeUnit.MILLISECONDS).build().newCall(tlsBadSubjectAltNamerequest).execute().use { response ->
        Log.w(null, "Call tlsbadsubjectaltname.no")
        tlsBadSubjectAltName = response.body!!.string()
    }

    Surface(color = Color.Cyan) {
        Text(
            text = content.plus("\n\n")
                .plus(tlsExpired).plus("\n\n")
                .plus(tlsRevoked).plus("\n\n")
                .plus(tlsBadSubjectAltName),
            modifier = modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Log.i(null, "Set preview")
    UnCrackableLevel1Theme {
        CallInsecureServers()
    }
}