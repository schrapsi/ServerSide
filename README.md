# ServerSide
Server-side code refers to the code that runs on a server, which is responsible for handling requests from client-side applications, processing data, and generating responses. 

## What does this repository actually contain?
1. A WebSocket server which is responsible for:
   * handling requests from **ClientSide**.
   * storing local database
   * generating responses back to **ClientSide**.

2. The game model including:
   * all players
   * cards
   * tokens
   * ...

Game logic is being processed on the server based on responses sent by the user. That includes adding users, creating rooms and implementing game changes.
That's what makes our project server-side application.


### What are reactions?
The `websocket` package is a storage for the entire WebSocket server. That's where we add new `request handlers` similarly named `reactions`.
[Websocket reactions are located here](src/main/java/com/github/splendor_mobile_game/websocket/handlers/reactions).
Each reaction proceedes **JSON data** given by Client. 

#### Example Client request:


```json
{
     "messageContextId": "80bdc250-5365-4caf-8dd9-a33e709a0116",
     "type": "CREATE_ROOM",
     "data": {
         "roomDTO": {
             "name": "TajnyPokoj",
             "password": "Tajne6Przez2Poufne.;"
         },
         "userDTO": {
             "uuid": "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",
             "name": "James"
         }
     }
 }
```

This request's data is processed and then response is generated.

#### Example server response:

```json 
{
    "messageContextId":"80bdc250-5365-4caf-8dd9-a33e709a0116",
    "type":"CREATE_ROOM_RESPONSE",
    "result":"OK",
    "data": {
        "room":{
            "uuid":"0599e96b-7573-4a7a-9605-323dee718fc5",
            "name":"TajnyPokoj"
        },
        "user":{
            "uuid":"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",
            "name":"James"
        }
    }
}
```

## How to create a new reaction?
If you want to create a new reaction then head to the `reactions` package and create a new class. Make sure to add this class into ArrayList in App.java so that it will be registered as a reaction.
The reaction named **CreateRoom** inserts changes to game objects. Their structure is stored [here](src/main/java/com/github/splendor_mobile_game/game/model).

Basically, reactions are handling game logic which structure is implemented in package `game.model`.


## How to commit new code to repository?

There is a protected `main` branch which stores "production" code. 
In order to avoid code destroying, the default branch is `develop`. We don't want to work directly on it, because few users may be editing the same files.
Instead - you must create a new branch which will be a temporal storage for any additional code that you've created.
