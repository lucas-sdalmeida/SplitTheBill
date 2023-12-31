package com.lucassdalmeida.splitthebill.domain.model.member

class Member(
    val id: Long,
    name: String,
    expenses: Set<Expense> = emptySet(),
) {
    var name = name
        set(value) {
            require(value.isNotBlank()) { "Member's name cannot be blank" }
            field = value
        }

    private val _expenses = expenses.toMutableSet()
    val expenses get() = _expenses.toSet()

    val totalSpent get() = _expenses.sumOf { it.price }

    init {
        require(name.isNotBlank()) { "Member's name cannot be blank!" }
        require(_expenses.size <= MAX_NUMBER_OF_EXPENSES)
            { "A member can have three expenses at most!" }
    }

    companion object {
        val MAX_NUMBER_OF_EXPENSES = 3
    }

    fun addExpense(expense: Expense) {
        check(_expenses.size < MAX_NUMBER_OF_EXPENSES) { "Max number of expenses reached!" }
        _expenses.add(expense)
    }

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

    override fun toString() = "Member(id=$id, name='$name', _expenses=$_expenses, " +
            "totalSpent=$totalSpent)"
}