package com.github.elimxim.fliptbot

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.InputStream
import java.nio.file.Files
import kotlin.io.path.*

class FlipBot(
    private val botProperties: TelegramBotProperties,
    botOptions: DefaultBotOptions
) : TelegramLongPollingBot(botOptions, botProperties.token) {
    override fun getBotUsername(): String {
        return botProperties.username!!
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            if (isFlip(update.message)) {
                sendFlip(update)
            }
        }
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
        private val log: Logger = LoggerFactory.getLogger(FlipBot::class.java)
    }
}