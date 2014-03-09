NoteOp = function(data) {
    var svg = document.getElementById('svg');
    var hex = svg.getElementById(data.hexid);
    var note = sprites.note.cloneNode(true);
    note.id = data._id;
    note.getElementsByTagName('p')[0].textContent = data.text;
    note.getElementsByTagName('text')[0].textContent = data.text;
    copyTransformation(hex, note);
    byId('overlays').appendChild(note);
    return function() {
        note.remove();
    };
}