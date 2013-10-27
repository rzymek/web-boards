var fs = require('fs');

function capitalise(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

fs.readFile('packages/session.js', 'utf8', function (err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }

    process(data);
});

function getReferences(data) {
    return data.split('\n').filter(function(line){
        return line.match('^[/][/][/]')
    }).join('\n');
}

function process(data) {
    eval(data);
    var references = getReferences(data);
    var ts = references+'\n' +
        'class TypedSession {\n'
    for (var p in Session) {
        ts += '   ' + p + "():" + Session[p] + " { return Session.get('" + p + "');}\n";
        ts += '   set' + capitalise(p) + "(v:" + Session[p] + ") { Session.set('" + p + "',v);}\n";
    }
    ts += '}\n' +
        'declare var S:TypedSession;\n' +
        'S = new TypedSession();\n';

    var tsd = references+'\n' +
        'declare class TypedSession {\n';
    for (var p in Session) {
        tsd += '   public ' + p + '():' + Session[p] + ';\n';
        tsd += '   public set' + capitalise(p) + '(v:' + Session[p] + '):void;\n';
    }
    tsd += '}\n' +
        'declare var S:TypedSession;';
    fs.writeFile("client/TypedSession.ts", ts, function (err) {
        if (err) console.log(err);
    });
    fs.writeFile("client/TypedSession.d.ts", tsd, function (err) {
        if (err) console.log(err);
    });
}