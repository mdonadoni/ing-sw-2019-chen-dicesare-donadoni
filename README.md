# Prova Finale Ingegneria del Software 2019
## Gruppo AM26

- ###   10562633    Simone Chen ([@CPandaS](https://github.com/CPandaS))<br>simone.chen@mail.polimi.it
- ###   10529764    Federico Di Cesare ([@Kakasinho](https://github.com/Kakasinho))<br>federico.dicesare@mail.polimi.it
- ###   10560562    Marco Donadoni ([@mark03](https://github.com/mark03))<br>marco.donadoni@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

### Usage
<span style="color: red;">DISCLAIMER</span>: to maintain the repository clean and lightweight the JARs were published in the _Releases_ section (you can find them [here](https://github.com/mark03/ing-sw-2019-chen-dicesare-donadoni/releases/tag/v1.0))
- To run the tests: `mvn clean test`
- To build the application: `mvn clean package`

After building the application, you will find two jars inside the `target` directory called `AM26-server.jar` and `AM26-client.jar`.

All the following instructions will assume that both jars are in the current working directory.

#### Launching the server
To launch the server with default configuration: `java -jar AM26-server.jar`

Some server settings can be customized by command line parameters.
In particular:
- `--turnTimeout <value>` sets the timeout for a single turn of the game in seconds.
- `--lobbyTimeout <value>` sets the timeout in seconds before the start of a new match when at least 3 players are inside the lobby. If at some moment in time the lobby is full (5 players), the match starts immediately.
- `--singleActionTimeout <value>` sets the timeout for single actions that are done outside the turn (in particular respawn and the use of the tagback granade, since both of them are actions made outside of the turn of a player).
- `--skulls <value>` sets the number of skulls in a game (min: 1, max: 8).
- `--hostname <value>` sets the hostname or IP address of the server. This should always be used and should have the IP address/hostname of the server as value. This is needed to make RMI work on LAN.
- `--rmiPort <value>` sets the port where the RMI registry will be created.
- `--socketPort <value>` sets the port the server will be listening on for socket connections.
- `--help` shows the helper message.

Example of server running on a pc with IP address 10.0.0.1, RMI port 12345, socket port 8888, 3 skulls, 2 minutes turn timeout

`java -jar AM26-server.jar --turnTimeout 120 --rmiPort 12345 --socketPort 8888 --skulls 3 --hostname 10.0.0.1`

#### Launching the client
Regarding JavaFX, the JavaFX external dependency (version 12.0.1) should be placed inside the current working directory.

Regarding the CLI, a terminal with support for Unicode characters and ANSI color strings is needed. In Linux there shouldn't be any problem, on Windows we used _Cygwin_ (a terminal emulator, before running the jar please run `alias javac='javac -encoding UTF-8'` and `alias java='java -Dfile.encoding=UTF-8'`) .

To run the CLI version: `java -jar AM26-client.jar --hostname CLIENT_HOSTNAME --cli`

To run the GUI version: `java --module-path ./javafx-sdk-12.0.1/lib --add-modules=javafx.controls,javafx.fxml -jar AM26-client.jar --hostname CLIENT_HOSTNAME --gui`

There is also the possibility to run bots that will play randomly:
- with sockets: `java -jar AM26-client.jar --hostname CLIENT_HOSTNAME --socketBot --portBot SOCKET_PORT --serverBot SERVER_HOSTNAME`
- with RMI: `java -jar AM26-client.jar --hostname CLIENT_HOSTNAME --rmiBot --portBot RMI_PORT --serverBot SERVER_HOSTNAME`

Of course substitute `SERVER_HOSTNAME` with the hostname/IP address of the server, `CLIENT_HOSTNAME` with the hostname/IP address of the client, `RMI_PORT` with the port where the RMI Registry has been created by the server, `SOCKET_PORT` with the port on which the server is listening.