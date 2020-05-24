package abdulmanov.eduard.recipes.presentation.navigation

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
            val newFragment = (command.screen as SupportAppScreen).fragment!!

            setupFragmentTransaction(
                command,
                fragments.lastOrNull(),
                newFragment,
                this
            )

            if(fragments.isNotEmpty()){
                hide(fragments.last())
            }
            add(containerId,newFragment)
        }.commit()
    }


    private fun back(command: Command){
        fragmentManager.beginTransaction().apply {
            val fragments = fragmentManager.fragments

            setupFragmentTransaction(
                command,
                fragments.lastOrNull(),
                fragments.getOrNull(fragments.size-2),
                this
            )

            remove(fragments.last())
            if(fragments.size>1)
                show(fragments[fragments.size - 2])
        }.commit()
    }

    open fun setupFragmentTransaction(
        command: Command,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction
    ) {
    }
}