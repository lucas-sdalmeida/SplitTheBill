package com.lucassdalmeida.splitthebill.domain.model.member

data class Expense(val description: String, val price: Double) {
    init {
        val errorMessage: StringBuilder = StringBuilder()

        if (description.isBlank())
            errorMessage.append("A description must be given!")
        if (price <= 0)
            errorMessage.append("An expense with non-positive price has no meaning!" +
                    " Provided: $price")

        require(errorMessage.isBlank()) { errorMessage.toString() }
    }
}