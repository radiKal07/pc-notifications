package com.radikal.pcnotifications.activities

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.fragments.PairingFragment
import com.radikal.pcnotifications.service.network.NetworkDiscovery
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.fragment_container as fragmentContainer
import kotlinx.android.synthetic.main.activity_main.pair_try_again as pairTryAgainBtn


class MainActivity() : AppCompatActivity(), PairingFragment.OnFragmentInteractionListener {
    val TAG = "MainActivity"

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery
    @Inject
    lateinit var pairingFragment: PairingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as MainApplication).appComponent.inject(this)

        addPairingFragment()
        pairTryAgainBtn.setOnClickListener {
            pairTryAgainBtn.visibility = View.GONE
            addPairingFragment()
        }
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