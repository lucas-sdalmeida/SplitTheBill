package com.lucassdalmeida.splitthebill.domain.model.member

class Member(
    val id: Long,
    name: String,
    var expense: Expense? = null,
) {
    var name = name
        set(value) {
            require(value.isNotBlank()) { "Member's name cannot be blank" }
            field = value
        }

    init {
        require(name.isNotBlank()) { "Member's name cannot be blank!" }
    }

    companion object {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString() ="Member(id=$id, name='$name', expense=$expense)"
}