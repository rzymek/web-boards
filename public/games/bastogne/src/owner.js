getOwner = (function() {
    var ownerByCategory = (function() {
        var map = {
            'US': ["101", "327", "401", "501", "502", "506", "705", "Abrams", "CCB", "CCR", "Cherry", "Desobry", "O'Hara", "Misc US"],
            'GE': ["5 FJ", "26 VG", "KG Coch", "KG Gutt", "KG 901", "KG 902", "KG Kunkel", "Lehr", "KG Maucke", "v.Bohm", "v.Fallois", "Misc"]
        };
        var result = {};
        Object.keys(map).forEach(function(owner) {
            map[owner].forEach(function(category) {
                result[category] = owner;
            });
        });
        return result;
    })();
    return function(counter) {
        return ownerByCategory[counter.category];
    };
})();

