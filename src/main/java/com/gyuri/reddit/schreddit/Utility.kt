package com.gyuri.reddit.schreddit

import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Throws(NoSuchAlgorithmException::class)
fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    return String.format("%064x", BigInteger(1, digest.digest(this.toByteArray(StandardCharsets.UTF_8))))
}