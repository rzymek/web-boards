unbindKeys = function() {
    _.each(Meteor.Keybindings._bindings, function(obj) {
        Meteor.Keybindings.removeOne(obj.key);
    });
};

bindKeys = function() {
    Meteor.Keybindings.add({
        '←': menu.Back,
        '→': menu.Fwd,
        'ctrl+z': menu.Undo,
        'ctrl+shift+z': function() {
            alert('redo not implemented yet');
        },
        'ctrl+shift+d': function(e) {
            dbg();
            e.preventDefault();
        },
        'ctrl+shift+s': function(e) {
            Session.set('boardImgOverride',
                    Session.get('boardImgOverride') ? null : '/img/board-sfw.jpg');
            e.preventDefault();
        },
        'Q': function() {
            byId('svg').zoom(+1);
        },
        'W': function() {
            byId('svg').zoom(-1);
        }
    });
};