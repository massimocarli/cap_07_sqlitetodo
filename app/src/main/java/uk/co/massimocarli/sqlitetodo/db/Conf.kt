package uk.co.massimocarli.sqlitetodo.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

class ToDoDB {

  companion object {
    val DB_NAME = "ToDoDB"

    val DB_VERSION = 1

    val AUTHORITY = "uk.co.massimocarli.sqlitetodo"
  }


  class ToDo : BaseColumns {

    companion object {
      val TABLE_NAME = "TODO"
      val PATH = "todo"
      val CONTENT_URI = Uri.parse(
        "${ContentResolver.SCHEME_CONTENT}" +
            "://$AUTHORITY" +
            "/$PATH"
      )

      val MIME_TYPE_DIR = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.$PATH"
      val MIME_TYPE_ITEM = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.$PATH"

      const val FIELD_ID = BaseColumns._ID
      const val FIELD_TITLE = "title"
      const val FIELD_DESCRIPTION = "description"
      const val FIELD_DUE_DATE = "dueDate"
    }
  }
}