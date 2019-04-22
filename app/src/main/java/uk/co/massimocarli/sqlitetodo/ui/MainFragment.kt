package uk.co.massimocarli.sqlitetodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import uk.co.massimocarli.sqlitetodo.R
import uk.co.massimocarli.sqlitetodo.entity.ToDo


class MainFragment : BaseDBFragment() {

  lateinit var adapter: ToDoListAdapter
  private val todoModel: MutableList<ToDo> = mutableListOf()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_main, container, false)
    view.fab.setOnClickListener {
      navigation.replaceFragment(
        EditToDoFragment.newInstance(),
        backStackName = "newToDo"
      )
    }
    return view
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    recyclerView.layoutManager = LinearLayoutManager(activity)
    val dividerItemDecoration = DividerItemDecoration(
      context, DividerItemDecoration.VERTICAL
    )
    recyclerView.addItemDecoration(dividerItemDecoration)
    adapter = ToDoListAdapter(todoModel, OnViewHolderClickListener { selectedToDo ->
      navigation.replaceFragment(
        EditToDoFragment.newInstance(selectedToDo.id),
        backStackName = "editToDo"
      )
    })
    recyclerView.adapter = adapter
  }

  override fun onStart() {
    super.onStart()
    val newToDoModel = crud.list()
    val diffResult = DiffUtil.calculateDiff(ToDoDiffCallback(todoModel, newToDoModel))
    diffResult.dispatchUpdatesTo(adapter)
    todoModel.clear();
    todoModel.addAll(newToDoModel);
  }
}