# kettle-rocketchat
Send messages from PDI to a rocketchat server.

## Installation
Download the zip file from the releases page and extract the `rocketchat-plugin`-folder to `plugins` in your kettle home directory. The icon will be visible if you don't change the names. 

It is tested on PDI version `8.0.0.0-28`.

## Usage

The Rocketchat step is available for transformations under the Utility category.

![Screenshot of Step dialog containing the fields as in the following table.](/screenshots/dialog.png?raw=true "Rocketchat step Dialog")

The step has the following options:


| Option                   | Description |
|--------------------------|-------------|
| Step name                | Unique name of the step in the current transformation. |
| Rocketchat URL           | The url of your rocketchat server. <br>Automatically appends api extension if not supplied. <br>Supports variable substitution. |
| Rocketchat username      | Username of the Account to send messages with. Ideally a bot account. <br>Supports variable substitution. |
| Rocketchat password      | Password associated with the Account.           <br>Supports variable substitution. |
| Target field             | Field containing the name of the channel or recipient. <br>Can send to public or private chats ("Channel" or "Group"). These have to be preceded with the "#" character, e.g. "#general". If the sign is not present, a private message will be sent to the given name. <br>Discussions are not supported.           |
| Message field            | Field containing the message.            |
| Custom alias / avatar    | Wheter the following two options should be accessed. These are only accepted by the server if the sender account holds the `bot`-role.            |
| Alias field              | Optional. <br>Field containing the alternative name of the sender. This will appear to the user as "**Custom Name** @real_bot_name". <br>Match to an empty string to omit.            |
| Avatar field             | Optional. <br>Field containing the alternative avatar of the sender. This can be a URL to an image or a emoji. If an image, the source must be accessable to the rocketchat server. If a emoji, it must be specified using a rocketchat emoji sequence with leading and trailing colon. e.g. `:warning:` <br>Match to an empty string to omit.            |
| Name of new field        | Name of the new field appended to the stream that contains the send status of each message.            |

## Output

A new boolean field will be added to the stream that contains the send success. `True` if the message was sent successfully, `False` otherwise. If the login process failed, the step will fail and raise an error saying the credentials were not correct.

## Language Support
Included are translations for:
* English
* German
