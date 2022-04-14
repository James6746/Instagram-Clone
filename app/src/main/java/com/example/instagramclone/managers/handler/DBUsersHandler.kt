package com.example.instagramclone.managers.handler

import com.example.instagramclone.model.User

interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}