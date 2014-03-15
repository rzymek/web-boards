function notifySession(name) {
    return {
        onReady: function() {
            Session.set(name, true);
        },
        onError: function() {
            Session.set(name, false);
        }
    }
}
Meteor.subscribe('tables', notifySession('tables.ready'));

Deps.autorun(function(c) {
    Session.set('ops.ready', false);
    var tableId = Session.get('tableId');
    console.log('sprites.ready', 'board.ready', Session.get('sprites.ready'), Session.get('board.ready'), tableId);
    /* The board need to be fully ready before any Ops are executed */
    if (is('sprites.ready', 'board.ready', 'module.ready') && tableId) {
        console.log('sub ops...', tableId);
        Meteor.subscribe('operations', tableId, notifySession('ops.ready'));
    }
});
