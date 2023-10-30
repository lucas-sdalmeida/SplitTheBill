package com.lucassdalmeida.splitthebill.persistence.sqlite

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import com.lucassdalmeida.splitthebill.application.member.MemberRepository
import com.lucassdalmeida.splitthebill.domain.model.member.Expense
import com.lucassdalmeida.splitthebill.domain.model.member.Member

class SQLiteMemberRepositoryImpl(context: Context): MemberRepository {
    private val sqLiteDatabase = context.openOrCreateDatabase(
        SPLIT_THE_BILL_DB,
        MODE_PRIVATE,
        null
    )

    init {
        sqLiteDatabase.execSQL(CREATE_MEMBER_TABLE)
    }

    override fun create(member: Member) {
        sqLiteDatabase.replace(MEMBER_TABLE_NAME, null, member.toContentValues())
    }

    override fun findById(id: Long): Member? {
        val cursor = sqLiteDatabase.rawQuery(SELECT_MEMBER, arrayOf(id.toString()))
        val member = if (cursor.moveToFirst()) cursor.rowToMember() else null

        cursor.close()
        return member
    }

    override fun findAll(): List<Member> {
        val members = mutableListOf<Member>()
        val cursor = sqLiteDatabase.rawQuery(SELECT_ALL_MEMBERS, emptyArray())

        while (cursor.moveToNext())
            members.add(cursor.rowToMember())

        cursor.close()
        return members
    }

    override fun remove(id: Long) {
        sqLiteDatabase.delete(
            MEMBER_TABLE_NAME,
            "$ID_COLUMN = ?",
            arrayOf(id.toString())
        )
    }

    override operator fun contains(id: Long): Boolean {
        val cursor = sqLiteDatabase.rawQuery(IS_IN_REPOSITORY, arrayOf(id.toString()))
        val exists = cursor.moveToNext() && cursor.getLong(0) == 1L

        cursor.close()
        return exists
    }

    private fun Member.toContentValues() = ContentValues().also {
        it.put(ID_COLUMN, id)
        it.put(NAME_COLUMN, name)
        it.put(EXPENSE_COLUMN, expense?.description ?: "")
        it.put(EXPENSE_PRICE, expense?.price ?: 0.0)
    }

    private fun Cursor.rowToMember(): Member {
        val expense = Expense(
            getString(getColumnIndexOrThrow(EXPENSE_COLUMN)),
            getDouble(getColumnIndexOrThrow(EXPENSE_PRICE)),
        )
        return Member(
            getLong(getColumnIndexOrThrow(ID_COLUMN)),
            getString(getColumnIndexOrThrow(NAME_COLUMN)),
            expense,
        )
    }

    companion object {
        const val SPLIT_THE_BILL_DB = "split_the_bill_db"

        const val MEMBER_TABLE_NAME = "MEMBER"
        const val ID_COLUMN = "ID"
        const val NAME_COLUMN = "NAME"
        const val EXPENSE_COLUMN = "EXPENSE"
        const val EXPENSE_PRICE = "PRICE"

        const val CREATE_MEMBER_TABLE = """
                    CREATE TABLE IF NOT EXISTS $MEMBER_TABLE_NAME(
                        $ID_COLUMN INTEGER PRIMARY KEY,
                        $NAME_COLUMN TEXT NOT NULL,
                        $EXPENSE_COLUMN TEXT,
                        $EXPENSE_PRICE REAL
                    );
                """

        const val SELECT_MEMBER = """
            SELECT * FROM $MEMBER_TABLE_NAME WHERE $ID_COLUMN = ?
        """
        const val SELECT_ALL_MEMBERS = """
            SELECT * FROM $MEMBER_TABLE_NAME
        """

        const val IS_IN_REPOSITORY = """
            SELECT COUNT(*) FROM $MEMBER_TABLE_NAME WHERE ID = ?
        """
    }
}