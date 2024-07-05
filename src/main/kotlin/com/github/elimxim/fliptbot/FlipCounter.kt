package com.github.elimxim.fliptbot

import jakarta.annotation.PostConstruct
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption.*

class FlipCounter(path: Path) {
    private val file: Path = path.resolve(FILE_NAME)

    @PostConstruct
    fun postConstruct() {
        if (Files.notExists(file)) {
            Files.createFile(file);
        }
    }

    fun increment() {
        synchronized(this) {
            val next = get() + 1
            Files.newOutputStream(file, WRITE, TRUNCATE_EXISTING).buffered(BUFFER_SIZE).use {
                val buffer = ByteBuffer.allocate(BUFFER_SIZE).putLong(next)
                it.write(buffer.array())
            }
        }
    }

    fun get(): Long {
        synchronized(this) {
            Files.newInputStream(file, READ).buffered(BUFFER_SIZE).use {
                val buffer = ByteArray(BUFFER_SIZE)
                val num = it.read(buffer)
                if (num == -1) {
                    return 0
                } else {
                    return ByteBuffer.wrap(buffer).getLong()
                }
            }
        }
    }

    companion object {
        private const val FILE_NAME = "flip.counter"
        private const val BUFFER_SIZE = 8
    }
}