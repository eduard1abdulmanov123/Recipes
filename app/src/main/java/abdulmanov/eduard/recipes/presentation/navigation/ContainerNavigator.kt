package abdulmanov.eduard.recipes.presentation.navigation

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.*

open class ContainerNavigator(
    private val fragmentManager:FragmentManager,
    private val containerId:Int
):Navigator {

    override fun applyCommands(commands: Array<out Command>) {
        fragmentManager.executePendingTransactions()
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun applyCommand(command:Command){
        when(command){
            is Forward -> forward(command)
            is Back -> back(command)
        }
    }

    private fun forward(command: Forward){
        fragmentManager.beginTransaction().apply {
            val fragments = fragmentManager.fragments
            val currentFragment = fragments.lastOrNull()
            val newFragment = (command.screen as SupportAppScreen).fragment!!

            setupFragmentTransaction(command, currentFragment, newFragment, this)

            currentFragment?.let { hide(it) }
            add(containerId,newFragment)
        }.commit()
    }


    private fun back(command: Command){
        fragmentManager.beginTransaction().apply {
            val fragments = fragmentManager.fragments
            val currentFragment = fragments.lastOrNull()
            val newFragment = fragments.getOrNull(fragments.size-2)

            setupFragmentTransaction(command, currentFragment, newFragment, this)
            
            currentFragment?.let { remove(it) }
            newFragment?.let { show(it) }
        }.commit()
    }

    open fun setupFragmentTransaction(command: Command, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction) {
    }
}