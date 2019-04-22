package uk.co.massimocarli.sqlitetodo.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item_layout.view.*
import uk.co.massimocarli.sqlitetodo.asString
import uk.co.massimocarli.sqlitetodo.entity.ToDo
import java.util.function.Consumer

typealias OnViewHolderClickListener<T> = Consumer<T>

class ToDoViewHolder(val view: View, listener: OnViewHolderClickListener<ToDo>? = null) :
  RecyclerView.ViewHolder(view) {

  lateinit var currentTodo: ToDo

  init {
    view.setOnClickListener {
      listener?.accept(currentTodo)
    }
  }

  fun bindModel(todo: ToDo) {
    currentTodo = todo
    view.todoTitle.text = todo.title
    view.todoDescription.text = todo.description
    view.todoDueDate.text = todo.dueDate.asString()
  }
}