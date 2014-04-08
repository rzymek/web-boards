var requestUniqSuffix = '?' + (new Date().getTime());
requestSuffix = function() {
    return requestUniqSuffix;
};

getCurrentUsername = function() {
    return Meteor.user().emails[0].address;
};


dateFmt = function(date) {
    function fmt(c, s) {
        while (s.toString().length < c) {
            s = '0' + s;
        }
        return s;
    }
    return date.getFullYear() + "-" + fmt(2, date.getMonth() + 1) + "-" + fmt(2, date.getDate()) + " "
            + fmt(2, date.getHours()) + ":" + fmt(2, date.getMinutes());
};

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};