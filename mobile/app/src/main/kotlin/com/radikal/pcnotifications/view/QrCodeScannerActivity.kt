package com.radikal.pcnotifications.view

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.google.zxing.Result

import com.radikal.pcnotifications.R
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.view.ViewGroup
import com.radikal.pcnotifications.model.domain.ServerDetails

class QrCodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val TAG: String = javaClass.simpleName

    lateinit private var mScannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scanner)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val contentFrame = findViewById(R.id.content_frame) as ViewGroup
        mScannerView = ZXingScannerView(this)
        contentFrame.addView(mScannerView)

        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun handleResult(result: Result?) {
        val serverData: String? = result?.text

        val split = serverData?.split(",")

        if (split == null || split.size < 3) {
            //TODO show some error message
            return
        }

        val serverDetails = ServerDetails(split[0], split[1], split[2].toInt())
        mScannerView.stopCamera()

        val handler = Handler()
        handler.postDelayed({
            MainActivity.startActivity(applicationContext, serverDetails)
        }, 1000)
    }
}
