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
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    if (hex === null) {
        console.warn('Hex not found:', data.hexid);
    } else {
        var info = Session.get('gameInfo');
        var counter = document.createElementNS(SVGNS, 'image');
        counter.width.baseVal.value = info.counterDim.width;
        counter.height.baseVal.value = info.counterDim.height;
        counter.id = data._id;
        
        var cnt = find(info, data.category, data.name);
        console.log(cnt);
        counter.sides = cnt.images;
        counter.side = 0;
        counter.name = data.name;
        counter.category = data.category;
        counter.href.baseVal = '/games/' + getTable({fields:{game:1}}).game + '/images/' + counter.sides[0];
        svg.getElementById('counters').appendChild(counter);

        addToStack(hex, counter);
        alignStack(hex);
        return function() {
            removeFromStack(counter.position, counter);
            svg.getElementById('counters').removeChild(counter);
            alignStack(counter.position);
        };
    }
};