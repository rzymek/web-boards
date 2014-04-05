Template.playback.is = function(name) {
    return is(name);
};
function getReplayIdx() {
    var idx = Session.get('replayIndex');
    return (idx == null)
            ? idx = Operations.find({}).count()
            : idx;
}
playback = {
    rewindToStart: function() {
        Session.set('replayIndex', 0);
    },
    stepBack: function() {
        var idx = getReplayIdx();
        idx--;
        if (idx < 0)
            return;
        Session.set('replayIndex', idx);
    },
    stepForward : function() {
        var idx = getReplayIdx();
        idx++;
        if (idx > Operations.find({}).count())
            idx = null;
        Session.set('replayIndex', idx);
    }
};

Template.playback.events({
    'click .rewind-to-start': playback.rewindToStart,
    'click .play': function() {
        $('.playback .play').hide();
        $('.playback .pause').show();
    },
    'click .pause': function() {
        $('.playback .play').show();
        $('.playback .pause').hide();
    },
    'click .fast-rewind': function() {
        var idx = getReplayIdx();
        var current = Operations.find({}, {
            sort: {'createdAt': 1},
            skip: idx - 1,
            reactive: false
        });
        var rewindTo = Operations.findOne({
            createdAt: {$lt: current.createdAt},
            player: {$ne: current.player}
        }, {reactive: false});
        if (rewindTo == null) {
            Session.set('replayIndex', 0);
        } else {
            var rewindBy = Operations.find({
                $or: [
                    {createdAt: {$gte: rewindTo.createdAt}},
                    {createdAt: {$lt: current.createdAt}}
                ]
            }, {reactive: false}).count();
            Session.set('replayIndex', idx - rewindBy);
        }
    },
    'click .step-back': playback.stepBack,
    'click .fast-forward': function() {
    },
    'click .step-forward': playback.stepForward,
    'click .rewind-to-end': function() {
        Session.set('replayIndex', null);
    }
});