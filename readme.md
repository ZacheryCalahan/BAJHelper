# Bytes and Joysticks Discord Bot

## What is this?
This is a discord bot that is coded in Java that shows off the basics of a Command Manager system and a role selection system.
The bot logs no information, and only stores the emoji string and role when you use the RoleSelectListener.

## Getting Started
In order to use this bot, it will require a token from your bot. 
This token is a System Enviroment Variable with the name *BAJ_TOKEN*.

To use the bot in your server, you will need a few predetermined channels and categories.
They are defined in the BotConfiguration class, and are as follows:

```
/** Channel where bot commands are used */
    String botCommandChannel = "bot-commands";
    /** Channel where roles are selected */
    String roleReactChannel = "role-select";
    /** Channel of where the voice hub is located */
    String voiceHubChannel = "Hub (Join for Voice Channel)";
    /** Category of where the voice hub is located */
    String voiceHubCategory = "Hub Channels";
```
