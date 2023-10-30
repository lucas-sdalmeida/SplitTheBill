package com.lucassdalmeida.splitthebill.application.member

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberDto(
    val id: Long,
    val name: String,
    val expenses: List<Pair<String, Double>>
) : Parcelable