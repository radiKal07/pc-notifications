package com.radikal.pcnotifications.activities

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.fragments.PairingFragment
import com.radikal.pcnotifications.service.network.NetworkDiscovery
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.fragment_container as fragmentContainer


class MainActivity() : AppCompatActivity(), PairingFragment.OnFragmentInteractionListener {
    val TAG = "MainActivity"

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MainApplication).appComponent.inject(this)

        addPairingFragment()
    }

    private fun addPairingFragment() {
        val transaction = fragmentManager.beginTransaction()
        val pairingFragment = PairingFragment()

        pairingFragment.onAttach(this)

        transaction.add(fragmentContainer.id, pairingFragment as Fragment)
        transaction.commit()
    }

    override fun onFindServerSucceeded(serverIp: String) {
        fragmentContainer.visibility = View.GONE
    }
}
