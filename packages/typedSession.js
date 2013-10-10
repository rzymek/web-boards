var Session = {
    selectedGame: 'string',
    gameInfo: 'Module',
    games: 'string[]'
}

function capitalise(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

var ts = 'class TypedSession {\n'
for (var p in Session) {
    ts += '   ' + p + "():" + Session[p] + " { return Session.get('" + p + "');}\n";
    ts += '   set' + capitalise(p) + "(v:" + Session[p] + ") { Session.set('" + p + "',v);}\n";
}
ts += '}'

var v = 'window["S"] = new TypedSession();';

var tsd = 'declare class TypedSession {\n';
for (var p in Session) {
    tsd += '   public ' + p + '():' + Session[p]+';\n';
    tsd += '   public set' + capitalise(p) + '(v:'+Session[p]+'):void;\n';
}
tsd += '}';
console.log(ts);
console.log(tsd);
console.log(v);