var placeMPS = function(value, hexId) {
    var hex = byId(hexId);
    if (!hex) {
        console.error(hexId, 'not found');
    }
    var s = placeSprite(sprites.mps, hex, 'traces');
    s.style.pointerEvents = 'none';
    setSpriteTexts(s, value);
};

var placeSprite = function(sprite, hex, layer) {
    layer = layer || 'overlays';
    var element = sprite.cloneNode(true);
    element.style.pointerEvents = 'auto';
    element.id += hex.id;
    copyTransformation(hex, element);
    byId(layer).appendChild(element);
    return element;
};

var setSpriteTexts = function(sprite/*, [test1], [text2], ...*/) {
    var index = 1;
    var args = arguments;
    toArray(sprite.getElementsByTagNameNS(SVGNS, 'tspan')).forEach(function(tspan) {
        var value = args[index++];
        if (value === undefined)
            value = '';
        tspan.textContent = value;
    });
    return sprite;
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
