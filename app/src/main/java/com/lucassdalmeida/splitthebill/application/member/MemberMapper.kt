package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.domain.model.member.Member

fun Member.toDto() = MemberDto(
    id,
    name,
    expenses.map { it.description to it.price }.toList(),
)

fun Member.Companion.fromDto(dto: MemberDto) = Member(
    dto.id,
    dto.name,
    dto.expenses
        .map { (description, price) -> Expense(description, price) }
        .toSet()
)
