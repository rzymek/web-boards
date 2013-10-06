/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/lib.d.ts" />
/// <reference path="../common/model.d.ts" />
/// <reference path="xml2js.d.ts" />

Meteor.startup(function () {
    Meteor.startup(function() {
        Operations.remove({});
        Operations.insert({name:'move', args:{what:'cnt', to:'5.35'}});
    });
    var buildFile = Assets.getText('bastogne/buildFile');
    var xml = xml2js.parseStringSync(buildFile);
    var map = <Array>xml['VASSAL.launch.BasicModule']['VASSAL.build.module.Map'];
    map.forEach((e)=>{
        console.log(e['VASSAL.build.module.map.BoardPicker'][0]['VASSAL.build.module.map.boardPicker.Board']);
    })
});

