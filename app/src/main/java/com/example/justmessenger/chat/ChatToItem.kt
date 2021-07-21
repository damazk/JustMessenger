package com.example.justmessenger.chat

import com.example.justmessenger.R
import com.example.justmessenger.User
import com.example.justmessenger.UsersActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.chat_to_item.view.*

class ChatToItem (val message: Message, val user: User): Item<GroupieViewHolder>() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser.uid

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_to_tv.text = message.text
        Picasso.get().load("${user.profileImageUrl}").into(viewHolder.itemView.chat_to_img)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_item
    }

}