if (Meteor.isClient) {
    Template.debug.created = function() {
        Meteor.Keybindings.addOne('ctrl+shift+d', function(e) {
            Session.set('dbg', !is('dbg'));
            e.preventDefault();
        });
    };
    dbg = function() {
        $('#dbgModal').toggle();
    };

    Template.debug.session = function() {
        var result = [];
        var keys = Object.keys(Session.keys);
        for (var i in keys) {
            result.push({
                key: keys[i],
                value: Template.debug.json(Session.get(keys[i]))
            });
        }
        return result;
    };
    Template.debug.user = function() {
        return Template.debug.json(Meteor.user());
    };
    Template.debug.json = function(it) {
        return JSON.stringify(it, null, ' ');
    };
    Template.debug.tables = function() {
        return Tables.find();
    };
    Template.debug.ops = function() {
        return Operations.find();
    };
    var opsSub = null;
    Template.debug.events({
        'click .reset': function() {
            Meteor.call('reset');
        },
        'click .dbg-key': function(e) {
            $(e.currentTarget.nextElementSibling.firstElementChild).toggle();
        },
        'click .selectTable': function(e) {
            var id = e.currentTarget.getAttribute('value');
            if (opsSub) {
                opsSub.stop();
            }
            opsSub = Meteor.subscribe('operations', id);
        }
    });
}