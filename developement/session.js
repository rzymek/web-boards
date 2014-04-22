if (Meteor.isClient) {
    UI.registerHelper('devMode', function() {
        return true;
    });
}