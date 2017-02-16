package com.radikal.pcnotifications.activities

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.DragEvent
import android.view.View
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.fragments.PairingFragment
import com.radikal.pcnotifications.service.network.NetworkDiscovery
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.fragment_container as fragmentContainer
import kotlinx.android.synthetic.main.activity_main.pair_try_again as pairTryAgainBtn
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import kotlinx.android.synthetic.main.activity_main.my_toolbar as toolbar
import kotlinx.android.synthetic.main.activity_main.drawerlayout as drawerLayout
import kotlinx.android.synthetic.main.activity_main.container_menu as containerMenu


class MainActivity : AppCompatActivity(), PairingFragment.OnFragmentInteractionListener {
    val TAG = "MainActivity"

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery
    @Inject
    lateinit var pairingFragment: PairingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MainApplication).appComponent.inject(this)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp)

        addPairingFragment()
        pairTryAgainBtn.setOnClickListener {
            pairTryAgainBtn.visibility = View.GONE
            addPairingFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home ->  {
                if (drawerLayout.drawerState == FlowingDrawer.STATE_OPEN) drawerLayout.closeMenu() else drawerLayout.openMenu()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFindServerSucceeded(serverIp: String) {
        //TODO save serverIp
        removePairingFragment()
    }

    override fun onFindServerFailed() {
        removePairingFragment()
        pairTryAgainBtn.visibility = View.VISIBLE
    }

    private fun addPairingFragment() {
        val transaction = fragmentManager.beginTransaction()

        pairingFragment.onAttach(this)

        transaction.add(fragmentContainer.id, pairingFragment as Fragment)
        transaction.commit()
    }

    private fun removePairingFragment() {
        val transaction = fragmentManager.beginTransaction()
        transaction.remove(pairingFragment)
        transaction.commit()
    }
}
