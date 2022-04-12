package com.example.instagramclone.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.adapter.HomeAdapter
import com.example.instagramclone.model.Post
import com.example.instagramclone.utils.Logger

class HomeFragment : BaseFragment() {
    val TAG = HomeFragment::class.java.simpleName
    private var listener: HomeListener? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val iv_camera = view.findViewById<ImageView>(R.id.iv_camera)
        iv_camera.setOnClickListener {
            listener!!.scrollToUpload()
        }

        refreshAdapter(loadPosts())
    }

    /*
   * onAttach is for communication of Fragments
   * */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Logger.e(TAG, "is attached!")
        listener = if (context is HomeListener) {
            context
        } else {
            throw RuntimeException("$context must implement HomeListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Logger.e(TAG, "is detached!")
        listener = null
    }

    private fun refreshAdapter(items: ArrayList<Post>){
        val adapter = HomeAdapter(this, items)
        recyclerView!!.adapter = adapter
    }

    private fun loadPosts(): ArrayList<Post> {
        val items = ArrayList<Post>()

        items.add(Post("https://images.unsplash.com/photo-1649452814258-ac8822022f2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1649452815023-443d2217eb56?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80"))
        items.add(Post("https://images.unsplash.com/photo-1568429838920-de3a3aa8cf1c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80"))

        return items
    }

    /*
    * This interface is created for communication with UploadFragment
    * */

    interface HomeListener {
        fun scrollToUpload()
    }
}