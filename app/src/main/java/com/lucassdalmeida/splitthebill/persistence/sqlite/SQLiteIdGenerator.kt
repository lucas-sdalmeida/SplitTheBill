package com.lucassdalmeida.splitthebill.persistence.sqlite

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
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
            return 1L

        val cursor = sqLiteDatabase.rawQuery(
            "SELECT (COUNT(*) + 1) NEXT_ID FROM $MEMBER_TABLE_NAME",
            emptyArray()
        )
        val nextId = cursor.run { if (moveToFirst()) getLong(0) else 1L }

        cursor.close()
        return nextId
    }

    private fun tableNotExists(): Boolean {
        val cursor = sqLiteDatabase.rawQuery(
            "SELECT name FROM SQLITE_MASTER WHERE type = 'table' and name = '$MEMBER_TABLE_NAME'",
            arrayOf()
        )
        val notExists = !cursor.moveToFirst()

        cursor.close()
        return notExists
    }
}