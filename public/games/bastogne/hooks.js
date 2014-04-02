CRT = [
    ["     ", "1:3 ", "1:2 ", "1:1 ", "2:1 ", "3:1 ", "4:1 ", "5:1 "],
    ["2    ", "A1r2", "A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  "],
    ["3    ", "A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1"],
    ["4    ", "A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1"],
    ["5    ", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2"],
    ["6    ", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2"],
    ["7    ", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3"],
    ["8    ", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3"], //same
    ["9    ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3", "D1r4"],
    ["10   ", "A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5"],
    ["11   ", "A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5"], //same 
    ["12   ", "A1D1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r4", "D2r6"]
];

var catOwner = {
    'US': ["101", "327", "401", "501", "502", "506", "705", "Abrams", "CCB", "CCR", "Cherry", "Desobry", "O'Hara"],
    'GE': ["5 FJ", "26 VG", "KG Coch", "KG Gutt", "KG 901", "KG 902", "KG Kunkel", "Lehr", "KG Maucke", "v.Bohm", "v.Fallois"]
};

gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        hexClicked: hexClicked
    };
    console.log('original', original);
    hexClicked = function(hex) {
        console.log(hex.stack);
        original.hexClicked(hex);
    };

    MoveOp = function(data) {
        var counter = byId(data.counter);
        if (counter.name.match(/ DG$/)) {
            return original.MoveOp(data);
        }
        var from = counter.position;
        var to = byId(data.to);
        var DGfrom = from.stack ? from.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];
        var DGto = to.stack ? to.stack.filter(
                function(c) {
                    return c.name.match(/ DG$/);
                }) : [];

        var undo = [];
        undo.push(original.MoveOp(data));
        if (DGfrom.length > 0) {
            undo.push(PlaceOp({
                _id: (new Meteor.Collection.ObjectID()).toHexString(),
                name: DGfrom[0].name,
                category: DGfrom[0].category,
                hexid: data.to
            }));
        }
        if (from.stack && from.stack.length === DGfrom.length) {
            DGfrom.forEach(function(it) {
                undo.push(RemoveElementOp({element: it.id}));
            });
        }
        return function() {
            undo.reverse().forEach(function(fn) {
                fn();
            });
        };
    };

    return function() {
        MoveOp = original.MoveOp;
        hexClicked = original.hexClicked;
        delete CRT;
    };
};