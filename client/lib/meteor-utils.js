is = function() {
    for (var i = 0; i < arguments.length; i++) {
        var key = arguments[i];
        if(!Session.equals(key, true)) {
            return false;
        }
    }
    return true;
}