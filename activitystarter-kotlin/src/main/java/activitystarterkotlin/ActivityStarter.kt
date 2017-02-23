package activitystarterkotlin

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import android.support.v4.app.Fragment as FragmentV4

inline fun <reified T : Any> extra(default: T, key: String? = null) = ArgumentFieldBinder(T::class, default, key)

inline fun <reified T : Any> extraNullable(key: String? = null) = ArgumentFieldBinderNullable(T::class, key)

class ArgumentFieldBinder<T : Any>(clazz: KClass<T>, default: T?, key: String? = null) : Binder<T>(clazz, default, key), ReadOnlyProperty<Any, T> {

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T = when (thisRef) {
        is Activity -> readIntentNotNull(thisRef.intent, property)
        is Fragment -> readBundleNotNull(thisRef.arguments, property)
        is FragmentV4 -> readBundleNotNull(thisRef.arguments, property)
        else -> throw Error("Type $thisRef is not allowing Kotlin delegate injection")
    }
}

class ArgumentFieldBinderNullable<T : Any>(clazz: KClass<T>, key: String? = null) : Binder<T>(clazz, null, key), ReadOnlyProperty<Any, T?> {

    override operator fun getValue(thisRef: Any, property: KProperty<*>): T? = when (thisRef) {
        is Activity -> readIntentNullable(thisRef.intent, property)
        is Fragment -> readBundleNullable(thisRef.arguments, property)
        is FragmentV4 -> readBundleNullable(thisRef.arguments, property)
        else -> throw Error("Type $thisRef is not allowing Kotlin delegate injection")
    }

    private fun readIntentNullable(i: Intent, property: KProperty<*>): T? {
        if(!i.hasExtra(getKey(property))) return null
        return readIntentNotNull(i, property)
    }

    private fun readBundleNullable(b: Bundle, property: KProperty<*>): T? {
        if(!b.containsKey(getKey(property))) return null
        return readBundleNotNull(b, property)
    }
}

@Suppress("UNCHECKED_CAST")
open class Binder<T : Any>(val clazz: KClass<T>, val default: T?, val key: String? = null) {
    protected fun readIntentNotNull(i: Intent, property: KProperty<*>): T {
        val key = getKey(property)
        return when (clazz.java.simpleName) {
            "Long" -> i.getLongExtra(key, default as? Long ?: -1L) as T
            "Int" -> i.getIntExtra(key, default as? Int ?: -1) as T
            "String" -> i.getStringExtra(key) as T? ?: (default as String?) as T
            "Boolean" -> i.getBooleanExtra(key, default as? Boolean ?: false) as T
            "Float" -> i.getFloatExtra(key, default as? Float ?: -1F) as T
            else -> when {
                clazz.java.isAssignableFrom(Parcelable::class.java) -> i.getParcelableExtra<Parcelable>(key) as T
                clazz.java.isAssignableFrom(Serializable::class.java) -> i.getSerializableExtra(key) as T
                else -> throw Error("Type not correct")
            }
        }
    }

    protected fun readBundleNotNull(b: Bundle, property: KProperty<*>): T {
        val key = getKey(property)
        return when (clazz.java.simpleName) {
            "Long" -> b.getLong(key, default as? Long ?: -1L) as T
            "Int" -> b.getInt(key, default as? Int ?: -1) as T
            "String" -> b.getString(key) as T? ?: (default as String?) as T
            "Boolean" -> b.getBoolean(key, default as? Boolean ?: false) as T
            "Float" -> b.getFloat(key, default as? Float ?: -1F) as T
            else -> when {
                clazz.java.isAssignableFrom(Parcelable::class.java) -> b.getParcelable<Parcelable>(key) as T
                clazz.java.isAssignableFrom(Serializable::class.java) -> b.getSerializable(key) as T
                else -> throw Error("Type not correct")
            }
        }
    }

    protected fun getKey(property: KProperty<*>) = key ?: "${property.name}Key"
}