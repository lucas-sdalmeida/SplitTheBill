package com.lucassdalmeida.splitthebill.persistence.sqlite

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import androidx.core.database.getStringOrNull
import androidx.core.database.sqlite.transaction
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
        sqLiteDatabase.transaction {
            execSQL(CREATE_MEMBER_TABLE)
            execSQL(CREATE_MEMBER_EXPENSE_TABLE)
        }
    }

    override fun create(member: Member) {
        sqLiteDatabase.transaction {
            replace(MEMBER_TABLE_NAME, null, member.toContentValues())
            member.expense.forEach {
                replace(EXPENSE_TABLE_NAME, null, it.toContentValues(member.id))
            }
        }
    }

    override fun findById(id: Long): Member? {
        val cursor = sqLiteDatabase.rawQuery(SELECT_MEMBER, arrayOf(id.toString()))
        val member = if (cursor.moveToFirst()) cursor.rowToMember() else null

        cursor.close()
        return member
    }

    override fun findAll(): List<Member> {
        val members = mutableListOf<Member>()
        val cursor = sqLiteDatabase.rawQuery(SELECT_ALL_MEMBERS, arrayOf())

        while (cursor.moveToNext())
            members.add(cursor.rowToMember())

        cursor.close()
        return members
    }

    override fun remove(id: Long) {
        sqLiteDatabase.transaction {
            delete(MEMBER_TABLE_NAME, "$ID_COLUMN = ?", arrayOf(id.toString()))
            delete(EXPENSE_TABLE_NAME, "$ID_COLUMN = ?", arrayOf(id.toString()))
        }
    }

    override operator fun contains(id: Long): Boolean {
        val cursor = sqLiteDatabase.rawQuery(IS_IN_REPOSITORY, arrayOf(id.toString()))
        return cursor.moveToNext() && cursor.getLong(0) == 1L
    }

    private fun Member.toContentValues() = ContentValues().also {
        it.put(ID_COLUMN, id)
        it.put(NAME_COLUMN, name)
    }

    private fun Expense.toContentValues(memberId: Long) = ContentValues().also {
        it.put(ID_COLUMN, memberId)
        it.put(DESCRIPTION_COLUMN, description)
        it.put(PRICE_COLUMN, price)
    }

    private fun Cursor.rowToMember(): Member {
        val idColumnIndex = getColumnIndexOrThrow(ID_COLUMN)
        val member = Member(
            getLong(idColumnIndex),
            getString(getColumnIndexOrThrow(NAME_COLUMN))
        )

        val descriptionColumnIndex = getColumnIndexOrThrow(DESCRIPTION_COLUMN)
        if (getStringOrNull(descriptionColumnIndex) == null)
            return member

        while (moveToNext() && getLong(idColumnIndex) == member.id) {
            member.addExpense(Expense(
                getString(descriptionColumnIndex),
                getDouble(getColumnIndexOrThrow(PRICE_COLUMN))
            ))
        }

        moveToPrevious()
        return member
    }

    companion object {
        const val SPLIT_THE_BILL_DB = "split_the_bill_db"

        const val MEMBER_TABLE_NAME = "MEMBER"
        const val ID_COLUMN = "ID"
        const val NAME_COLUMN = "NAME"

        const val EXPENSE_TABLE_NAME = "MEMBER_EXPENSE"
        const val DESCRIPTION_COLUMN = "DESCRIPTION"
        const val PRICE_COLUMN = "PRICE"

        const val CREATE_MEMBER_TABLE = """
                    CREATE TABLE IF NOT EXISTS $MEMBER_TABLE_NAME(
                        $ID_COLUMN INTEGER PRIMARY KEY,
                        $NAME_COLUMN TEXT NOT NULL
                    );
                """
        const val CREATE_MEMBER_EXPENSE_TABLE = """
                    CREATE TABLE IF NOT EXISTS $EXPENSE_TABLE_NAME(
                        $ID_COLUMN INTEGER,
                        $DESCRIPTION_COLUMN TEXT,
                        $PRICE_COLUMN REAL,
                        
                        CONSTRAINT MEMBER_EXPENSE_PK PRIMARY KEY (
                            $ID_COLUMN, 
                            $DESCRIPTION_COLUMN,
                            $PRICE_COLUMN),
                        CONSTRAINT MEMBER_EXPENSE_MEMBER_FK FOREIGN KEY ($ID_COLUMN) 
                            REFERENCES $MEMBER_TABLE_NAME($ID_COLUMN)
                    );
                """

        const val SELECT_MEMBER = """
            SELECT M.$ID_COLUMN $ID_COLUMN, M.$NAME_COLUMN $NAME_COLUMN,
                E.$DESCRIPTION_COLUMN $DESCRIPTION_COLUMN, E.$PRICE_COLUMN $PRICE_COLUMN
            FROM $MEMBER_TABLE_NAME M LEFT OUTER JOIN $EXPENSE_TABLE_NAME E ON
                M.$ID_COLUMN = E.$ID_COLUMN
            WHERE M.$ID_COLUMN = ?
        """
        const val SELECT_ALL_MEMBERS = """
            SELECT M.$ID_COLUMN $ID_COLUMN, M.$NAME_COLUMN $NAME_COLUMN,
                E.$DESCRIPTION_COLUMN $DESCRIPTION_COLUMN, E.$PRICE_COLUMN, $PRICE_COLUMN
            FROM $MEMBER_TABLE_NAME M LEFT OUTER JOIN $EXPENSE_TABLE_NAME E ON
                M.$ID_COLUMN = E.$ID_COLUMN
        """

        const val IS_IN_REPOSITORY = """
            SELECT COUNT(*) FROM $MEMBER_TABLE_NAME WHERE ID = ?
        """
    }
}