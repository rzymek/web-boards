Meteor.startup(function() {
    bindings['ctrl+shift+d'] = function(e) {
        dbg();
        e.preventDefault();
    };
    bindings['ctrl+shift+s'] = function(e) {
        Session.set('boardImgOverride', Session.get('boardImgOverride') ? null : '/img/board-sfw.jpg');
        e.preventDefault();
    };
});