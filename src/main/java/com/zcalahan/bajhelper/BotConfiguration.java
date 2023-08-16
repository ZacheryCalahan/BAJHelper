package com.zcalahan.bajhelper;

/** Class where constants are stored. Will be changed to a general configuration file later. */
public interface BotConfiguration {
    /** Channel where bot commands are used */
    String botCommandChannel = "bot-commands";
    /** Channel where roles are selected */
    String roleReactChannel = "role-select";
    /** Channel of where the voice hub is located */
    String voiceHubChannel = "Hub (Join for Voice Channel)";
    /** Category of where the voice hub is located */
    String voiceHubCategory = "Hub Channels";
    /** Channel that houses the report feedback.*/
    String reportsChannel = "user-reports";

}
