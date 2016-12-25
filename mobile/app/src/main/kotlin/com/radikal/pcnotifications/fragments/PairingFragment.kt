package com.radikal.pcnotifications.fragments

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.service.network.NetworkDiscovery
import javax.inject.Inject

/**
 * @author tudor
 */
class PairingFragment : Fragment() {
    val TAG = "PairingFragment"

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MainApplication).appComponent.inject(this)

        networkDiscovery.getServerIp {
            activity.runOnUiThread {
                mListener?.onFindServerSucceeded(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_pairing, container, false)
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is OnFragmentInteractionListener) {
            mListener = activity as OnFragmentInteractionListener?
        } else {
            throw RuntimeException(activity!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFindServerSucceeded(serverIp: String)
    }
}
