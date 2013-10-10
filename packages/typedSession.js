var Session = {
    selectedGame: 'string',
    gameInfo: 'Module',
    games: 'string[]'
}

function capitalise(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

console.log('class TypedSession {')
for (var p in Session) {
    console.log('   ' + p + "():" + Session[p] + " { return Session.get('" + p + "');}");
}
console.log('');
for (var p in Session) {
    console.log('   set' + capitalise(p) + "(v:" + Session[p] + ") { Session.set('" + p + "',v);}");
}
console.log('}');
console.log('var s = new TypedSession();');
