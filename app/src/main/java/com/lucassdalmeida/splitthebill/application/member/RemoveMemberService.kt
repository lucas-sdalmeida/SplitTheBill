package com.lucassdalmeida.splitthebill.application.member

class RemoveMemberService(private val memberRepository: MemberRepository) {
    fun remove(id: Long) = memberRepository.remove(id)
}