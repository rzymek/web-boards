Session.setDefault('gameInfo', null);
Session.setDefault('tableId', null);
Session.setDefault('config', '');

Session.set('board.ready', false);
Session.set('sprites.ready', false);
Session.set('module.ready', false);
Session.set('tables.ready', false);
Session.set('ops.ready', false);

Session.set('welcome.commentEditing', false);

is = function() {
    var result = true;
    for (var i = 0; i < arguments.length; i++) {
        var key = arguments[i];
        if (!Session.equals(key, true)) {
            result = false;
        }
    }
    return result;
};
toggle = function(key) {
    Session.set(key, !is(key));
};