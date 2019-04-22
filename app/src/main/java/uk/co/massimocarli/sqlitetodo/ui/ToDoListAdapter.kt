package uk.co.massimocarli.sqlitetodo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.co.massimocarli.sqlitetodo.R
import uk.co.massimocarli.sqlitetodo.entity.ToDo


class ToDoListAdapter(
  val model: List<ToDo>,
  val listener: OnViewHolderClickListener<ToDo>? = null
) : RecyclerView.Adapter<ToDoViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
    val todoItemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.todo_item_layout, parent, false)
    return ToDoViewHolder(todoItemView, listener)
  }

  override fun getItemCount(): Int =
    model.size

  override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) =
    holder.bindModel(model[position])
}
