package com.lucassdalmeida.splitthebill.controller

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.lucassdalmeida.splitthebill.R
import com.lucassdalmeida.splitthebill.databinding.MemberTileBinding
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import kotlin.math.absoluteValue
import kotlin.math.sign

class MembersListViewAdapter(
    context: Context,
    private val memberList: MutableList<Pair<Member, Double?>>,
): ArrayAdapter<Pair<Member, Double?>>(context, LAYOUT, memberList) {
    companion object {
        val LAYOUT = R.layout.member_tile
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val (member, deviation) = memberList[position]
        val targetView = convertView ?: createView(parent)
        val memberTileHolder = targetView.tag as MemberTileHolder

        with(memberTileHolder) {
            memberName.text = member.name
            memberExpenses.text = member.expenses.joinToString(", ") { it.description }
            memberTotalExpense.text = context.getString(
                R.string.price_format,
                String.format("%.2f", member.totalSpent)
            )
            memberBalanceFactor.text = when (deviation) {
                null -> ""
                else -> context.getString(
                    R.string.deviation_format,
                    getDeviationSign(deviation),
                    String.format("%.2f", deviation.absoluteValue)
                )
            }
        }

        return targetView
    }

    private fun createView(parent: ViewGroup): View {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val memberTileBinding = MemberTileBinding.inflate(inflater)
        val memberTileHolder = MemberTileHolder(
            memberTileBinding.memberName,
            memberTileBinding.memberExpenses,
            memberTileBinding.memberTotalExpense,
            memberTileBinding.memberBalanceFactor,
        )

        return memberTileBinding.root.also {
            it.tag = memberTileHolder
        }
    }

    private fun getDeviationSign(deviation: Double) = when {
        deviation.sign >= 0 -> "+"
        else -> "-"
    }

    private data class MemberTileHolder(
        val memberName: TextView,
        val memberExpenses: TextView,
        val memberTotalExpense: TextView,
        val memberBalanceFactor: TextView,
    )
}