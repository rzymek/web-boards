web-boards
==========

Web application for playing online adaptation of board games.

Goals:
* online play as well as turn based (like PBEM)
* VASSAL modules import
* state on server - no need to send log files like in PBEL
* works on Desktop as well as Mobile 


Implementation
==============

Startup:
1) load sprites.svg
2) subscribe to Tables collection


On /play:tableId
----------------

1) subscribe to Operations(tableId)
2) find out what game this is: Tables.findOne(tableId).game
3) render appropriate board: /games/{game}/images/{board.image}
    a) attach sprites to board
4) execute Operations (observe)
5) check if user is in Table(tableId).players
    a) if not, ask if what to join -> Meteor.call('join', tableId)
