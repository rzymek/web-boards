function find(info, category, name) {
    for(i in info.pieces) {
        var group = info.pieces[i];
        if(group.category !== category) 
            continue;
        for(j in group.list) {
            var counter = group.list[j];
            if(counter.name === name) {
                return counter;
            }
        }
    }
    return null;
}

PlaceOp = function(data) {
    var svg = byId('svg');
    var hex = byId(data.hexid);
    if (hex === null) {
        console.warn('Hex not found:', data.hexid);
    } else {
        var info = Session.get('gameInfo');
        var counter = document.createElementNS(SVGNS, 'g');
        var contents = document.createElementNS(SVGNS, 'image');
        contents.width.baseVal.value = info.counterDim.width;
        contents.height.baseVal.value = info.counterDim.height;
        contents.x.baseVal.value = -contents.width.baseVal.value/2;
        contents.y.baseVal.value = -contents.height.baseVal.value/2;
        counter.id = data._id;
        
        var cnt = find(info, data.category, data.name);
        counter.sides = cnt.images;
        counter.side = 0;
        counter.name = data.name;
        counter.category = data.category;       
        contents.href.baseVal = '/games/' + getTable({fields:{game:1}}).game + '/images/' + counter.sides[0];
        counter.appendChild(contents);
        byId('counters').appendChild(counter);

        addToStack(hex, counter);
        alignStack(hex);
        return function() {
            removeFromStack(counter.position, counter);
            svg.getElementById('counters').removeChild(counter);
            alignStack(counter.position);
        };
    }
};