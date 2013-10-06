/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/lib.d.ts" />
/// <reference path="../common/model.d.ts" />
/// <reference path="xml2js.d.ts" />

class BasicModule {
    private node: any;
    constructor(node:any) {
        this.node = node;
    }
    PlayerRoster():any { return this.node['VASSAL.build.module.PlayerRoster'];}
    GlobalProperties():any { return this.node['VASSAL.build.module.properties.GlobalProperties'];}
    Documentation():any { return this.node['VASSAL.build.module.Documentation'];}
    BasicCommandEncoder():any { return this.node['VASSAL.build.module.BasicCommandEncoder'];}
    DiceButton():any { return this.node['VASSAL.build.module.DiceButton'];}
    PrototypesContainer():any { return this.node['VASSAL.build.module.PrototypesContainer'];}
    Chatter():any { return this.node['VASSAL.build.module.Chatter'];}
    NotesWindow():any { return this.node['VASSAL.build.module.NotesWindow'];}
    PieceWindow():any { return this.node['VASSAL.build.module.PieceWindow'];}
    ChartWindow():any { return this.node['VASSAL.build.module.ChartWindow'];}
    Map(f:string,v:string):any {
        var map:any[]= this.node['VASSAL.build.module.Map'];
        return map.filter((e)=>{
            return e['$'][f]===v
        })[0];
    }
    Language():any { return this.node['VASSAL.i18n.Language'];}
    GamePieceImageDefinitions():any { return this.node['VASSAL.build.module.gamepieceimage.GamePieceImageDefinitions'];}
    GlobalOptions():any { return this.node['VASSAL.build.module.GlobalOptions'];}
    PredefinedSetup():any { return this.node['VASSAL.build.module.PredefinedSetup'];}
}

function fill(str:string, o) {
    return str.replace(/{([^{}]*)}/g,
        function (a, b) {
            var r = o[b];
            return typeof r === 'string' || typeof r === 'number' ? r : a;
        }
    );
};
function shorten(p:string):string{
    return p.substring(p.lastIndexOf('.') + 1)
}
interface NamedXML {
    typeName:string;
    xml:any;
}
function keys(o:{}) {
    var keys = [];
    for(var t in o) {
        keys.push(t);
    }
    return keys;
}

class Foo {
    private xml:any;
    private types = {};
    private result = '';

    constructor(xml:any) {
        this.xml = xml;
    }
    process():string {
        for (var tag in this.xml) {
            this.processType(tag, this.xml[tag]);
        }
        console.log(keys(this.types).join('\n'));
        return this.result;
    }

//    private processTypes() {
//        this.types.forEach((t)=>this.processType(t.typeName, t.xml));
//    }

    processType(tag:string, xml:any) {
        console.log('processType('+tag+')',xml);
//        if(Array.isArray(xml)) {
//            return
//        }
        for (var p in xml) {
            var shortName = shorten(p);
            var type = p === '$' ? tag+'Attributes' : shortName;
            if(typeof(xml[p])==='object' && !Array.isArray(xml[p])) {
                console.log('\t',type, p, typeof(xml[p]), xml[p].length);
                this.types[type] = xml[p];
                this.processType(type, xml[p]);
            }
        }
    }
//
//    processType1(tag:string, xml) {
//        var s = 'class ' + tag + ' {\n';
//        for (var p in xml) {
//            var shortName = shorten(p);
//            var type = p === '$' ? 'BasicModuleAttributes' : shortName;
//            s += fill("   {shortName}:{type} { return this.node['{name}'];};\n", {
//                shortName: shortName,
//                type: type,
//                name: p
//            });
//            this.types.push({typeName: type, xml: xml[p]});
//        }
//        s += '}\n';
//        this.processTypes();
//        this.result += s;
//    }
}

Meteor.startup(function () {
    Meteor.startup(function () {
        Operations.remove({});
        Operations.insert({name: 'move', args: {what: 'cnt', to: '5.35'}});
    });
    var buildFile = Assets.getText('bastogne/buildFile');
    var xml = xml2js.parseStringSync(buildFile);
    var mod = new BasicModule(xml['VASSAL.launch.BasicModule']);
    var types = [];
    var s = "class BasicModule {\n";
    for (var p in xml['VASSAL.launch.BasicModule']) {
        var shortName = p.substring(p.lastIndexOf('.') + 1);
        var type = p === '$' ? 'BasicModuleAttributes' : shortName;
        s += fill("   {shortName}:{type} { return this.node['{name}'];};\n", {
            shortName: shortName,
            type: type,
            name: p
        });
        types.push(type);
    }
    s += "}";
    var foo = new Foo(xml);
//    console.log(mod.Map('mapName','Main Map'));
});

