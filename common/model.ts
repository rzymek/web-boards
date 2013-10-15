/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="model.api.d.ts"/>

interface Operation{

}
Operations = new Meteor.Collection<Operation>('operations');