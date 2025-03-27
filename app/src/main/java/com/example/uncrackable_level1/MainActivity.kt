package com.example.uncrackable_level1

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.view.WindowManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uncrackable_level1.ui.theme.UnCrackableLevel1Theme
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnCrackableLevel1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Pentester",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val view = LinearLayout(this)
        view.layoutParams =
            RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )

        class MyWebViewClient : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        }

        val webView = WebView(this)
        webView.webViewClient = MyWebViewClient()
        webView.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        view.addView(webView)
        webView.loadUrl("https://raw.githubusercontent.com/OWASP/owasp-masvs/7b05d0fabfa35dfde991e4bb90bcc8c77dfa65b4/README.md")
        windowManager.addView(view, null)
    }
}

fun Builder(): OkHttpClient.Builder {
    try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                // N / A
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                // N / A
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        // builder.hostnameVerifier { _, _ -> true }
        val ALLOW_ALL_HOSTNAME_VERIFIER = HostnameVerifier { hostname, session -> true }
        builder.hostnameVerifier(ALLOW_ALL_HOSTNAME_VERIFIER)

        return builder
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val content: String
    val masvs: String
        = "https://raw.githubusercontent.com/OWASP/owasp-masvs/7b05d0fabfa35dfde991e4bb90bcc8c77dfa65b4/README.md"
    val client = Builder().build();
    val request = Request.Builder()
        .url(masvs).build()
    client.newCall(request).execute().use { response ->
        content = response.body!!.string()
    }
    Text(
        text = "Hello $name!\nHere are the old 1.4 MSTG req.\n".plus(content),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnCrackableLevel1Theme {
        Greeting("Android")
    }
}