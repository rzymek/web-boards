Router.map(function() {
       this.route('debug', {
        path: '/dbg',
        template: 'debug'
    }); 
});

Template.debug.session = function() {
    var result = [];
    var keys = Object.keys(Session.keys);
    for (var i in keys) {
        result.push({
            key: keys[i],
            value: Session.get(keys[i])
        });
    }
    return result;
};

Template.debug.user = function() {
    return JSON.stringify(Meteor.user());
};

Template.debug.json = function(it) {
    return JSON.stringify(it);
};

Template.debug.tables = function() {
    return Tables.find();
};
Template.debug.ops = function() {
    return Operations.find();
};

var opsSub = null;
Template.debug.events({
    'click .selectTable': function(e) {
        var id = e.currentTarget.getAttribute('value');
        if (opsSub) {
            opsSub.stop();
        }
        opsSub = Meteor.subscribe('operations', id);
    }
});