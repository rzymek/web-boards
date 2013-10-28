/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/lib.d.ts" />
/// <reference path="../common/model.d.ts" />
Meteor.startup(function () {
});
Operations.allow({
    insert: function () {
        return true;
    },
    update: function () {
        return true;
    }
});
Operations.deny({
    insert: function (userId, doc) {
        doc.createdAt = new Date().valueOf();
        return false;
    },
    update: function (userId, doc, fieldNames, modifier) {
        if (modifier.$set) {
            modifier.$set.updated = new Date();
        }
        return false;
    }
});
Meteor.methods({
    'undo': function () {
        var last = Operations.findOne({}, {
            sort: { 'createdAt': -1 }
        });
        Operations.remove(last._id);
    }
});
