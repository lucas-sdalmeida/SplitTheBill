package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.domain.model.member.Member

class AddExpenseToMemberService(private val memberRepository: MemberRepository) {
    fun addExpense(memberId: Long, description: String, price: Double): Member {
        val member = memberRepository.findById(memberId) ?:
            throw NoSuchElementException("There is not a member with id: $memberId")

        member.addExpense(Expense(description, price))
        memberRepository.create(member)

        return member
    }
}