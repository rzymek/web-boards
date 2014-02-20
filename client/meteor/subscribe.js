Meteor.subscribe('tables');

Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId !== null) {
        Meteor.subscribe('operations', tableId);
    }
});
