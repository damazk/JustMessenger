package com.example.justmessenger

import android.content.ClipData
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.android.gms.dynamite.DynamiteModule.load
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_item.view.*
import java.lang.System.load


class UserItem (val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load("${user.profileImageUrl}").into(viewHolder.itemView.profile_iv)
        viewHolder.itemView.username_tv.text = user.username

    }

    override fun getLayout() = R.layout.user_item
}