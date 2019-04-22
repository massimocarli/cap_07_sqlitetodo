package uk.co.massimocarli.sqlitetodo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import uk.co.massimocarli.sqlitetodo.db.Crud
import uk.co.massimocarli.sqlitetodo.db.CrudOwner
import uk.co.massimocarli.sqlitetodo.entity.ToDo
import uk.co.massimocarli.sqlitetodo.ui.navigation.Navigation

open class BaseDBFragment : Fragment() {

  protected lateinit var crud: Crud<ToDo>
  protected lateinit var navigation: Navigation

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val activityAsCrudOwner = activity as? CrudOwner<ToDo>
    if (activityAsCrudOwner != null) {
      crud = activityAsCrudOwner.getCrud() as Crud<ToDo>
    } else {
      throw IllegalStateException("DbLifecycleOwner Needed!")
    }
    val activityAsNavigation = activity as? Navigation
    if (activityAsNavigation != null) {
      navigation = activityAsNavigation
    } else {
      throw IllegalStateException("Navigation Needed!")
    }
  }

}