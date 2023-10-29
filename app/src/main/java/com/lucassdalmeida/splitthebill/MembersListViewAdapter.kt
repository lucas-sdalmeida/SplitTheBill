package com.lucassdalmeida.splitthebill

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lucassdalmeida.splitthebill.databinding.MemberTileBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member

class MembersListViewAdapter(
    context: Context,
    private val memberList: MutableList<Member>,
): ArrayAdapter<Member>(context, LAYOUT, memberList) {
    companion object {
        val LAYOUT = R.layout.member_tile
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val member = memberList[position]
        val targetView = convertView ?: createView(parent)
        val memberTileHolder = targetView.tag as MemberTileHolder

        with(memberTileHolder) {
            memberName.text = member.name
            memberTotalExpense.text = "R$${member.totalExpense}"
        }

        return targetView
    }

    private fun createView(parent: ViewGroup): View {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val memberTileBinding = MemberTileBinding.inflate(inflater)
        val memberTileHolder = MemberTileHolder(
            memberTileBinding.memberName,
            memberTileBinding.memberTotalExpense,
            memberTileBinding.memberBalanceFactor,
        )

        return memberTileBinding.root.also {
            it.tag = memberTileHolder
        }
    }

    private data class MemberTileHolder(
        val memberName: TextView,
        val memberTotalExpense: TextView,
        val memberBalanceFactor: TextView,
    )
}