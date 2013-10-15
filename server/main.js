/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/lib.d.ts" />
/// <reference path="../common/model.d.ts" />
/// <reference path="xml2js.d.ts" />
var BasicModule = (function () {
    function BasicModule(node) {
        this.node = node;
    }
    BasicModule.prototype.PlayerRoster = function () {
        return this.node['VASSAL.build.module.PlayerRoster'];
    };
    BasicModule.prototype.GlobalProperties = function () {
        return this.node['VASSAL.build.module.properties.GlobalProperties'];
    };
    BasicModule.prototype.Documentation = function () {
        return this.node['VASSAL.build.module.Documentation'];
    };
    BasicModule.prototype.BasicCommandEncoder = function () {
        return this.node['VASSAL.build.module.BasicCommandEncoder'];
    };
    BasicModule.prototype.DiceButton = function () {
        return this.node['VASSAL.build.module.DiceButton'];
    };
    BasicModule.prototype.PrototypesContainer = function () {
        return this.node['VASSAL.build.module.PrototypesContainer'];
    };
    BasicModule.prototype.Chatter = function () {
        return this.node['VASSAL.build.module.Chatter'];
    };
    BasicModule.prototype.NotesWindow = function () {
        return this.node['VASSAL.build.module.NotesWindow'];
    };
    BasicModule.prototype.PieceWindow = function () {
        return this.node['VASSAL.build.module.PieceWindow'];
    };
    BasicModule.prototype.ChartWindow = function () {
        return this.node['VASSAL.build.module.ChartWindow'];
    };
    BasicModule.prototype.Map = function (f, v) {
        var map = this.node['VASSAL.build.module.Map'];
        return map.filter(function (e) {
            return e['$'][f] === v;
        })[0];
    };
    BasicModule.prototype.Language = function () {
        return this.node['VASSAL.i18n.Language'];
    };
    BasicModule.prototype.GamePieceImageDefinitions = function () {
        return this.node['VASSAL.build.module.gamepieceimage.GamePieceImageDefinitions'];
    };
    BasicModule.prototype.GlobalOptions = function () {
        return this.node['VASSAL.build.module.GlobalOptions'];
    };
    BasicModule.prototype.PredefinedSetup = function () {
        return this.node['VASSAL.build.module.PredefinedSetup'];
    };
    return BasicModule;
})();

function fill(str, o) {
    return str.replace(/{([^{}]*)}/g, function (a, b) {
        var r = o[b];
        return typeof r === 'string' || typeof r === 'number' ? r : a;
    });
}
;
function shorten(p) {
    return p.substring(p.lastIndexOf('.') + 1);
}

function keys(o) {
    var keys = [];
    for (var t in o) {
        keys.push(t);
    }
    return keys;
}

var Foo = (function () {
    function Foo(xml) {
        this.types = {};
        this.result = '';
        this.xml = xml;
    }
    Foo.prototype.process = function () {
        for (var tag in this.xml) {
            this.processType(tag, this.xml[tag]);
        }
        console.log(keys(this.types).join('\n'));
        return this.result;
    };

    //    private processTypes() {
    //        this.types.forEach((t)=>this.processType(t.typeName, t.xml));
    //    }
    Foo.prototype.processType = function (tag, xml) {
        console.log('processType(' + tag + ')', xml);

        for (var p in xml) {
            var shortName = shorten(p);
            var type = p === '$' ? tag + 'Attributes' : shortName;
            if (typeof (xml[p]) === 'object' && !Array.isArray(xml[p])) {
                console.log('\t', type, p, typeof (xml[p]), xml[p].length);
                this.types[type] = xml[p];
                this.processType(type, xml[p]);
            }
        }
    };
    return Foo;
})();

Meteor.startup(function () {
    Meteor.startup(function () {
        Operations.remove({});
        Operations.insert({ name: 'move', args: { what: 'cnt', to: '5.35' } });
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
