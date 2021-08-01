package com.example.justmessenger

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.new_message_item.view.*


class UserItem (val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        Picasso.get().load("${user.profileImageUrl}").into(viewHolder.itemView.profile_iv)
        viewHolder.itemView.username_tv.text = user.username
    }

    override fun getLayout() = R.layout.new_message_item
}