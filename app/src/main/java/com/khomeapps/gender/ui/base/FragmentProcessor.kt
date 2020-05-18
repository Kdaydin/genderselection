package com.khomeapps.gender.ui.base

import android.content.Context
import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference

class FragmentProcessor(
    private val fragmentManager: WeakReference<FragmentManager>, private val containerId: Int,
    private val context: WeakReference<Context>
) {
    fun clearBackStack() {
        if (fragmentManager.get()!!.backStackEntryCount > 0)
            fragmentManager.get()!!.popBackStack(
                fragmentManager.get()!!.getBackStackEntryAt(0).name,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        val fragment = fragmentManager.get()!!.findFragmentById(containerId)
        fragment?.let { fra ->
            fragmentManager.get()!!.beginTransaction().remove(fra).commitAllowingStateLoss()
        }

    }

    fun getBackStackEntryCount() = fragmentManager.get()!!.backStackEntryCount

    fun getCurrentFragment(): BaseFragment<*, *>? {
        val currentFragment = fragmentManager.get()!!.findFragmentById(containerId)
        return if (currentFragment != null) {
            (currentFragment as BaseFragment<*, *>)
        } else {
            null
        }
    }

    fun add(fragment: BaseFragment<*, *>) {
        add(fragment, null)
    }

    fun add(fragment: BaseFragment<*, *>, clazz: Class<out BaseFragment<*, *>>) {
        add(fragment, clazz.simpleName)
    }

    fun add(fragment: BaseFragment<*, *>, tag: String?) {
        val fragmentTransaction = fragmentManager.get()!!.beginTransaction()
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.replace(containerId, fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun replaceFragment(fragment: BaseFragment<*, *>) {
        val fragmentTransaction = fragmentManager.get()!!.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun remove(fragment: BaseFragment<*, *>) {
        val fragmentTransaction = fragmentManager.get()!!.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun back() {
        val fragmentTransaction = fragmentManager.get()!!.beginTransaction()
        if (getCurrentFragment() != null) {
            fragmentTransaction.remove(getCurrentFragment()!!)
        }
        fragmentTransaction.commitAllowingStateLoss()
        fragmentManager.get()!!.popBackStack()
    }

    fun backTo(clazz: Class<out BaseFragment<*, *>>) {
        fragmentManager.get()!!.popBackStack(clazz.simpleName, 0)
    }
}