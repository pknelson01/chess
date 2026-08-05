# Phase 6: Game Play

--- 

## Commands the user can make:
1. Help
    - Displays text informing the user what actions they can take.
2. Redraw Chess Board
    - Redraws the chess board upon the user's request.
3. Leave
    - Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.
4. Make move
    - Allow the user to input what move they want to make. The board is updated to reflect the result of the move, and the board automatically updates on all clients involved in the game.
5. Resign
    - Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over. Does not cause the user to leave the game.
6. Highlight legal moves
    - Allows the user to input the piece for which they want to highlight legal moves. The selected piece's current square and all squares it can legally move to are highlighted. This is a local operation and has no effect on remote users' screens.

> [!WARNING] 
> I don't know how hard this will be but notifications are a feature I need to build out, see [Phase-6 Overview Notes](phase-6_overview.md)