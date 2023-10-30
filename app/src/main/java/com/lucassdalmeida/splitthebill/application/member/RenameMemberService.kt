package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Member

class RenameMemberService(private val memberRepository: MemberRepository) {
    fun rename(memberId: Long, newName: String): MemberDto {
        val member = memberRepository.findById(memberId) ?:
            throw NoSuchElementException("There is not such member! Provided id: $memberId")

        member.name = newName
        memberRepository.create(member)
        
        return member.toDto()
    }
}