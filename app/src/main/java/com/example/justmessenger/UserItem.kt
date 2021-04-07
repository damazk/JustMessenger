package com.example.justmessenger

import android.content.ClipData
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_item.view.*

class UserItem (val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_tv.text = user.username
    }

    override fun getLayout() = R.layout.user_item
}