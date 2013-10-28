if (Meteor.isClient) {
    Operations.find().observeChanges({
            added: function (id, fields) {
                var op = new PlaceOperation(fields);
                op.run();
            }
        }
    );
} else {
//   Operations.remove({});
}

