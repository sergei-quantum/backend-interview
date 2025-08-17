package org.deblock.exercise.infrastructure.exceptions

/**
 * Runtime remote service exception class.
 * Can be used for exceptions to remote services
 */
class RemoteServiceException(message: String): RuntimeException(message)