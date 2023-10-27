package com.lucassdalmeida.splitthebill.application.member

import com.lucassdalmeida.splitthebill.domain.model.member.Member

interface MemberRepository {
    fun create(member: Member)

    fun findById(id: Long): Member?

    fun findAll(): List<Member>

    fun remove(id: Long)

    operator fun contains(id: Long): Boolean
}
