package uk.co.massimocarli.sqlitetodo.db

import android.content.ContentResolver
import android.content.Context
import android.database.CursorWrapper
import android.net.Uri
import uk.co.massimocarli.sqlitetodo.entity.ToDo
import uk.co.massimocarli.sqlitetodo.entity.asContentValues
import uk.co.massimocarli.sqlitetodo.getId

class CPToDoCrud(context: Context) : Crud<ToDo> {

  val contentResolver: ContentResolver

  init {
    contentResolver = context.contentResolver
  }

  override fun insert(item: ToDo): Long =
    contentResolver.insert(
      ToDoDB.ToDo.CONTENT_URI,
      item.asContentValues()
    )?.getId() ?: -1

  override fun update(item: ToDo): Int {
    var uriToUpdate = Uri.withAppendedPath(
      ToDoDB.ToDo.CONTENT_URI,
      "${item.id}"
    )
    return contentResolver.update(
      uriToUpdate,
      item.asContentValues(),
      null,
      null
    )
  }

  override fun findById(id: Long): ToDo? {
    var uriToFind = Uri.withAppendedPath(
      ToDoDB.ToDo.CONTENT_URI,
      "$id"
    )
    val cursorWrapper = contentResolver.query(
      uriToFind,
      null,
      null,
      null,
      null,
      null
    ) as CursorWrapper
    val cursor = cursorWrapper.wrappedCursor as ToDoCursorFactory.ToDoCursor
    var todo: ToDo? = null
    if (cursor.moveToNext()) {
      todo = cursor.getToDo()
    }
    return todo
  }


  override fun list(): List<ToDo> {
    val cursorWrapper = contentResolver.query(
      ToDoDB.ToDo.CONTENT_URI,
      null,
      null,
      null,
      null,
      null
    ) as CursorWrapper
    val cursor = cursorWrapper.wrappedCursor as ToDoCursorFactory.ToDoCursor
    val todoList = mutableListOf<ToDo>()
    while (cursor.moveToNext()) {
      todoList.add(cursor.getToDo())
    }
    return todoList
  }

  override fun deleteById(id: Long): Int {
    var uriToDelete = Uri.withAppendedPath(
      ToDoDB.ToDo.CONTENT_URI,
      "$id"
    )
    return contentResolver.delete(
      uriToDelete,
      null,
      null
    )
  }
}