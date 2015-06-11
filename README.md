# Time Tracker
Time Tracker is a self educational application for timesheet tracking. The application mainly has three parts:
- a JEE7 REST server, connecting to a database for storing users, projects and timesheets, as well as an WebSocket interface to notify registered clients about status changes.
- a common library with interfaces, entities and exceptions.
- a AngularJS web client that is consuming the servers's REST API and registers for status changes.

# Time Tracker - Websocket IMPL
## Main desicions
The ability to use `WebSocket`s for instant communication between client and server is implemented outside the server application.
This design is based on `Events` which the server will send to notify any registered client about status changes.

The concept of events in the context of `JEE` is covered by `CDI`.

This way it would be possible to deploy the server without the `WebSocket` deployment unit.
In this case the client browser would receive no server events. The server itself would fire events, there is just no consumer.

Therefore, this could be called a `plugin` for the `Timetracker Server`.

The `timetracker-websocket-imp` project provides the classes that are responsible for event handling, which includes consuming events and forwaring the messages to the processing classes.

## Used software and versions
- IDE : Oracle NetBeans 8.0.2 (https://netbeans.org/downloads/, choose the `Java EE` edition)

The IDE is allready installed for the development of `Timetracker Server`.

For the following descriptions the base installation directory `\time-tracker` is assumed.

## Development
The directory structure described below is like this:
```
\time-tracker
   \timetracker-websocket-impl
```

### Requirements
1. Download the `timetracker-websocket-impl` project from https://github.com/apatrikis/timetracker-websocket-impl

In case NetBeans IDE is not already installed refer to `timetracker-server`.


### Initial configuration
This is covered by `timetracker-server`, no special setup is required.

## Test
Currently there are no `Unit test` for this project.
The classes are involved in test for the `timetracker-server`.

## Build (CI)
For `CI` the `Maven POM` is to be used within `Jenkins`.
For more information see the `timetracker-server` project.

## Run
The packaging type of this project is `JAR`.
In case web sockets should be available for the `timetracker-server` the artifact needs to be included in the `timetracker-server` artifact.

To achieve this the project has to be added as a `runtime` dependendy in the `timetracker-server` `pom`.

```
<dependency>
    <groupId>com.prodyna.pac</groupId>
    <artifactId>timetracker-websocket-impl</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>runtime</scope>
</dependency>
```
