package com.example.instagramclone.managers.handler

interface AuthHandler {
    fun onSuccess(uid: String)
    fun onError(exception: Exception?)
}