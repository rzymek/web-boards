Template.playback.is = function(name) {
    return is(name);
};

Template.playback.events({
    'click .rewind-to-start': function() {
        Session.set('replayIndex', 0);
    },
    'click .fast-rewind': function() {
        var idx = Session.get('replayIndex');
        if (idx === null)
            idx = Operations.find({}).count();
        idx--;
        if (idx < 0)
            return;
        Session.set('replayIndex', idx);
    },
    'click .fast-forward': function() {
        var idx = Session.get('replayIndex');
        if (idx === null)
            return;
        idx++;
        if (idx > Operations.find({}).count())
            idx = null;
        Session.set('replayIndex', idx);
    },
    'click .rewind-to-end': function() {
        Session.set('replayIndex', null);
    }
});