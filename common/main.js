if (Meteor.isClient) {
    Operations.find().observeChanges({
        added: function (id, fields) {
//            console.log("operations added: ",id);
            var op = new PlaceOperation(fields);
            op.run();
        }
    });
}else{
    Operations.remove({});
}

