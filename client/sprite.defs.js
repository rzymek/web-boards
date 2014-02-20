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