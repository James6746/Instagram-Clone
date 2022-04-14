package com.example.instagramclone.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.R
import com.example.instagramclone.adapter.ProfileAdapter
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.StorageManager
import com.example.instagramclone.managers.handler.DBUserHandler
import com.example.instagramclone.managers.handler.StorageHandler
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Extensions.toast
import com.google.android.material.imageview.ShapeableImageView
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

/*
* In ProfileFragment, posts that user uploaded can be seen and user is able to change his/her profile photo.
* */

class ProfileFragment : BaseFragment() {
    val TAG = ProfileFragment::class.java.simpleName
    lateinit var rv_profile: RecyclerView
    lateinit var iv_profile: ShapeableImageView
    lateinit var tv_fullname: TextView
    lateinit var tv_email: TextView

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rv_profile = view.findViewById(R.id.rv_profile)
        rv_profile.layoutManager = GridLayoutManager(activity, 2)

        iv_profile = view.findViewById(R.id.iv_profile)
        tv_fullname = view.findViewById(R.id.tv_fullname)
        tv_email = view.findViewById(R.id.tv_email)

        iv_profile.setOnClickListener {
            pickFishBunPhoto()
        }

        val iv_logout = view.findViewById<ImageView>(R.id.iv_logout)
        iv_logout.setOnClickListener {
            AuthManager.signOut()
            callSignInActivity(requireActivity())
        }

        loadUserInfo()
        refreshAdapter(loadPosts())
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
            allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
            pickedPhoto = allPhotos.get(0)
            uploadPickedPhoto()
            upLoadUserPhoto()
        }

    private fun refreshAdapter(items: ArrayList<Post>) {
        val adapter = ProfileAdapter(this, items)
        rv_profile.adapter = adapter
    }

    private fun uploadPickedPhoto() {
        if (pickedPhoto != null) {
            d(TAG, pickedPhoto!!.path.toString())
        }
    }

    private fun loadUserInfo(){
        DatabaseManager.loadUser(AuthManager.currentUser()!!.uid, object : DBUserHandler{
            override fun onSuccess(user: User?) {
                if(user != null){
                    showUserInfo(user)
                }
            }

            override fun onError(e: Exception) {

            }

        })
    }

    private fun showUserInfo(user: User){
        tv_fullname.text = user.fullname
        tv_email.text = user.email
        Glide.with(this).load(user.userImg)
            .placeholder(R.drawable.img)
            .error(R.drawable.img)
            .into(iv_profile)
    }

    private fun upLoadUserPhoto() {
        if (pickedPhoto == null) return
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                DatabaseManager.updateUserImage(imgUrl)
                iv_profile.setImageURI(pickedPhoto)
                toast("successfully")
            }

            override fun onError(exception: Exception?) {
                toast("failed")
                toast(exception.toString())
            }

        })
    }

    private fun loadPosts(): ArrayList<Post> {
        val items = ArrayList<Post>()

        items.add(Post("https://images.unsplash.com/photo-1649452814258-ac8822022f2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452815023-443d2217eb56?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1568429838920-de3a3aa8cf1c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452814258-ac8822022f2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452815023-443d2217eb56?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1568429838920-de3a3aa8cf1c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452814258-ac8822022f2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452815023-443d2217eb56?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1568429838920-de3a3aa8cf1c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"))

        return items

    }


}