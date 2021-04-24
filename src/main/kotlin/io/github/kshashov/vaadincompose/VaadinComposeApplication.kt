package io.github.kshashov.vaadincompose

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VaadinComposeApplication

fun main(args: Array<String>) {
	runApplication<VaadinComposeApplication>(*args)
}
