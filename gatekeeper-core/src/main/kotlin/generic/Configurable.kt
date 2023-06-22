package generic

open class Configurable<T>(private val initFunction: ((T) -> Unit)? = null) {
    protected fun initializeInstance(param: T) {
        initFunction?.invoke(param)
    }
}