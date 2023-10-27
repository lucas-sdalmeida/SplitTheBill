package com.lucassdalmeida.splitthebill.domain.services

interface IdGeneratorService<T> {
    fun next(): T
}