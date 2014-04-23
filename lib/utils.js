var requestUniqSuffix = '?' + (new Date().getTime());
requestSuffix = function() {
    return requestUniqSuffix;
};

getCurrentUsername = function() {
    var user = Meteor.user();
    return user ? user.emails[0].address : null;
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

cloneArray = function(array) {
    return array.slice(0);
};

notNull = function(it) {
    return it;
};

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

/* https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/findIndex#Polyfill */
if (!Array.prototype.findIndex) {
    Object.defineProperty(Array.prototype, 'findIndex', {
        enumerable: false,
        configurable: true,
        writable: true,
        value: function(predicate) {
            if (this == null) {
                throw new TypeError('Array.prototype.find called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                if (i in list) {
                    value = list[i];
                    if (predicate.call(thisArg, value, i, list)) {
                        return i;
                    }
                }
            }
            return -1;
        }
    });
}