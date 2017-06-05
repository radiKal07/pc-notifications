package com.radikal.pcnotifications.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.R.id.my_toolbar
import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.model.domain.ServerDetails
import com.radikal.pcnotifications.model.service.ServerDetailsDao
import com.radikal.pcnotifications.utils.snackbar
import kotlinx.android.synthetic.main.activity_main.connection_status as connectionStatus
import kotlinx.android.synthetic.main.activity_main.connection_status_image as connectionStatusImage
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.drawerlayout as drawerLayout
import kotlinx.android.synthetic.main.activity_main.container as container
import kotlinx.android.synthetic.main.activity_main.my_toolbar as toolbar
import kotlinx.android.synthetic.main.activity_main.add_device_fab as addDeviceFab
import kotlinx.android.synthetic.main.menu.forget_button as forgetButton


class MainActivity : AppCompatActivity(), PairingContract.View {
    val TAG = "MainActivity"

    @Inject
    lateinit var pairingPresenter: PairingContract.Presenter

    @Inject
    lateinit var serverDetailsDao: ServerDetailsDao

    companion object {
        val HOSTNAME_TAG: String = "hostname"
        val IP_TAG: String = "ip"
        val PORT_TAG: String = "port"

        fun startActivity(context: Context, serverDetails: ServerDetails) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(HOSTNAME_TAG, serverDetails.hostname)
            intent.putExtra(IP_TAG, serverDetails.ip)
            intent.putExtra(PORT_TAG, serverDetails.port)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MainApplication).appComponent.inject(this)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp)

        ((container.layoutParams) as RelativeLayout.LayoutParams).addRule(RelativeLayout.BELOW, my_toolbar)

        addDeviceFab.setOnClickListener {
            val intent = Intent(applicationContext, QrCodeScannerActivity::class.java)
            startActivity(intent)
            addDeviceFab.visibility = View.GONE
        }

        pairingPresenter.setView(this)

        forgetButton.setOnClickListener {
            serverDetailsDao.delete()
            addDeviceFab.visibility = View.VISIBLE
        }

        val hostname: String? = intent?.extras?.get(HOSTNAME_TAG) as String?
        val ip: String? = intent?.extras?.get(IP_TAG) as String?
        val port: Int? = intent?.extras?.get(PORT_TAG) as Int?

        if (hostname != null && ip != null && port != null) {
            pairingPresenter.onServerDetails(ServerDetails(hostname, ip, port))
            Log.d(TAG, "ip and port: $ip:$port ($hostname)")
        }

        if (pairingPresenter.isConnected()) {
            onServerFound()
        } else {
            onServerFindFailed()
        }

        pairingPresenter.attachStateListeners()
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            snackbar(container, message)
        }
    }

    override fun onServerFound() {
        runOnUiThread {
            connectionStatus.text = getString(R.string.connection_up)
            connectionStatusImage.setImageDrawable(getDrawable(R.drawable.ic_sentiment_very_satisfied_black_48dp))
            addDeviceFab.visibility = View.GONE
        }
    }

    override fun onServerFindFailed() {
        runOnUiThread {
            addDeviceFab.visibility = View.VISIBLE
            connectionStatus.text = getString(R.string.connection_down)
            connectionStatusImage.setImageDrawable(getDrawable(R.drawable.ic_sentiment_dissatisfied_black_48dp))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (drawerLayout.drawerState == FlowingDrawer.STATE_OPEN) drawerLayout.closeMenu() else drawerLayout.openMenu()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
