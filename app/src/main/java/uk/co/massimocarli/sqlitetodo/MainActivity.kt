package uk.co.massimocarli.sqlitetodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import uk.co.massimocarli.sqlitetodo.db.CPToDoCrud
import uk.co.massimocarli.sqlitetodo.db.Crud
import uk.co.massimocarli.sqlitetodo.db.CrudOwner
import uk.co.massimocarli.sqlitetodo.db.ToDoDbLifecycle
import uk.co.massimocarli.sqlitetodo.entity.ToDo
import uk.co.massimocarli.sqlitetodo.ui.MainFragment
import uk.co.massimocarli.sqlitetodo.ui.navigation.Navigation

class MainActivity : AppCompatActivity(), CrudOwner<ToDo>, Navigation {

  lateinit var localDbLifecycle: ToDoDbLifecycle
  lateinit var crudImpl: Crud<ToDo>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    localDbLifecycle = ToDoDbLifecycle(this)
    crudImpl = CPToDoCrud(this)
    if (savedInstanceState == null) {
      replaceFragment(MainFragment())
    }
    localDbLifecycle.onAttach(this)
  }

  override fun onDestroy() {
    localDbLifecycle.onDetach()
    super.onDestroy()
  }

  override fun getCrud(): Crud<ToDo> = crudImpl

  override fun replaceFragment(
    fragment: Fragment,
    backStackName: String?,
    tag: String?
  ) {
    supportFragmentManager.beginTransaction().apply {
      replace(R.id.anchor, fragment)
      backStackName?.let {
        addToBackStack(it)
      }
      commit()
    }
  }

  override fun back() {
    supportFragmentManager.popBackStack()
  }
}
