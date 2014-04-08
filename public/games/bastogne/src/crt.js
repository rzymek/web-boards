var normalizeOdds = function(odds) {
    var min = Math.min(odds[0], odds[1]);
    return min === 0 ? odds : [
        Math.round(odds[0] / min),
        Math.round(odds[1] / min)
    ];
};


//args: roll: number, odds: [a,b]
var getCombatResult = (function() {
    var columns = 
            [[1, 3], [1, 2], [1, 1], [2, 1], [3, 1], [4, 1], [5, 1]];
    var CRT = {
        2:  ["A1r2", "A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  "],
        3:  ["A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1"],
        4:  ["A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1"],
        5:  ["A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2"],
        6:  ["A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2"],
        7:  ["A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3"],
        8:  ["A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3"], //same
        9:  ["A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3", "D1r4"],
        10: ["A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5"],
        11: ["A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5"], //same 
        12: ["A1D1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r4", "D2r6"]
    };
    return function(roll, odds) {
        odds = normalizeOdds(odds);
        if (!(2 <= roll && roll <= 12))
            throw new Error("roll must be 2..12, not " + roll);
        if (odds[0] === 1 && odds[1] > 3)
            odds[1] = 3;
        else if (odds[1] === 1 && odds[0] > 5)
            odds[0] = 5;        
        var col = columns.findIndex(function(o) {
            return o[0] === odds[0] && o[1] === odds[1];
        });
        if (col === -1)
            throw new Error("No CRT column for odds " + odds.join(':'));
        var result = CRT[roll][col];
        if (result === undefined)
            throw new Error("No CRT result for odds " + odds.join(':') + '(col:' + col + ') and roll ' + roll);
        return result.trim();
    };
})();
