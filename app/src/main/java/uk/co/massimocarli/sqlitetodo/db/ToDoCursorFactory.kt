package uk.co.massimocarli.sqlitetodo.db

import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteCursorDriver
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery
import uk.co.massimocarli.sqlitetodo.asDate
import uk.co.massimocarli.sqlitetodo.entity.ToDo

class ToDoCursorFactory : SQLiteDatabase.CursorFactory {

  class ToDoCursor(
    driver: SQLiteCursorDriver,
    editTable: String,
    query: SQLiteQuery
  ) : SQLiteCursor(driver, editTable, query) {

    val idIndex: Int
    val titleIndex: Int
    val descriptionIndex: Int
    val dueDateIndex: Int

    init {
      idIndex = getColumnIndex(ToDoDB.ToDo.FIELD_ID)
      titleIndex = getColumnIndex(ToDoDB.ToDo.FIELD_TITLE)
      descriptionIndex = getColumnIndex(ToDoDB.ToDo.FIELD_DESCRIPTION)
      dueDateIndex = getColumnIndex(ToDoDB.ToDo.FIELD_DUE_DATE)
    }

    fun getToDo() = ToDo(
      getLong(idIndex),
      getString(titleIndex),
      getString(descriptionIndex),
      getLong(dueDateIndex).asDate()
    )
  }

  override fun newCursor(
    db: SQLiteDatabase?,
    masterQuery: SQLiteCursorDriver,
    editTable: String,
    query: SQLiteQuery
  ): Cursor {
    if (ToDoDB.ToDo.TABLE_NAME == editTable) {
      return ToDoCursor(masterQuery, editTable, query)
    } else {
      throw IllegalArgumentException("Table $editTable not valid! Use ${ToDoDB.ToDo.TABLE_NAME}")
    }
  }
}