package com.example.instagramclone.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.R
import com.example.instagramclone.utils.Logger
import com.example.instagramclone.utils.Utils
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.RuntimeException


/*
* In UploadFragment, user can upload
* a post with photo and caption
* */
class UploadFragment : BaseFragment() {
    val TAG = UploadFragment::class.java.simpleName
    private var listener: UploadListener? = null

    lateinit var fl_photo: FrameLayout
    lateinit var iv_photo: ImageView
    lateinit var et_caption: EditText

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        initViews(view)
        return view
    }

    /*
    * onAttach is for communication of Fragments
    * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.e(TAG, "is attached!")
        listener = if(context is UploadListener){
            context
        } else {
            throw RuntimeException("$context must implement FirstListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Logger.e(TAG, "is detached!")
        listener = null
    }

    private fun initViews(view: View) {
        val fl_view = view.findViewById<FrameLayout>(R.id.fl_view)
        setViewHeight(fl_view)
        et_caption = view.findViewById(R.id.et_caption)
        fl_photo = view.findViewById(R.id.fl_photo)
        iv_photo = view.findViewById(R.id.iv_photo)

        val iv_close = view.findViewById<ImageView>(R.id.iv_close)
        val iv_pick = view.findViewById<ImageView>(R.id.iv_pick)
        val iv_upload = view.findViewById<ImageView>(R.id.iv_upload)

        iv_pick.setOnClickListener {
            pickFishBunPhoto()
        }

        iv_close.setOnClickListener {
            hidePickedPhoto()
        }

        iv_upload.setOnClickListener {
            uploadNewPost()
        }
    }

    /*
    * Set view height as screen width
    * */

    private fun setViewHeight(flView: FrameLayout) {
        val params: ViewGroup.LayoutParams = flView.layoutParams
        params.height = Utils.screenSize(requireActivity().application).width
        flView.layoutParams = params
    }

    /*
    * Pick photo using FishBun library
    * */

    private fun pickFishBunPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos.get(0)
                showPickedPhotos()
            }
        }

    fun uploadNewPost() {
        listener!!.scrollToHome()
        val caption = et_caption.text.toString().trim()
        if (caption.isNotEmpty() && pickedPhoto != null) {
            Logger.d(TAG, caption)
            Logger.d(TAG, pickedPhoto!!.path.toString())
            resetAll()
        }
    }


    private fun showPickedPhotos() {
        fl_photo.visibility = View.VISIBLE
        iv_photo.setImageURI(pickedPhoto)
    }

    private fun hidePickedPhoto() {
        pickedPhoto = null
        fl_photo.visibility = View.GONE
    }


    private fun resetAll() {
        et_caption.text.clear()
        pickedPhoto = null
        fl_photo.visibility = View.GONE
    }

    /*
    * This interface is created for communication with HomeFragment
    * */

    interface UploadListener{
        fun scrollToHome()
    }

}