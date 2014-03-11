web-boards
==========

Web application for playing online adaptation of board games.

Goals:
* online play as well as turn based (like PBEM)
* VASSAL modules import
* state on server - no need to send log files like in PBEL
* works on Desktop as well as Mobile 


## Implementation

### Startup:

1. load sprites.svg
2. subscribe to Tables collection

### On /play:tableId

1. subscribe to Operations(tableId)
2. find out what game this is: Tables.findOne(tableId).game
3. render appropriate board: /games/{game}/images/{board.image}
  1. attach sprites to board
4. execute Operations (observe)
5. check if user is in Table(tableId).players
  1. if not, ask if what to join -> Meteor.call('join', tableId)

### Counters

Counters are represented by `SVGImageElement`.

##### Properties:

* `.position` - the hex (`SVGUseElement`) the counter is placed on

##### TransformList
`.transform` (`SVGTransformList`) items are in the follwing order:

1. translation
2. scale 
3. rotation 

All are optional, but if present must be in this order and on these positions.

### Hexes

Hexes are `SVGUseElement`-clones of a SVGPathElement. 
Hexes are positioned using translation transformation. 
x=0,y=0 of a hex is in it's center.

##### Properties:
* `.stack` Array of counters (`SVGImageElement`) on the hex. May be `undefined` if empty.