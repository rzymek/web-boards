//Meteor.startup(function() {
//});
Meteor.publish('gamesSub',function() {
    this.added('games', 'theid', {games:games});
    this.ready();
});
Meteor.publish('operations',function() {
   return Operations.find();
});

Meteor.methods({
    games: function() {
        var games = fs.readdirSync('../client/app/games');
        console.log('avaiable games',games);
        return games;
    }
});
