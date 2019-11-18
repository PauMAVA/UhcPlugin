[![Build Status](https://dev.azure.com/pmachetti/pmachetti/_apis/build/status/PauMAVA.UhcPlugin?branchName=master)](https://dev.azure.com/pmachetti/pmachetti/_build/latest?definitionId=1&branchName=master)

# UhcPlugin
With this plugin you will be able to host UHC matches with your friends.

Plugin Version: 1.0
Supported Minecraft Version: 1.14.4

!IMPORTANT NOTE! || The version 1.0 of this plugin may contain bugs that affect the gameplay or produce unexpected exceptions. Please, note that if you found a bug I encourage you to report it in order to improve this plugin. || !IMPORTANT NOTE!

[Developer Note]
This plugin was inspired by the series UHC España organized by ElRichMC. If you want to contribute to the code don't doubt to contact me. The project is in its early days, so if you would want to see a new feature added to the plugin please do not doubt to post it. Any kind of constructive criticism is always appreciated in order to improve this project and my coding skills. Hope you like this plugin :).
[Developer Note]

## Features:
This plugin comes along with the following features:

- Create an UHC match divided in episodes so that anyone can record it (for YouTube or whatever) easily.
- Scoreboard with current time and episode information.
- Player life is shown at the Tab list.
- Create teams of a custom size.
- Team chat to communicate with your teammates or save coordinates.
- Global chat (you can use it by beginning your message with !).
- Message sender obfuscation so that the player cannot be easily identified.
- Achievement achiever name obfuscated so that then other players don't know who has done an advancement but they do know someone has.
- A totem is generated on a player's death location containing the player's head. (It can be used to craft a golden apple in a cheaper way).
- Custom craftings for golden apple and glistering melon slice.
- Custom music at the end of each episode. Resource pack.
- World border management. The border closes to force battle.
- Random teleport location. (All teams are guaranteed to be at least at 800 blocks of distance at the spawning moment.)
- Custom death messages.

## Commands:
The main command of this plugin is /uhc this command gives you access to all the other subcommands included within UhcPlugin.

## Start a game:
In order to start a game you must use the following command: /uhc start
Alongside this command you can specify the duration of the countdown to the start of the game in seconds.
For example: /uhc start 5 This will start the game after a 5 second verbose countdown.
If you do not provide any argument then the match will start with a default countdown of 10 seconds.

## Configure the game:
You can configure the match options from Minecraft Command Line. Use the subcommand /uhc config to set or get config options.

To get the actual configuration use /uhc config get. If you don't specify an option after the keyword "get" then the full configuration file will be displayed. If you provide an option only the state of that option will be printed. Example:
/uhc config get season

To set a configuration option use the following command structure: /uhc config <option> <newValue>.
Example: /uhc config season 1.

The available configuration options are:
season: Specify the season you are playing. This will be used in titles and scoreboards.
lang: The language that the plugin uses. This feature is actually under development and the only available option is en (English). Translations are welcome :)
chapter_length: The length in minutes of each chapter.
closable_border: Specify if the border has to be static (false) or it must close at a certain episode.
border_radius: The radius of the world border in blocks.
final_radius: The final border radius after it stops closing in blocks.
border_closing_episode: The episode the border must begin closing.

## Manage teams:
Team management can also be done through Minecraft command-line. The command acesser is: /uhc teams. With this command you can perform one of the following actions.

Create a team: /uhc teams register <teamName>
Delete a team: /uhc teams delete <teamName>
Add a player to a team: /uhc teams add <playerName> <teamName>
Kick a player from a team: /uhc teams kick <playerName> <teamName>
Set a team maximum size: /uhc teams setMaxSize <teamName> <newSize>

Note: the setMaxSize option will kick players from a team if the old team doesn't fit in the new size. It will do this by kicking the players that were added the latest first.
