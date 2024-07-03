package com.github.elimxim.fliptbot

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
open class TelegramBotConfig(private val botProperties: TelegramBotProperties) {

    @Bean
    open fun flipBot(): FlipBot {
        return FlipBot(botProperties, DefaultBotOptions())
    }

    @Bean
    open fun telegramBotsApi(flipBot: FlipBot): TelegramBotsApi {
        return TelegramBotsApi(DefaultBotSession::class.java).apply {
            registerBot(flipBot)
        }
    }
}