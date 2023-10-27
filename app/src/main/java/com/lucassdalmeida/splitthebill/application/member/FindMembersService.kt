package com.lucassdalmeida.splitthebill.application.member

class FindMembersService(private val memberRepository: MemberRepository) {
    fun findOneById(id: Long) = memberRepository.findById(id)

    fun findAll() = memberRepository.findAll()
}