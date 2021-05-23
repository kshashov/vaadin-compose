package io.github.kshashov.vaadincompose.example

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Description(val value: String = "")
