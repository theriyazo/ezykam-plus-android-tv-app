package com.theriyazo.shopcamera

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
/**
 * Loads a grid of cards with movies to browse.
 */

class MainFragment : Fragment() {
    private val webUrl = "https://web.ezykam.com/playback"

    private val TAG = "MainFragment"
    private lateinit var webView: WebView
    private lateinit var cursorView: ImageView
    private var cursorX = 100f
    private var cursorY = 100f
    private val moveStep = 30f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "onCreateView started")
        val rootView = inflater.inflate(R.layout.fragment_webview, container, false)
        webView = rootView.findViewById(R.id.webview)

        cursorView = rootView.findViewById(R.id.cursorView)
        moveCursorTo(cursorX, cursorY)

        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener { _, keyCode, event ->
            Log.d(TAG, "Key pressed: $keyCode")
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> moveCursorBy(0f, -moveStep)
                    KeyEvent.KEYCODE_DPAD_DOWN -> moveCursorBy(0f, moveStep)
                    KeyEvent.KEYCODE_DPAD_LEFT -> moveCursorBy(-moveStep, 0f)
                    KeyEvent.KEYCODE_DPAD_RIGHT -> moveCursorBy(moveStep, 0f)
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        simulateClickAtCursor()
                        restoreRootViewFocus()
                    }
                    else -> return@setOnKeyListener false
                }
                true
            } else {
                false
            }
        }

        val reloadButton: Button = rootView.findViewById(R.id.reloadTheScripts)
        reloadButton.setOnClickListener {
            Log.d(TAG, "🔄 reloadTheScripts button clicked")
            // Start the automation chain again manually
            webView.postDelayed({
                autoClickRoomTitle()
            }, 500)
        }
        val fullScreenButton: Button = rootView.findViewById(R.id.enterFullScreen)
        fullScreenButton.setOnClickListener {
            Log.d(TAG, "🔄 fullScreenButton clicked")
            // Start the automation chain again manually
            stimulateHoverOnFirstDiv()
            webView.postDelayed({
//                clickSeventhSvgWrapper()
                stimulateSingleCameraFullScreen()
            }, 500)

            webView.postDelayed({
                stimulateClickOnFullscreenBox()
            }, 800)
        }



        Log.d(TAG, "WebView found: $webView")


        webView.setOnTouchListener { _, _ -> false } // Let others handle the touch

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d(TAG, "Page finished loading: $url")

