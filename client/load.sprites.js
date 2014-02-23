$.get('/sprites.svg', function(data) {
    sprites = {
        defs: data.getElementsByTagName('defs')[0],
    };
    var elements = data.getElementById('tmpl').childNodes;
    for (var i = 0; i < elements.length; i++) {
        var e = elements[i];
        if (e.nodeType !== Element.ELEMENT_NODE)
            continue;
        sprites[e.id] = e;
    }
    Session.set('sprites.ready', true);
});

Deps.autorun(function() {
    if (!is('sprites.ready', 'board.ready'))
        return;
    var dest = svg.getElementsByTagName('defs')[0];
    var defs = sprites.defs.childNodes;
    for (var i = 0; i < defs.length; i++) {
        if (defs[i].nodeType !== Element.ELEMENT_NODE)
            continue;
        dest.appendChild(defs[i]);
    }
});  