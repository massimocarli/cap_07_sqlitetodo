package uk.co.massimocarli.sqlitetodo.entity

import android.content.ContentValues
import androidx.core.content.contentValuesOf
import uk.co.massimocarli.sqlitetodo.db.ToDoDB
import java.util.*

/**
 * The entity of our DB
 */
class ToDo(
  val id: Long,
  val title: String,
  val description: String?,
  val dueDate: Date
)

fun ToDo.asContentValues(): ContentValues =
  contentValuesOf(
    ToDoDB.ToDo.FIELD_TITLE to title,
    ToDoDB.ToDo.FIELD_DESCRIPTION to description,
    ToDoDB.ToDo.FIELD_DUE_DATE to dueDate.time
  )