package com.lucassdalmeida.splitthebill.application.member

class FindMembersService(private val memberRepository: MemberRepository) {
    fun findOneById(id: Long) = memberRepository.findById(id)?.toDto()

    fun findAll() = memberRepository.findAll()
        .map { it.toDto() }
}