//Meteor.startup(function() {
//});

Meteor.methods({
   dirs: function() {
       var files = fs.readdirSync('../client/app/games');
       var s = process.cwd()+", "+files;
       console.log(s);
       return s;
   }
});