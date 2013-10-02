if (Meteor.isClient) {
    Template.main.game = 'bastogne';
    Template.main.board = {w: 6800, h: 4400};
    Meteor.startup(function() {
        svgZAP.setup(document.getElementsByTagName("svg")[0]);
        console.log('started');
    });
}

if (Meteor.isServer) {
    Meteor.startup(function() {
        // code to run on server at startup
    });
}

