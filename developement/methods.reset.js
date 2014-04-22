if (Meteor.isServer) {
    Meteor.methods({
        reset: function() {
            console.log('FULL RESET');
            Operations.remove({});
            Tables.remove({});
            ErrorLog.remove({});
        }
    });
}