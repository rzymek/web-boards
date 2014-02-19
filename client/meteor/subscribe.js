Meteor.subscribe('tables');

Deps.autorun(function() {
    var tid = Session.get('tableId');
    if (tid !== null) {
        console.log("subscribe to ops " + tid);
        Meteor.subscribe('operations', tid);
    }
});
