NoteOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var note = sprites.notePopup.cloneNode(true);
    note.id = data._id;
    note.getElementsByTagNameNS(XHTMLNS, 'p')[0].textContent = data.text;
    note.getElementsByTagNameNS(SVGNS, 'text')[0].textContent = data.text;
    note.onclick = function() {
        Operations.insert({
            op: 'RemoveElementOp',
            element: note.id
        });
    };
    note.style.pointerEvents = 'auto';
    copyTransformation(hex, note);
    byId('overlays').appendChild(note);
    return function() {
        note.remove();
    };
};