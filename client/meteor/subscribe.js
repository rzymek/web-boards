Meteor.subscribe('tables', {
    onReady: function() {
        Session.set('tables.ready', true);
    },
    onError: function() {
        Session.set('tables.ready', false);
    }
});

Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (is('scenario.ready') && tableId !== null) {
        Meteor.subscribe('operations', tableId);
    }
});
