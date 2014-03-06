getUsername = function(userId) {
    return Meteor.users.findOne(userId).emails[0].address;
};

