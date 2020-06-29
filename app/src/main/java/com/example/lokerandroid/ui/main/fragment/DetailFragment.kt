package com.example.lokerandroid.ui.main.fragment

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.lokerandroid.R
import com.example.lokerandroid.data.model.LokerPalette
import com.example.lokerandroid.data.model.LowonganKerja
import com.example.lokerandroid.data.model.SmsInfo
import com.example.lokerandroid.databinding.FragmentDetailBinding
import com.example.lokerandroid.databinding.SendSmsDialogBinding
import com.example.lokerandroid.ui.main.view.MainActivity
import com.example.lokerandroid.ui.main.viewmodel.DetailLokerViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailLokerViewModel
    private var jobUuid = 0
    private var sendSmsStarted = false
    private var currentJob: LowonganKerja? = null

    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            jobUuid = DetailFragmentArgs.fromBundle(
                it
            ).jobUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailLokerViewModel::class.java)
        viewModel.fetch(jobUuid)



        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.lokerLiveData.observe(viewLifecycleOwner, Observer { loker ->
            currentJob = loker
            loker?.let {
                dataBinding.loker = loker

                it.gambar?.let {
                    setupBackgroundColor(it)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.vibrantSwatch?.rgb ?: 0
                            val myPalette =
                                LokerPalette(
                                    intColor
                                )
                            dataBinding.palette = myPalette
                        }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this job")
                intent.putExtra(Intent.EXTRA_TEXT, "${currentJob?.posisi} dengan gaji ${currentJob?.gaji} lokasi di ${currentJob?.cabang}")
                intent.putExtra(Intent.EXTRA_STREAM, currentJob?.gambar)
                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean){
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentJob?.posisi} dengan gaji ${currentJob?.gaji} lokasi di ${currentJob?.cabang}",
                    currentJob?.gambar
                )
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it), R.layout.send_sms_dialog, null, false)

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") {dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel"){dialog, which ->  }
                    .show()

                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo){
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0 , intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }
}
