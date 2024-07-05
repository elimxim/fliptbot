package com.github.elimxim.fliptbot

import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.nio.file.Files
import kotlin.io.path.*

class FlipBot(
    private val botProperties: TelegramBotProperties,
    private val flipCounter: FlipCounter,
    botOptions: DefaultBotOptions
) : TelegramLongPollingBot(botOptions, botProperties.token) {
    override fun getBotUsername(): String {
        return botProperties.username!!
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            if (isTimesCommand(update.message)) {
                sendCounter(update)
            } else if (isFlip(update.message)) {
                sendFlip(update)
                flipCounter.increment()
            }
        }
    }

    private fun isTimesCommand(message: Message): Boolean {
        return message.hasText() && message.text == TIMES_COMMAND
    }

    private fun sendCounter(update: Update) {
        val counter = flipCounter.get()
        execute(SendMessage().apply {
            chatId = update.message.chatId.toString()
            text = counter.toString()
        })
    }

    private fun isFlip(message: Message): Boolean {
        return message.hasText() && botProperties.flipMessages.any {
            message.text.contains(it, ignoreCase = true)
        }
    }

    private fun sendFlip(update: Update) {
        val img = flipImages().random()
        Files.newInputStream(botProperties.imageDir!!.resolve(img)).use {
            execute(SendSticker().apply {
                chatId = update.message.chatId.toString()
                sticker = InputFile(it, img)
            })
        }
    }

    private fun flipImages(): List<String> {
        return botProperties.imageDir!!.listDirectoryEntries()
            .filter { it.isRegularFile() }
            .map { it.fileName.name }
    }

    companion object {
        private const val TIMES_COMMAND = "/times"
    }
}