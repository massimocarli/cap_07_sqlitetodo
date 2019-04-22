package uk.co.massimocarli.sqlitetodo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

interface DBLifecycleOwner {
  fun getDbLifecycle(): DBLifecycle
}

abstract class DBLifecycle(
  val dbName: String,
  val version: Int = 1,
  val factory: SQLiteDatabase.CursorFactory? = null
) {

  lateinit var sqLiteDatabase: SQLiteDatabase

  fun onAttach(context: Context) {
    if (!::sqLiteDatabase.isInitialized) {
      val dbFile = context.getDatabasePath(dbName)
      val existingDb = dbFile.exists()
      sqLiteDatabase = context.openOrCreateDatabase(
        dbName,
        Context.MODE_PRIVATE,
        factory
      )
      if (!existingDb) {
        initializeDb(sqLiteDatabase, dbName, version)
      } else {
        val oldVersion = sqLiteDatabase.version
        if (version != oldVersion) {
          dbVersionChanged(sqLiteDatabase, dbName, oldVersion, version)
        }
      }
      sqLiteDatabase.enableWriteAheadLogging()
    }
  }

  open fun dbVersionChanged(
    sqLiteDatabase: SQLiteDatabase?,
    dbName: String,
    oldVersion: Int,
    newVersion: Int
  ) {
  }

  /**
   * Invoked when we need to initialize the DB
   */
  abstract fun initializeDb(sqLiteDatabase: SQLiteDatabase, dbName: String, version: Int)

  fun onDetach() {
    sqLiteDatabase.close()
  }
}

