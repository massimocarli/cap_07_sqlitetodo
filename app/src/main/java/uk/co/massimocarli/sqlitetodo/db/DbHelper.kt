package uk.co.massimocarli.sqlitetodo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uk.co.massimocarli.sqlitetodo.R
import uk.co.massimocarli.sqlitetodo.asString

class DbHelper(
  val context: Context
) : SQLiteOpenHelper(context, ToDoDB.DB_NAME, ToDoCursorFactory(), ToDoDB.DB_VERSION) {

  override fun onCreate(db: SQLiteDatabase) {
    try {
      db.beginTransaction()
      val createSQL = context.resources.openRawResource(R.raw.create_schema)
      db.execSQL(createSQL.asString())
      db.setTransactionSuccessful()
    } finally {
      db.endTransaction()
    }
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    try {
      db.beginTransaction()
      val dropSQL = context.resources.openRawResource(R.raw.drop_schema)
      db.execSQL(dropSQL.asString())
      onCreate(db)
      db.setTransactionSuccessful()
    } finally {
      db.endTransaction()
    }
  }
}