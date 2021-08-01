package com.example.justmessenger.chat

import com.example.justmessenger.R
import com.example.justmessenger.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.new_message_item.view.*

class ChatFromItem (val message: Message, val user: User): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_from_tv.text = message.text
        Picasso.get().load("${user.profileImageUrl}").into(viewHolder.itemView.chat_from_img)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_item
    }

}