Tables.allow({
    insert: function(userId, doc) {
        doc.started = new Date().valueOf();
        return true;
    },
});

Operations.allow({
    insert: function() {
        return true;
    },
});

Operations.deny({
    insert: function(userId, doc) {
        doc.createdAt = new Date().valueOf();
        if (doc.result !== undefined) {
            console.warn('trying to insert doc with result');
            return true;
        }
        if (doc.server !== undefined) {
            //TODO: parse the actual request in doc.server
            doc.result = {
                roll: 1 + Math.floor(Math.random() * 6)
            };
            delete doc['server'];
            console.log('rolled', doc.roll);
        }
        return false;
    },
    update: function() {
        return true;
    },
    remove: function() {
        return true;
    }    
});

