import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T> key(): Class<T> = T::class.java

inline fun kontainer(build: Kontainer.Builder.() -> Unit) =
    Kontainer(Kontainer.Builder().apply(build).deps)

class Kontainer(val deps: Map<Class<*>, Any>) {
    inline fun <reified T : Any> get(): T = deps[key<T>()] as T

    class Builder {
        val deps = HashMap<Class<*>, Any>()

        inline fun <reified T : Any> provide(function: () -> T) {
            deps[key<T>()] = function()
        }

        inline fun <reified T : Any> get(): T = deps[key<T>()] as T
    }
}

interface Injektor {
    val kontainer: Kontainer
}

inline fun <reified T : Any> injekt() = object : ReadOnlyProperty<Injektor, T> {
    override fun getValue(thisRef: Injektor, property: KProperty<*>): T = thisRef.kontainer.get()
}