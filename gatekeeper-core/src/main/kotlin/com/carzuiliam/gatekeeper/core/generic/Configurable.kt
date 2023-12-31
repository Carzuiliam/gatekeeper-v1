package main.kotlin.com.carzuiliam.gatekeeper.core.generic

open class Configurable<T>(private val initFunction: ((T) -> Unit)? = null) {
    protected fun initializeInstance(param: T) {
        initFunction?.invoke(param)
    }
}