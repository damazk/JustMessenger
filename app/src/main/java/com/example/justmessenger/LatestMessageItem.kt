package com.example.justmessenger

import com.example.justmessenger.chat.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_item.view.*

class LatestMessageItem (val message: Message): Item<GroupieViewHolder>() {
    var user: User? = null
    var currentUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.latestMessageText_tv.text = message.text

        val userUid  =
            if (message.toId == FirebaseAuth.getInstance().uid) {
                message.fromId
            } else message.toId

        val ref = FirebaseDatabase.getInstance().getReference("users/$userUid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)

                Picasso.get().load("${user?.profileImageUrl}").into(viewHolder.itemView.latestMessageProfile_iv)
                viewHolder.itemView.latestMessageUsername_tv.text = user?.username
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val currentUserRef = FirebaseDatabase.getInstance().getReference("users/${FirebaseAuth.getInstance().uid}")
        currentUserRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_item
    }

}