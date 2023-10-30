package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Member

fun Member.toDto() = MemberDto(
    id,
    name,
    expenses.map { it.description to it.price }.toList(),
)
