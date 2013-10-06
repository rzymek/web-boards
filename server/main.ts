/// <reference path="../packages/meteor-typescript-libs/meteor.d.ts" />
/// <reference path="../common/model.d.ts" />

Meteor.startup(function () {
    Meteor.startup(function() {
        Operations.remove({});
        Operations.insert({name:'move', args:{what:'cnt', to:'5.35'}});
    });
});