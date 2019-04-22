package uk.co.massimocarli.sqlitetodo.db

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class ToDoContentProvider : ContentProvider() {

  companion object {
    private const val TODO_DIR_INDICATOR = 1
    private const val TODO_ITEM_INDICATOR = 2
    private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
      addURI(ToDoDB.AUTHORITY, ToDoDB.ToDo.PATH, TODO_DIR_INDICATOR)
      addURI(ToDoDB.AUTHORITY, "${ToDoDB.ToDo.PATH}/#", TODO_ITEM_INDICATOR)
    }
  }

  lateinit var dbHelper: DbHelper

  override fun onCreate(): Boolean {
    dbHelper = DbHelper(context)
    return true
  }

  override fun getType(uri: Uri): String = when (URI_MATCHER.match(uri)) {
    TODO_DIR_INDICATOR -> ToDoDB.ToDo.MIME_TYPE_DIR
    TODO_ITEM_INDICATOR -> ToDoDB.ToDo.MIME_TYPE_ITEM
    else -> throw IllegalArgumentException("$uri not valid")
  }


  override fun insert(uri: Uri, values: ContentValues?): Uri? {
    if (URI_MATCHER.match(uri) == TODO_DIR_INDICATOR) {
      val db = dbHelper.writableDatabase
      val newToDoId = db.insert(
        ToDoDB.ToDo.TABLE_NAME,
        ToDoDB.ToDo.FIELD_DESCRIPTION,
        values
      )
      if (newToDoId > 0) {
        val newToDoUri = ContentUris.withAppendedId(
          ToDoDB.ToDo.CONTENT_URI,
          newToDoId
        )
        context.contentResolver.notifyChange(newToDoUri, null)
        return newToDoUri
      }
      return null
    } else {
      throw IllegalArgumentException("$uri not valid")
    }
  }

  override fun query(
    uri: Uri,
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?,
    sortOrder: String?
  ): Cursor? {
    var cursor: Cursor? = null
    var whereClause: String?
    val db = dbHelper.readableDatabase
    when (URI_MATCHER.match(uri)) {
      TODO_DIR_INDICATOR -> {
        cursor = db.query(
          ToDoDB.ToDo.TABLE_NAME,
          projection,
          selection,
          selectionArgs,
          null,
          null,
          sortOrder,
          null
        )
      }
      TODO_ITEM_INDICATOR -> {
        whereClause = "${ToDoDB.ToDo.FIELD_ID} = ${uri.pathSegments[1]}"
        selection?.let {
          whereClause += " AND ($it)"
        }
        cursor = db.query(
          ToDoDB.ToDo.TABLE_NAME,
          projection,
          whereClause,
          selectionArgs,
          null,
          null,
          sortOrder,
          null
        )
      }
    }
    cursor?.let {
      it.setNotificationUri(context.contentResolver, ToDoDB.ToDo.CONTENT_URI)
    }
    return cursor
  }

  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<String>?
  ): Int {
    var whereClause: String?
    val db = dbHelper.writableDatabase
    var updateNumber = 0
    when (URI_MATCHER.match(uri)) {
      TODO_DIR_INDICATOR -> {
        updateNumber = db.update(
          ToDoDB.ToDo.TABLE_NAME,
          values,
          selection,
          selectionArgs
        )
      }
      TODO_ITEM_INDICATOR -> {
        whereClause = "${ToDoDB.ToDo.FIELD_ID} = ${uri.pathSegments[1]}"
        selection?.let {
          whereClause += " AND ($it)"
        }
        updateNumber = db.update(
          ToDoDB.ToDo.TABLE_NAME,
          values,
          whereClause,
          selectionArgs
        )
      }
    }
    if (updateNumber > 0) {
      context.contentResolver.notifyChange(uri, null)
    }
    return updateNumber
  }

  override fun delete(
    uri: Uri,
    selection: String?,
    selectionArgs: Array<String>?
  ): Int {
    var whereClause: String?
    val db = dbHelper.writableDatabase
    var deleteNumber = 0
    when (URI_MATCHER.match(uri)) {
      TODO_DIR_INDICATOR -> {
        deleteNumber = db.delete(
          ToDoDB.ToDo.TABLE_NAME,
          selection,
          selectionArgs
        )
      }
      TODO_ITEM_INDICATOR -> {
        whereClause = "${ToDoDB.ToDo.FIELD_ID} = ${uri.pathSegments[1]}"
        selection?.let {
          whereClause += " AND ($it)"
        }
        deleteNumber = db.delete(
          ToDoDB.ToDo.TABLE_NAME,
          whereClause,
          selectionArgs
        )
      }
    }
    if (deleteNumber > 0) {
      context.contentResolver.notifyChange(uri, null)
    }
    return deleteNumber
  }

}