bindings = {
    '←': function() {
        playback.stepBack();
    },
    '→': function() {
        playback.stepForward();
    },
    'ctrl+z': function() {
        menu.Undo();
    },
    'Q/+': function() {
        byId('svg').zoom(+1);
    },
    'W/-': function() {
        byId('svg').zoom(-1);
    }
};

bindKeys = function() {
    Meteor.Keybindings.add(bindings);
};

unbindKeys = function() {
    _.each(Meteor.Keybindings._bindings, function(obj) {
        Meteor.Keybindings.removeOne(obj.key);
    });
};