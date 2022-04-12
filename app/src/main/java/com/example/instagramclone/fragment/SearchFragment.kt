package com.example.instagramclone.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.R
import com.example.instagramclone.adapter.SearchAdapter
import com.example.instagramclone.model.User

/*
* In SearchFragment, all registered users can be found by searching keyword and followed.
**/


class SearchFragment : Fragment() {
    val TAG = SearchFragment::class.java.simpleName
    lateinit var rvSearch: RecyclerView
    var items = ArrayList<User>()
    var users = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rvSearch = view.findViewById(R.id.rvSearch)
        rvSearch.layoutManager = GridLayoutManager(activity, 1)
        val et_search = view.findViewById<EditText>(R.id.et_search)
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val keyWord = p0.toString().trim()
                usersByKeyWord(keyWord)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        loadUsers()
        refreshAdapter(items)

    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val adapter = SearchAdapter(this, items)
        rvSearch.adapter = adapter
    }

    private fun usersByKeyWord(keyWord: String) {
        if (keyWord.isEmpty()) {
            refreshAdapter(items)
        }

        users.clear()
        for (user in items) {
            if (user.fullname.lowercase().startsWith(keyWord.lowercase()))
                users.add(user)
        }

        refreshAdapter(users)
    }

    private fun loadUsers(): ArrayList<User> {
        items = ArrayList()
        items.add(User("xurshid", "shamsun.com@gmail.com"))
        items.add(User("begzod", "shamsun.com@gmail.com"))
        items.add(User("sherzod", "shamsun.com@gmail.com"))
        items.add(User("firdavs", "shamsun.com@gmail.com"))
        items.add(User("jamshid", "shamsun.com@gmail.com"))
        items.add(User("muxtor", "shamsun.com@gmail.com"))
        items.add(User("yahyo", "shamsun.com@gmail.com"))
        items.add(User("ali", "shamsun.com@gmail.com"))
        items.add(User("mansur", "shamsun.com@gmail.com"))
        items.add(User("jabbor", "shamsun.com@gmail.com"))
        items.add(User("samandar", "shamsun.com@gmail.com"))
        items.add(User("mirzohid", "shamsun.com@gmail.com"))
        items.add(User("nurmuhammad", "shamsun.com@gmail.com"))

        return items
    }

}