/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="model.api.d.ts"/>

interface Operation{
    run():void;
    undo():void;
}
Operations = new Meteor.Collection<any>('operations');