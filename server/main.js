//Meteor.startup(function() {
//});
Meteor.publish('gamesSub',function() {
    var games = fs.readdirSync('../client/app/games');
    this.added('games', 'theid', {games:games})
    this.ready();
});