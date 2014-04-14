var placeSprite = function(sprite, hex) {
    var element = sprite.cloneNode(true);
    element.style.pointerEvents = 'auto';
    element.id += hex.id;
    copyTransformation(hex, element);
    byId('overlays').appendChild(element);
    return element;
};

var setSpriteTexts = function(sprite/*, [test1], [text2], ...*/) {
    var index = 1;
    var args = arguments;
    toArray(sprite.getElementsByTagNameNS(SVGNS, 'tspan')).forEach(function(tspan) {
        var value = args[index++] || '';
        tspan.textContent = value;
    });
};

var getKillRoll = function(info) {
    switch (info.artyType) {
        case ArtyType.GUNS_88:
            return 5;
        case ArtyType.OTHER:
            return 6;
        case ArtyType.YELLOW:
            return 4;
    }
};
