package com.lucassdalmeida.splitthebill.persistence.sqlite

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.lucassdalmeida.splitthebill.domain.services.IdGeneratorService
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl.Companion.MEMBER_TABLE_NAME
import com.lucassdalmeida.splitthebill.persistence.sqlite.SQLiteMemberRepositoryImpl.Companion.SPLIT_THE_BILL_DB

class SQLiteIdGenerator(context: Context): IdGeneratorService<Long> {
    private val sqLiteDatabase = context.openOrCreateDatabase(
        SPLIT_THE_BILL_DB,
        MODE_PRIVATE,
        null
    )

    override fun next(): Long {
        if (tableNotExists())
            return 1

        val cursor = sqLiteDatabase.rawQuery(
            "SELECT (COUNT(*) + 1) NEXT_ID FROM $MEMBER_TABLE_NAME",
            emptyArray()
        )

        return cursor.getLong(0)
    }

    private fun tableNotExists(): Boolean {
        val cursor = sqLiteDatabase.rawQuery(
            "SELECT NAME FROM SQLITE_MASTER WHERE TYPE = 'TABLE' AND NAME='$MEMBER_TABLE_NAME'",
            arrayOf()
        )
        return !cursor.moveToFirst()
    }
}