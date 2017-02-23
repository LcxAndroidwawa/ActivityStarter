package activitystarter.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Element
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable

val INTENT = ClassName.get("android.content", "Intent")
val BUNDLE = ClassName.get("android.os", "Bundle")
val CONTEXT = ClassName.get("android.content", "Context")

fun getFieldType(element: Element): TypeMirror = element.asType()
        .let { if (it.kind == TypeKind.TYPEVAR) (it as TypeVariable).upperBound else it }

fun getSetterParameterType(element: Element): TypeMirror? = (element.asType()  as? ExecutableType)?.parameterTypes?.get(0)

inline fun MethodSpec.Builder.doIf(check: Boolean, f: MethodSpec.Builder.() -> Unit) = apply {
    if (check) f()
}

inline fun TypeSpec.Builder.doIf(check: Boolean, f: TypeSpec.Builder.() -> Unit) = apply {
    if (check) f()
}