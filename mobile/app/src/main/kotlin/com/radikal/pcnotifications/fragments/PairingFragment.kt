package com.radikal.pcnotifications.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.radikal.pcnotifications.MainApplication
import com.radikal.pcnotifications.R
import com.radikal.pcnotifications.service.network.NetworkDiscovery
import com.radikal.pcnotifications.utils.snackbar
import com.radikal.pcnotifications.validators.Validator
import com.radikal.pcnotifications.validators.impl.PortValidator
import javax.inject.Inject
import javax.inject.Named

/**
 * @author tudor
 */
class PairingFragment : Fragment() {
    val TAG = "PairingFragment"

    @Inject
    lateinit var networkDiscovery: NetworkDiscovery

    @Inject
    @Named("portValidator")
    lateinit var portValidator: PortValidator // TODO: inject as interface (Dagger2 does not know what impl to get)

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MainApplication).appComponent.inject(this)

        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.port_input_dialog, null)
        builder.setView(view)
                .setTitle("Pairing")
                .setMessage("Please input pairing code")
                .setCancelable(false)
                .setPositiveButton("OK") {
                    dialog, which ->
                    run {
                        val portEditText = view.findViewById(R.id.port_edit_text) as EditText?
                        val portString = portEditText?.text?.toString()
                        if (!portValidator.isValid(portString)) {
                            dialog.cancel()
                            snackbar(this.view, "Invalid code provided")
                            mListener?.onFindServerFailed()
                        } else {
                            networkDiscovery.getServerIp(portString!!.toInt()) {
                                activity.runOnUiThread {
                                    if (it == "-1") {
                                        snackbar(this.view, "Failed to connect")
                                        mListener?.onFindServerFailed()
                                    } else {
                                        mListener?.onFindServerSucceeded(it) // TODO also add port
                                    }
                                }
                            }
                        }
                    }
                }.setNegativeButton("Cancel") {
                    dialog, which -> dialog.cancel()
                    mListener?.onFindServerFailed()
                }

        builder.create().show()
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
        fun onFindServerFailed()
    }
}
