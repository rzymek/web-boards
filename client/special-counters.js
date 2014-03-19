function roll(count, sides) {
    return {
        src: '/img/dice.svg?x=' + count + 'd' + sides,
        action: function(hex) {
            Operations.insert({
                op: 'RollOp',
                hexid: hex.id,
                server: {
                    roll: {
                        count: count,
                        sides: sides
                    }
                }
            });
        }
    };
}

Special = {
    roll_d6: roll(1, 6),
    roll_2d6: roll(2, 6),
    roll_d10: roll(1, 10),
    note: {
        src: '/img/note.svg',
        action: function(hex) {
            var text = window.prompt("Enter message:");
            if (text === null)
                return;
            Operations.insert({
                op: 'NoteOp',
                text: text,
                hexid: hex.id
            });
        }
    },
    used: {
        src: '/img/used.svg',
        action: function(hex, counter) {
            if(!counter)
                return;
            Operations.insert({
                op: 'PlaceMarkerOp',
                src: '/img/used.svg',
                counterId: counter.id
            });            
        }
    }
};