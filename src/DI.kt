import java.lang.ref.WeakReference
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : Any> keyOf(): Class<T> = T::class.java

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Map<Class<*>, Scope<*>>.resolve(): T = (this[keyOf<T>()] as Scope<T>).value

inline fun kontainer(build: Builder.() -> Unit) =
    Kontainer(Builder().apply(build).deps)

class Kontainer(val deps: Map<Class<*>, Scope<*>>) {
    inline fun <reified T : Any> get(): T = deps.resolve()
}

class Builder {
    val deps = HashMap<Class<*>, Scope<*>>()
    inline fun <reified T : Any> resolve(): T = deps.resolve()
    inline fun <reified T : Any> scope(scope: () -> Scope<T>) {
        deps[keyOf<T>()] = scope()
    }
}

inline fun <reified T : Any> Builder.provider(crossinline producer: () -> T) = scope { Provider(producer) }
inline fun <reified T : Any> Builder.singleton(crossinline producer: () -> T) = scope { Singleton(producer) }
inline fun <reified T : Any> Builder.weak(crossinline producer: () -> T) = scope { Weak(producer) }

interface Injektor {
    val kontainer: Kontainer
}

inline fun <reified T : Any> injekt() = object : ReadOnlyProperty<Injektor, T> {
    override fun getValue(thisRef: Injektor, property: KProperty<*>): T = thisRef.kontainer.get()
}

interface Scope<T : Any> {
    val value: T
}

@Suppress("FunctionName")
inline fun <T : Any> Provider(crossinline producer: () -> T) = object : Scope<T> {
    override val value: T get() = producer()
}

@Suppress("FunctionName")
inline fun <T : Any> Singleton(crossinline producer: () -> T) = object : Scope<T> {
    private var bean: T? = null
    override val value: T get() = bean ?: producer().also { bean = it }
}

@Suppress("FunctionName")
inline fun <T : Any> Weak(crossinline producer: () -> T) = object : Scope<T> {
    private var beanRef: WeakReference<T>? = null
    override val value: T get() = beanRef?.get() ?: producer().also { beanRef = WeakReference(it) }
}
