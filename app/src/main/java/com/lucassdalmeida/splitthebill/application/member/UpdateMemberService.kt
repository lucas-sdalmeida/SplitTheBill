package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Expense

class UpdateMemberService(private val memberRepository: MemberRepository) {
    fun rename(memberId: Long, newName: String, expense: Expense? = null): MemberDto {
        val member = memberRepository.findById(memberId) ?:
            throw NoSuchElementException("There is not such member! Provided id: $memberId")

        member.apply {
            name = newName
            this.expense = expense ?: this.expense
        }
        memberRepository.create(member)
        
        return member.toDto()
    }
}