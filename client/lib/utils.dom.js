byId = function(id) {
    return id != undefined ? document.getElementById(id) : null;
};
toArray = function(collection) {
    var array = [];
    for (var i = 0, len = collection.length; i < len; i++) {
        array.push(collection[i]);
    }
    return array;
};

getNatural = function(e) {
    if (e.naturalWidth !== undefined && e.naturalHeight !== undefined) {
        return {width: e.naturalWidth, height: e.naturalHeight};
    } else {
        var img = new Image();
        img.src = e.src;
        return {width: img.width, height: img.height};
    }
};

isTouchDevice = function() {
    if (Session.get('config').indexOf('mobile') > 0) {
        return true;
    }
    return 'ontouchstart' in window || 'onmsgesturechange' in window;
};

removeChildren = function(node) {
    while (node.firstChild) {
        node.removeChild(node.firstChild);
    }
};

log = function() {
    console.log.apply(console, arguments);
    return arguments[0];
};

