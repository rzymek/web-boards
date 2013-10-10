//Meteor.startup(function() {
//});
Meteor.publish('games',function() {
    return fs.readdirSync('../client/app/games');
});