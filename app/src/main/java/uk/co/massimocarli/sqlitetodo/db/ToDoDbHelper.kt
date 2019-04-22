package uk.co.massimocarli.sqlitetodo.db

import android.content.Context
import androidx.core.content.contentValuesOf
import uk.co.massimocarli.sqlitetodo.entity.ToDo

class ToDoDbHelper(context: Context) {

  val dbOpenHelper: DbHelper

  init {
    dbOpenHelper = DbHelper(context)
  }

  fun insert(todo: ToDo): Long {
    if (todo.id == -1L) {
      return dbOpenHelper.writableDatabase.insert(
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

  fun update(todo: ToDo): Int {
    if (todo.id != -1L) {
      return dbOpenHelper.writableDatabase.update(
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

  fun findById(id: Long): ToDo? {
    dbOpenHelper.readableDatabase.query(
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


  fun listToDos(): List<ToDo> {
    val todoList = mutableListOf<ToDo>()
    dbOpenHelper.readableDatabase.query(
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

  fun deleteById(id: Long): Int {
    return dbOpenHelper.writableDatabase.delete(
      ToDoDB.ToDo.TABLE_NAME,
      " ${ToDoDB.ToDo.FIELD_ID} = ?",
      arrayOf("$id")
    )
  }
}