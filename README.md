# d2-item-find-notify

Get notified on discord about anything that your kolbot bot finds.

# Prerequist
1. [Java](https://java.com/de/download/) installed
1. [Your own discor server](https://support.discordapp.com/hc/en-us/articles/204849977-How-do-I-create-a-server-)


# How to setup
1. Download the jar file and the properties file (jar + properties file must be in same folder)
1. [Set up a web hook](https://support.discordapp.com/hc/en-us/articles/228383668-Intro-to-Webhooks)
1. Open the property file
    1. Set the property to your kolbot logs folder
         * e.g. `net.joe.itemFileDirectory=C:\\D2-Bot\\kolbot\\trunk\\d2bs\\kolbot\\logs`
         * **Note the double `\\`**
    1. Set the property to your discord webhook
         * e.g. `net.joe.discordWebhookUrl=https://discordapp.com/api/webhooks/YOUR_ID/YOUR_SECRET`
1. [Open a command line in the folder where the jar is](https://www.howtogeek.com/howto/windows-vista/stupid-geek-tricks-open-a-command-prompt-from-the-desktop-right-click-menu/)
1. Enter `java -jar item-find-notifier-0.0.1-SNAPSHOT.jar`
1. Enjoy :)
