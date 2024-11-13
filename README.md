# Flip Telegram Bot

Does a flip.

> https://t.me/FlipResponseBot

## Setup

The bot is started a system daemon process:

```bash
cat /etc/systemd/system/fliptbot.service
[Unit]
Description=Telegram Bot @FlipBot

[Service]
Type=simple
Restart=always
Environment=FLIPTBOT_OPTS="-DBOT_TOKEN=*** -DCOUNT_DIR=/root/telegrambots/fliptbot-boot-1.1/counter/ -DIMAGE_DIR=/root/telegrambots/fliptbot-boot-1.1/flip"
ExecStart=/root/telegrambots/fliptbot-boot-1.1/bin/fliptbot
User=root
```