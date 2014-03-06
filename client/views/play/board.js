Template.status.status = function() {
    return Meteor.status();
};

Template.board.game = function() {
    return getGame();
};

