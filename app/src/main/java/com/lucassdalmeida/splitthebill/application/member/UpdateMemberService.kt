package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Expense

class UpdateMemberService(private val memberRepository: MemberRepository) {
    fun rename(memberId: Long, newName: String, expenses: Set<Expense> = emptySet()): MemberDto {
        val member = memberRepository.findById(memberId) ?:
            throw NoSuchElementException("There is not such member! Provided id: $memberId")

        member.name = newName
        expenses.forEach { member.addExpense(it) }
        memberRepository.create(member)
        
        return member.toDto()
    }
}