//                // Inject polling JS to wait until the DOM has the element
//                webView.evaluateJavascript(
//                    """
//            (function waitForClass(callback) {
//                function check() {
//                    if (document.querySelector('.login_login__5ghNx')) {
//                        callback(true);
//                    } else {
//                        setTimeout(check, 500);
//                    }
//                }
//                check();
//            })(function(found) {
//                console.log("Class check complete: " + found);
//            });
//            """
//                ) {
//                    Log.d(TAG, "✅ Class was eventually found in the DOM!")
//                }

                // If we are on the /playback page, start polling for target element and click
                if (url?.contains("/playback") == true) {
                    Log.d(TAG, "url contains playback")
                    reloadButton.visibility = View.VISIBLE
                    fullScreenButton.visibility = View.VISIBLE

//                    webView.postDelayed({
////                        autoClickRoomTitle()
//                    }, 2000) // wait 2 seconds for SPA to render
                } else {
                    reloadButton.visibility = View.GONE
                    fullScreenButton.visibility = View.GONE
                }



            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?,
            ) {
                Log.e(TAG, "Error loading page $failingUrl: $description")
            }
        }

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false

        webView.loadUrl(webUrl)
        Log.d(TAG, "WebView loading URL: $webUrl")

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isFocusableInTouchMode = true
        view.requestFocus()

        view.setOnKeyListener { _, keyCode, event ->
            Log.d(TAG, "Key pressed: $keyCode")
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> moveCursorBy(0f, -moveStep)
                    KeyEvent.KEYCODE_DPAD_DOWN -> moveCursorBy(0f, moveStep)
                    KeyEvent.KEYCODE_DPAD_LEFT -> moveCursorBy(-moveStep, 0f)
                    KeyEvent.KEYCODE_DPAD_RIGHT -> moveCursorBy(moveStep, 0f)
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        simulateClickAtCursor()
                        restoreRootViewFocus()
                    }
                    else -> return@setOnKeyListener false
                }
                true
            } else {
                false
            }
        }

    }

    private fun autoClickRoomTitle() {
        Log.d(TAG, "autoClickRoomTitle() called!")

        webView.evaluateJavascript(
            """
    (function tryClick() {
            const el = document.querySelector('.home_roomTitle__1POai');
            if (el) {
                el.click();
                return "✅ Clicked!";
            }
    })();
    """
        ) { result ->
            Log.d(TAG, "✅ Room title clicked: $result")

            if (!result.isNullOrEmpty() && result.contains("✅ Clicked")) {
                // Add a delay before triggering the next auto click
                webView.postDelayed({
                    autoClickCamera()
                }, 2000) // delay in milliseconds
            } else {
                Log.w(TAG, "⚠️ Room title not clicked or not found: $result")
            }
        }

    }

    private fun autoClickCamera() {
        Log.d(TAG, "autoClickCamera() called!")

        webView.evaluateJavascript(
            """
    (function tryClick() {
            const el = document.querySelector('.deviceItem_box__3ZdcA');
            if (el) {
                el.click();
                return "✅ Clicked!";
            }
    })();
    """
        ) { result ->
            Log.d(TAG, "✅ Camera clicked: $result")

            if (!result.isNullOrEmpty() && result.contains("✅ Clicked")) {
                // Add a delay before triggering the next auto click
                webView.postDelayed({
//                    clickSeventhSvgWrapper()
                }, 15000) // delay in milliseconds
            } else {
                Log.w(TAG, "⚠️ Room title not clicked or not found: $result")
            }
        }
//        { result ->
//            Log.d(TAG, "🔁 JS returned immediately: $result")
//            clickSeventhSvgWrapper()
//        }

    }

    private fun clickSeventhSvgWrapper() {
        Log.d(TAG, "clickSeventhSvgWrapper() called!")
        webView.evaluateJavascript(
            """
        (function clickSeventhElement() {
            const elements = document.querySelectorAll(".SVG_cs-wrapper__3Cu4D");
            if (elements.length >= 8) { // index 7 is the 8th item
                elements[7].click();
                return "✅ Clicked the 8th element (index 7)";
            } else {
                return "❌ Only found " + elements.length + " elements.";
            }
        })();
        """
        ) { result ->
            Log.d(TAG, "🖱 SVG Click Result: $result")

        }
//        { result ->
//            Log.d("MainFragment", "🖱 SVG Click Result: $result")
//        }
    }
    private fun stimulateSingleCameraFullScreen() {
        Log.d(TAG, "stimulateSingleCameraFullScreen() called!")
        webView.evaluateJavascript(
            """
        (function clickSeventhElement() {
            const elements = document.querySelectorAll(".gridSwitch_grid-item__32DzQ");
            if (elements.length >= 6) { // index 7 is the 8th item
                elements[0].click();
                return "✅ Clicked the 1st element";
            } else {
                return "❌ Only found " + elements.length + " elements.";
            }
        })();
        """
        ) { result ->
            Log.d(TAG, "🖱 SVG Click Result: $result")

        }
    }

    private fun stimulateHoverOnFirstDiv() {
        Log.d(TAG, "stimulateHoverOnFirstDiv() called!")
        webView.evaluateJavascript(
            """
        (function simulateHover() {
            const container = document.querySelector('.footer_right__2hoH9');
            if (container) {
                const firstDiv = container.querySelector('div');
                if (firstDiv) {
                    firstDiv.click()
                    const hoverEvent = new MouseEvent('mouseover', {
                        bubbles: true,
                        cancelable: true,
                        view: window
                    });
                    firstDiv.dispatchEvent(hoverEvent);
                    return "✅ Hovered over the first div inside .footer_right__2hoH9";
                } else {
                    return "❌ No child div found inside .footer_right__2hoH9";
                }
            } else {
                return "❌ .footer_right__2hoH9 container not found";
            }
        })();
        """
        ) { result ->
            Log.d(TAG, "🖱 Hover Result: $result")
        }
    }

    private fun stimulateClickOnFullscreenBox() {
        Log.d(TAG, "stimulateClickOnFullscreenBox() called!")
        webView.evaluateJavascript(
            """
        (function simulateClick() {
            const container = document.querySelector('.footer_right__2hoH9');
            if (container) {
                const firstDiv = container.querySelector('.gridFulScreen_gridFulScreen-box__3cm1-');
                if (firstDiv) {
                    firstDiv.click();
                    return "✅ Clicked the fullscreen box inside .footer_right__2hoH9";
                } else {
                    return "❌ No matching child div found inside .footer_right__2hoH9";
                }
            } else {
                return "❌ .footer_right__2hoH9 container not found";
            }
        })();
        """
        ) { result ->
            Log.d(TAG, "🖱 Click Result: $result")
        }
    }

    private fun restoreRootViewFocus() {
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            Log.d(TAG, "✅ Focus re-requested on root view")
        }
    }



    private fun moveCursorBy(dx: Float, dy: Float) {
        cursorX += dx
        cursorY += dy
        moveCursorTo(cursorX, cursorY)
    }

    private fun moveCursorTo(x: Float, y: Float) {
        cursorView.translationX = x
        cursorView.translationY = y
    }

    private fun simulateClickAtCursor() {
        val clickX = cursorX + cursorView.width / 2
        val clickY = cursorY + cursorView.height / 2

        Log.d(TAG, "Simulating click at: ($clickX, $clickY)")

        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis()

        val motionEventDown = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_DOWN, clickX, clickY, 0
        )
        val motionEventUp = MotionEvent.obtain(
            downTime, eventTime + 100, MotionEvent.ACTION_UP, clickX, clickY, 0
        )

//        webView.dispatchTouchEvent(motionEventDown)
//        webView.dispatchTouchEvent(motionEventUp)

        // ✅ Dispatch to the whole view hierarchy, not just the WebView
        requireActivity().window.decorView.dispatchTouchEvent(motionEventDown)
        requireActivity().window.decorView.dispatchTouchEvent(motionEventUp)

        // 👇 Bring focus back to the root view to continue receiving DPAD input
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            Log.d(TAG, "Focus re-requested")

        }

        Handler(Looper.getMainLooper()).postDelayed({
            view?.let {
                it.isFocusableInTouchMode = true
                it.requestFocus()
                Log.d(TAG, "Focus re-requested after click")
            }
        }, 200)

    }




//deviceItem_box__3ZdcA
//    rtcVideo_retry__3AIEF
}
