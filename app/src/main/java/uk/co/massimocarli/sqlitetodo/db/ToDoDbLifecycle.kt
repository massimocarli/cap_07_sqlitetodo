package uk.co.massimocarli.sqlitetodo.db

import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import uk.co.massimocarli.sqlitetodo.R
import uk.co.massimocarli.sqlitetodo.asDate
import uk.co.massimocarli.sqlitetodo.asString
import uk.co.massimocarli.sqlitetodo.entity.ToDo


class ToDoDbLifecycle(
  val context: Context,
  toDoFactory: SQLiteDatabase.CursorFactory? = null
) : DBLifecycle(ToDoDB.DB_NAME, ToDoDB.DB_VERSION, toDoFactory), Crud<ToDo> {
  override fun initializeDb(sqLiteDatabase: SQLiteDatabase, dbName: String, version: Int) {
    val createDbQuery = context.resources
      .openRawResource(R.raw.create_schema).asString()
    DatabaseUtils.createDbFromSqlStatements(
      context,
      dbName,
      version,
      createDbQuery
    )
  }

  override fun dbVersionChanged(
    sqLiteDatabase: SQLiteDatabase?,
    dbName: String,
    oldVersion: Int,
    newVersion: Int
  ) {
    sqLiteDatabase?.apply {
      val dropDbQuery = context.resources
        .openRawResource(R.raw.drop_schema).asString()
      execSQL(dropDbQuery)
      initializeDb(this, dbName, newVersion)
    }
  }

  override fun insert(todo: ToDo): Long {
    if (todo.id == -1L) {
      return sqLiteDatabase.insert(
        ToDoDB.ToDo.TABLE_NAME,
        ToDoDB.ToDo.FIELD_DESCRIPTION,
        contentValuesOf(
          ToDoDB.ToDo.FIELD_TITLE to todo.title,
          ToDoDB.ToDo.FIELD_DESCRIPTION to todo.description,
          ToDoDB.ToDo.FIELD_DUE_DATE to todo.dueDate.time
        )
      )
    } else {
      return -1
    }
  }

  override fun update(todo: ToDo): Int {
    if (todo.id != -1L) {
      return sqLiteDatabase.update(
        ToDoDB.ToDo.TABLE_NAME,
        contentValuesOf(
          ToDoDB.ToDo.FIELD_TITLE to todo.title,
          ToDoDB.ToDo.FIELD_DESCRIPTION to todo.description,
          ToDoDB.ToDo.FIELD_DUE_DATE to todo.dueDate.time
        ),
        " ${ToDoDB.ToDo.FIELD_ID} = ?",
        arrayOf("${todo.id}")
      )
    } else {
      return 0
    }
  }

  override fun findById(id: Long): ToDo? {
    sqLiteDatabase.query(
      ToDoDB.ToDo.TABLE_NAME,
      null,
      " ${ToDoDB.ToDo.FIELD_ID} = ?",
      arrayOf("$id"),
      null,
      null,
      null
    ).use { cursor ->
      if (cursor.moveToNext()) {
        val title = cursor.getString(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_TITLE))
        val description = cursor.getString(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_DESCRIPTION))
        val dueDate = cursor.getLong(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_DUE_DATE))
        return ToDo(id, title, description, dueDate.asDate())
      }
    }
    return null
  }

  fun findByIdWithToDoCursor(id: Long): ToDo? {
    sqLiteDatabase.query(
      ToDoDB.ToDo.TABLE_NAME,
      null,
      " ${ToDoDB.ToDo.FIELD_ID} = ?",
      arrayOf("$id"),
      null,
      null,
      null
    ).use { cursor ->
      val asTodoCursor = cursor as ToDoCursorFactory.ToDoCursor
      if (asTodoCursor.moveToNext()) {
        return asTodoCursor.getToDo()
      }
    }
    return null
  }

  override fun list(): List<ToDo> {
    val todoList = mutableListOf<ToDo>()
    sqLiteDatabase.query(
      ToDoDB.ToDo.TABLE_NAME,
      null,
      null,
      null,
      null,
      null,
      null
    ).use { cursor ->
      while (cursor.moveToNext()) {
        val id = cursor.getLong(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_ID))
        val title = cursor.getString(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_TITLE))
        val description = cursor.getString(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_DESCRIPTION))
        val dueDate = cursor.getLong(cursor.getColumnIndex(ToDoDB.ToDo.FIELD_DUE_DATE))
        todoList.add(ToDo(id, title, description, dueDate.asDate()))
      }
    }
    return todoList
  }

  fun listWithToDoCursor(): List<ToDo> {
    val todoList = mutableListOf<ToDo>()
    sqLiteDatabase.query(
      ToDoDB.ToDo.TABLE_NAME,
      null,
      null,
      null,
      null,
      null,
      null
    ).use { cursor ->
      val asTodoCursor = cursor as ToDoCursorFactory.ToDoCursor
      while (asTodoCursor.moveToNext()) {
        todoList.add(asTodoCursor.getToDo())
      }
    }
    return todoList
  }

  override fun deleteById(id: Long): Int {
    return sqLiteDatabase.delete(
      ToDoDB.ToDo.TABLE_NAME,
      " ${ToDoDB.ToDo.FIELD_ID} = ?",
      arrayOf("$id")
    )
  }

}


