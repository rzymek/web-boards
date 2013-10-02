if (Meteor.isClient) {
    var board = {w:6800, h:4400};
    Template.main.game = 'bastogne';
    var hex2hex = { y:125.29, x:108.5};
    var A = 2 * 125.29 / Math.sqrt(3) / 100;
    var hexes = []
    for(var x=0;x<1;x++)  {
        hexes.push({
            x: 80 ,
            y: 65,
            scale: A
        })
    }
    Template.main.hexes = hexes;
    Template.main.hex = {
            x: 80 ,
            y: 65,
            scale: A        
    }
}

if (Meteor.isServer) {
  Meteor.startup(function () {
    // code to run on server at startup
  });
}

