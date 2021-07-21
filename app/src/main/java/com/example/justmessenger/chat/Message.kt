package com.example.justmessenger.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message (
    val id: String,
    val fromId: String,
    val toId: String,
    val text: String,
    val timeSnap: Long,
    val toIdprofileImageUrl: String,
    val fromIdprofileImageUrl: String) : Parcelable {
    constructor() : this("", "", "", "", -1, "", "")
}