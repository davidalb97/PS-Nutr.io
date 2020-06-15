package pt.isel.ps.g06.httpserver

import org.mockito.Mockito

/**
 * Calling Mockito.any() returns null on any non primitive/wrapper classes,
 * due to mockito not supporting kotlin, this utility method is used in most of the test params are that
 * not nullable.
 */
inline fun <reified T> anyNonNull(): T = Mockito.any(T::class.java)

/**
 * Calling Mockito.any() returns null on any non primitive/wrapper classes,
 * due to mockito not supporting kotlin, this utility method is used in most of the test params are that
 * not nullable.
 */
private fun <T> anyNonNull(type: Class<T>): T = Mockito.any(type)
