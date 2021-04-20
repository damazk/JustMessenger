package com.example.justmessenger

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val username: String,
    val uid: String,
    val email: String,
    val password: String,
    val profileImageUrl: String) : Parcelable {

    constructor () : this ("", "", "", "", "")
}
