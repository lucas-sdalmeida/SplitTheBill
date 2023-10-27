package com.lucassdalmeida.splitthebill.domain.model.member

class Member(
    val id: Long,
    name: String,
    expenses: Set<Expense> = emptySet()
) {
    var name = name
        set(value) {
            require(value.isNotBlank()) { "Member's name cannot be blank" }
            field = value
        }

    private val _expenses = expenses.toMutableSet()
    val expenses get() = _expenses.toSet()

    val totalExpense get() = _expenses.sumOf { it.price }

    init {
        require(name.isNotBlank()) { "Member's name cannot be blank!" }
    }

    fun addExpense(expense: Expense) = _expenses.add(expense)

    fun removeExpense(expense: Expense) = _expenses.remove(expense)

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

    override fun toString() ="Member(id=$id, name='$name', _expenses=$_expenses, " +
            "totalExpense=$totalExpense)"
}