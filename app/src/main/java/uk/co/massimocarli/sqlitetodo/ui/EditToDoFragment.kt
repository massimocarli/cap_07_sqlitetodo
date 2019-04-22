package uk.co.massimocarli.sqlitetodo.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_edit_to_do.*
import kotlinx.android.synthetic.main.fragment_edit_to_do.view.*
import uk.co.massimocarli.sqlitetodo.R
import uk.co.massimocarli.sqlitetodo.asDate
import uk.co.massimocarli.sqlitetodo.asString
import uk.co.massimocarli.sqlitetodo.entity.ToDo

const val NEW_TODO_ID = -1L
private const val PARAM_TODO_ID = "todoId"

class EditToDoFragment : BaseDBFragment() {
  private var todoId: Long = NEW_TODO_ID

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      todoId = it.getLong(PARAM_TODO_ID, NEW_TODO_ID)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_edit_to_do, container, false)
    with(view) {
      saveButton.setOnClickListener {
        // We collect the data
        val name = todoName.text.toString()
        val description = todoDescription.text.toString()
        val dueDate = todoDueDate.text.toString().asDate()
        if (todoId == NEW_TODO_ID) {
          // We create the ToDo
          val newTodo = ToDo(-1, name, description, dueDate)
          crud.insert(newTodo)
        } else {
          // We update the corrent one
          val updatedTodo = ToDo(todoId, name, description, dueDate)
          crud.update(updatedTodo)
        }
        navigation.back()
      }
      deleteButton.setOnClickListener {
        crud.deleteById(todoId)
        navigation.back()
      }
    }
    return view
  }

  override fun onStart() {
    super.onStart()
    if (todoId != NEW_TODO_ID) {
      crud.findById(todoId).apply {
        todoName.setText(this?.title ?: "")
        todoDescription.setText(this?.description ?: "")
        todoDueDate.setText(this?.dueDate?.asString() ?: "")
      }
      deleteButton.visibility = View.VISIBLE
    } else {
      deleteButton.visibility = View.GONE
    }
  }

  companion object {
    @JvmStatic
    fun newInstance(todoId: Long = NEW_TODO_ID) =
      EditToDoFragment().apply {
        arguments = Bundle().apply {
          putLong(PARAM_TODO_ID, todoId)
        }
      }
  }
}
