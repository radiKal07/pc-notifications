package com.radikal.pcnotifications.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.R.id.my_toolbar
import com.radikal.pcnotifications.contracts.PairingContract
import com.radikal.pcnotifications.services.CustomNotificationListenerService
import com.radikal.pcnotifications.utils.snackbar
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.drawerlayout as drawerLayout
import kotlinx.android.synthetic.main.activity_main.container as container
import kotlinx.android.synthetic.main.activity_main.my_toolbar as toolbar
import kotlinx.android.synthetic.main.activity_main.add_device_fab as addDeviceFab
import kotlinx.android.synthetic.main.activity_main.find_server_progress_bar as findServerProgressBar
import kotlinx.android.synthetic.main.activity_main.find_server_text_view as findServerTextView


class MainActivity : AppCompatActivity(), PairingContract.View {
    val TAG = "MainActivity"

    @Inject
    lateinit var pairingPresenter: PairingContract.Presenter

    var dialog: DialogInterface? = null

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
            addDeviceFab.visibility = View.GONE
            findServerProgressBar.visibility = View.VISIBLE
            findServerTextView.visibility = View.VISIBLE
            showPairingCodeDialog()
        }

        pairingPresenter.setView(this)
        startService(Intent(this, CustomNotificationListenerService::class.java))
    }

    fun showPairingCodeDialog() {
        val builder = AlertDialog.Builder(this)
        val view = this.layoutInflater.inflate(R.layout.port_input_dialog, null)
        builder.setView(view)
                .setTitle("Pairing")
                .setMessage("Please input pairing code")
                .setCancelable(false)
                .setPositiveButton("OK") {
                    dialog, which ->
                    run {
                        this.dialog = dialog
                        val portEditText = view.findViewById(R.id.port_edit_text) as EditText?
                        val portString = portEditText?.text?.toString()
                        pairingPresenter.onPortSubmitted(portString)
                    }
                }.setNegativeButton("Cancel") {
            dialog, which ->
            hideServerSearch()
            addDeviceFab.visibility = View.VISIBLE
        }

        builder.create().show()
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            snackbar(container, message)
        }
    }

    override fun onServerFound() {
        runOnUiThread { hideServerSearch() }
    }

    override fun onServerFindFailed() {
        runOnUiThread {
            hideServerSearch()
            addDeviceFab.visibility = View.VISIBLE
        }
    }

    fun hideServerSearch() {
        dialog?.cancel()
        findServerProgressBar.visibility = View.GONE
        findServerTextView.visibility = View.GONE
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
