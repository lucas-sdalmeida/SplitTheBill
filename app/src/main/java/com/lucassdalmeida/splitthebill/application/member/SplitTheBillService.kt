package com.lucassdalmeida.splitthebill.application.member

class SplitTheBillService(private val memberRepository: MemberRepository) {
    fun split() : Map<MemberDto, Double> {
        val members = memberRepository.findAll()

        if (members.isEmpty())
            return emptyMap()

        val expensesMean = members.sumOf { it.totalSpent } / members.size

        return members.associate { it.toDto() to it.totalSpent - expensesMean }
    }
}