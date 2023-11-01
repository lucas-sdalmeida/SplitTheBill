package com.lucassdalmeida.splitthebill.application.member

import android.os.Parcelable
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberDto(
    val id: Long,
    val name: String,
    val expenses: Set<Pair<String, Double>>,
) : Parcelable
