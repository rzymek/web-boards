sleep = function(sleepDuration) {
    var now = new Date().getTime();
    while (new Date().getTime() < now + sleepDuration) { /* do nothing */
    }
};

NoOp = {
    run: function() {
    },
    undo: function() {
    }
};
