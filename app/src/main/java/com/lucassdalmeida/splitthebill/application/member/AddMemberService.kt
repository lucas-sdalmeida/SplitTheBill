package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.domain.model.member.Member
import com.lucassdalmeida.splitthebill.domain.services.IdGeneratorService

class AddMemberService(
    private val memberRepository: MemberRepository,
    private val longIdGeneratorService: IdGeneratorService<Long>,
) {
    fun add(name: String, expense: Set<Expense> = emptySet()): MemberDto {
        val id = longIdGeneratorService.next()
        val member = Member(id, name, expense)

        memberRepository.create(member)

        return member.toDto()
    }
}
