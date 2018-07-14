# d2-item-find-notify

Get notified on discord about anything that your kolbot bot finds.

[![Build Status](https://travis-ci.com/jKi-joe/d2-item-find-notify.svg?branch=master)](https://travis-ci.com/jKi-joe/d2-item-find-notify)

# Prerequist
1. [Java](https://java.com/de/download/) installed
1. [Your own discor server](https://support.discordapp.com/hc/en-us/articles/204849977-How-do-I-create-a-server-)


# How to setup
1. [Download the jar file and properties file](https://github.com/jKi-joe/d2-item-find-notify/releases/latest)
     1. For windows users: [Download the start-item-find-notifier.bat](https://github.com/jKi-joe/d2-item-find-notify/releases/latest)
1. Make sure jar + properties file + start-item-find-notifier.bat file are in same folder
1. [Set up a web hook](https://support.discordapp.com/hc/en-us/articles/228383668-Intro-to-Webhooks)
    * Only the section `MAKING A WEBHOOK` is relevant
1. Open the property file
    1. Set the property to your kolbot logs folder
         * e.g. `itemfindnotifier.itemFileDirectory=C:/kolbot/trunk/d2bs/kolbot/logs`
         * **Please note: The path should contain `/` and not `\` (especially for Window Users)**
    1. Set the property to your discord webhook
         * e.g. `itemfindnotifier.discordWebhookUrl=https://discordapp.com/api/webhooks/YOUR_ID/YOUR_SECRET`
    1. Optional: Add filters if you like (see file comments for more info)
         * e.g. `itemfindnotifier.filters=Flawless Ruby,(magic) Ring,Sol Rune`
    1. Optional: Edit the prefix messages (see file comments for more info)
         * e.g. `itemfindnotifier.messagePrefixes=Awwwww yeah a new item: ,Awesome I found: ,Cool I just got: `
    1. Optional: Edit the suffix message:
         * e.g. `itemfindnotifier.messageSuffix=--------------------------------------------------------`
1. Double click the `start-item-find-notifier.bat`
     1. For non windows users: Use `java -jar item-find-notifier.jar` 
1. Enjoy :)
