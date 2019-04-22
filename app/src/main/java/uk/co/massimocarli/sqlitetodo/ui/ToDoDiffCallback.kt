package uk.co.massimocarli.sqlitetodo.ui

import androidx.recyclerview.widget.DiffUtil
import uk.co.massimocarli.sqlitetodo.entity.ToDo
import java.util.*

class ToDoDiffCallback(
  val newToDos: List<ToDo>,
  val oldToDos: List<ToDo>
) : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
    oldToDos[oldItemPosition].id == newToDos[newItemPosition].id

  override fun getOldListSize(): Int = oldToDos.size

  override fun getNewListSize(): Int = newToDos.size

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
    Objects.equals(oldToDos[oldItemPosition], newToDos[oldItemPosition])
}