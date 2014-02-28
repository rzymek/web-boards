Meteor.subscribe('tables', {
    onReady: function() {
        Session.set('tables.ready', true);
    }
});

Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId !== null) {
        Meteor.subscribe('operations', tableId);
    }
});
