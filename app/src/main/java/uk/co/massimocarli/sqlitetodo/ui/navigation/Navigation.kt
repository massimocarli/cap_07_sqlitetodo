package uk.co.massimocarli.sqlitetodo.ui.navigation

import androidx.fragment.app.Fragment

interface Navigation {

  /**
   * Navigate to the given fragment with the given tag. It allows you to add the Fragment to
   * the backstack
   */
  fun replaceFragment(
    fragment: Fragment,
    backStackName: String? = null,
    tag: String? = null
  )

  fun back()
}