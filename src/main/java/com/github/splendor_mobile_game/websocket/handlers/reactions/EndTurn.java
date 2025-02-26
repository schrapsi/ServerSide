package com.github.splendor_mobile_game.websocket.handlers.reactions;

import com.github.splendor_mobile_game.database.Database;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.Messenger;
import com.github.splendor_mobile_game.websocket.handlers.Reaction;
import com.github.splendor_mobile_game.websocket.handlers.ReactionName;

/**
 * This reaction handles the request sent by a player to end their turn. The server sends a message of type `NEW_TURN_ANNOUNCEMENT` to all players, announcing the end of the current turn and selecting the player for the next turn. If a player sends an invalid request, the server sends a response only to the requester.
 * 
 * Example of a valid user request:
 * 
 * {
 *      "messageContextId": "02442d1b-2095-4aaa-9db1-0dae99d88e03",
 *      "type": "END_TURN"
 *      "data": {
 *          "userUuid": "6850e6c1-6f1d-48c6-a412-52b39225ded7"
 *      }
 * }
 * 
 * Example of a successful server announcement:
 * {
 *      "messageContextId": "02442d1b-2095-4aaa-9db1-0dae99d88e03",
 *      "type": "NEW_TURN_ANNOUNCEMENT",
 *      "result": "OK",
 *      "data": {
 *          "nextUserUuid": "6850e6c1-6f1d-48c6-a412-52b39225ded7"
 *      }
 * }
 *
 * Implementation details:
 * - The player who sent the message is identified by their WebSocket's connectionHashCode.
 * - The implementation should include logic to store information about whose turn it is and the order of turns.
 * 
 * This reaction is also responsible for detecting when the game ends. The game ends when one player has more than 15 points and the server needs to complete the current turn so that every player has done the same amount of actions.
 * 
 * If a player sends an invalid request, such as when it is not their turn or they are not in any game, the server sends a response only to the requester. For example:
 *
 * {
 *     "messageContextId": "02442d1b-2095-4aaa-9db1-0dae99d88e03",
 *     "type": "END_TURN_RESPONSE",
 *     "result": "FAILURE",
 *     "data": {
 *         "error": "You cannot end turn if it's not your turn!"
 *     }
 * }
 * 
 */
@ReactionName("END_TURN")
public class EndTurn extends Reaction {

    public EndTurn(int connectionHashCode, UserMessage userMessage, Messenger messenger, Database database) {
        super(connectionHashCode, userMessage, messenger, database);
    }

    @Override
    public void react() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'react'");
    }
    
}
