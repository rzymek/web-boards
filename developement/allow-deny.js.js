if (Meteor.isServer) {
    Edit.allow({
        insert: function() {
            return true;
        },
        remove: function() {
            return true;
        },
        update: function() {
            return true;
        }

    });
}


