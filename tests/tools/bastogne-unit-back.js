var ArtyType = {
    OTHER: 0,
    YELLOW: 1,
    GUNS_88: 2
};
var unitInfo = {
    "5 FJ\u001e1/I/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e2/I/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e3/I/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e4/I/14": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "5 FJ\u001e5/II/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e6/II/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e7/II/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e8/II/14": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "5 FJ\u001e9/III/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e10/III/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e11/III/14": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "5 FJ\u001e12/III/14": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001e1/I/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e2/I/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e3/I/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e4/I/39": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/I/39": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001e5/II/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e6/II/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e7/II/39": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e8/II/39": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/II/39": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001e1/I/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e2/I/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e3/I/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e4/I/77": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/I/77": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001e5/II/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e6/II/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e7/II/77": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e8/II/77": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/II/77": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001e1/I/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e2/I/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e3/I/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e4/I/78": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/I/78": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001e5/II/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e6/II/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e7/II/78": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "26 VG\u001e8/II/78": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "26 VG\u001eS/II/78": {
        "attack": 3,
        "defence": 1,
        "movement": 7
    },
    "26 VG\u001eI/26(-)": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 6
    },
    "26 VG\u001eII/26": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 6
    },
    "26 VG\u001eIII/26": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 6
    },
    "26 VG\u001eIV/26": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 6
    },
    "KG Coch\u001e1/273": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "KG Coch\u001e3/38": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "KG Coch\u001e2": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG Coch\u001e3": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG Coch\u001e4": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG Coch\u001e5/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Coch\u001e6/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Coch\u001e7/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Coch\u001e9": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 10
    },
    "KG Coch\u001e10": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "KG Coch\u001eArty": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 14
    },
    "KG Gutt\u001e1/38": {
        "attack": 6,
        "defence": 3,
        "movement": 12
    },
    "KG Gutt\u001e1/3": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG Gutt\u001e1/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG Gutt\u001e2/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG Gutt\u001e3/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG Gutt\u001e9": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG Gutt\u001e10": {
        "attack": 3,
        "defence": 5,
        "movement": 12
    },
    "KG Gutt\u001eArty": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "KG Gutt\u001eNebel": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 10
    },
    "KG Gutt\u001eWpn": {
        "attack": 3,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 14
    },
    "KG 901\u001e6/130": {
        "attack": 8,
        "defence": 4,
        "movement": 12
    },
    "KG 901\u001e1/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 901\u001e2/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 901\u001e3/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 901\u001e5/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 901\u001e6/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 901\u001e7/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 901\u001e9": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG 901\u001e10": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "KG 901\u001eArty": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "KG 902\u001e1/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 902\u001e2/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 902\u001e3/I": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG 902\u001e5/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 902\u001e6/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 902\u001e7/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG 902\u001e5/130": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG 902\u001e7/130": {
        "attack": 9,
        "defence": 5,
        "movement": 12
    },
    "KG 902\u001e9": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG 902\u001e10": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "KG 902\u001eArty": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "KG Kunkel\u001e1/26": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "KG Kunkel\u001e2/26": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "KG Kunkel\u001e3/26": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "KG Kunkel\u001eWpn/26": {
        "attack": 3,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG Kunkel\u001e1/I/26": {
        "attack": 3,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 12
    },
    "KG Kunkel\u001e2 Aufk": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "KG Kunkel\u001e3/PJ": {
        "attack": 3,
        "defence": 4,
        "movement": 12
    },
    "KG Kunkel\u001eFJ": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "KG Kunkel\u001eHetz": {
        "attack": 6,
        "defence": 3,
        "movement": 12
    },
    "KG Kunkel\u001eNebel": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 10
    },
    "KG Kunkel\u001ePio": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "Lehr\u001e1/130": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "Lehr\u001e2/130": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "Lehr\u001e1/sPJ": {
        "attack": 6,
        "defence": 3,
        "movement": 12
    },
    "Lehr\u001e2/sPJ": {
        "attack": 7,
        "defence": 4,
        "movement": 12
    },
    "Lehr\u001e130": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Lehr\u001e130PJ": {
        "attack": 7,
        "defence": 4,
        "movement": 12
    },
    "KG Maucke\u001e1/115": {
        "attack": 8,
        "defence": 4,
        "movement": 12
    },
    "KG Maucke\u001e2/115": {
        "attack": 6,
        "defence": 3,
        "movement": 12
    },
    "KG Maucke\u001e1/I": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e2/I": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e3/I": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e4/I": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG Maucke\u001e5/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e6/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e7/II": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e8/II": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "KG Maucke\u001e9/III": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e10/III": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e11/III": {
        "attack": 3,
        "defence": 4,
        "movement": 10
    },
    "KG Maucke\u001e12/III": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 8
    },
    "v.Bohm\u001e2 Aufk": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "v.Bohm\u001e3 Aufk": {
        "attack": 2,
        "defence": 3,
        "movement": 8
    },
    "v.Bohm\u001eHQ": {
        "attack": 6,
        "defence": 4,
        "movement": 14
    },
    "v.Bohm\u001ePz": {
        "attack": 8,
        "defence": 4,
        "movement": 12
    },
    "v.Bohm\u001eWpn": {
        "attack": 3,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 14
    },
    "v.Fallois\u001e1 Aufk": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "v.Fallois\u001e3 Aufk": {
        "attack": 6,
        "defence": 6,
        "movement": 14
    },
    "v.Fallois\u001e3/PzPio": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "v.Fallois\u001eArty": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 14
    },
    "v.Fallois\u001eHQ": {
        "attack": 6,
        "defence": 4,
        "movement": 14
    },
    "v.Fallois\u001ePz": {
        "attack": 8,
        "defence": 4,
        "movement": 12
    },
    "Misc\u001e4/766": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "Misc\u001e5/766": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "Misc\u001e6/766": {
        "attack": 3,
        "defence": 6,
        "range": 6,
        "artyType": ArtyType.GUNS_88,
        "movement": 10
    },
    "Misc\u001e766": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "101\u001e327": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "101\u001e377": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "101\u001e463": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "101\u001e907": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "101\u001eA/81": {
        "attack": 1,
        "defence": 2,
        "movement": 6
    },
    "101\u001eB/81": {
        "attack": 1,
        "defence": 2,
        "movement": 6
    },
    "101\u001eC/81": {
        "attack": 1,
        "defence": 2,
        "movement": 6
    },
    "101\u001eA/326": {
        "attack": 1,
        "defence": 1,
        "movement": 6
    },
    "101\u001eB/326": {
        "attack": 1,
        "defence": 1,
        "movement": 6
    },
    "101\u001eC/326": {
        "attack": 1,
        "defence": 1,
        "movement": 6
    },
    "101\u001eD/326": {
        "attack": 1,
        "defence": 1,
        "movement": 6
    },
    "327\u001eA/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "327\u001eB/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "327\u001eC/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "327\u001eE/2": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "327\u001eF/2": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "327\u001eG/2": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "401\u001eA/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "401\u001eB/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "401\u001eC/1": {
        "attack": 2,
        "defence": 5,
        "movement": 6
    },
    "501\u001eA/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eB/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eC/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eD/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eE/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eF/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eG/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eH/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "501\u001eI/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eA/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eB/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eC/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eD/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eE/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eF/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eG/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eH/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "502\u001eI/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eA/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eB/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eC/1": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eD/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eE/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eF/2": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eG/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eH/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "506\u001eI/3": {
        "attack": 2,
        "defence": 4,
        "movement": 6
    },
    "705\u001eA(-)": {
        "attack": 3,
        "defence": 4,
        "movement": 14
    },
    "705\u001eB": {
        "attack": 4,
        "defence": 6,
        "movement": 14
    },
    "705\u001eC": {
        "attack": 4,
        "defence": 6,
        "movement": 14
    },
    "705\u001eHHC": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 16
    },
    "705\u001eRec": {
        "attack": 4,
        "defence": 4,
        "movement": 16
    },
    "Abrams\u001eA/37": {
        "attack": 6,
        "defence": 5,
        "movement": 12
    },
    "Abrams\u001eB/37": {
        "attack": 6,
        "defence": 5,
        "movement": 12
    },
    "Abrams\u001eC/37": {
        "attack": 6,
        "defence": 5,
        "movement": 12
    },
    "Abrams\u001eD/37": {
        "attack": 4,
        "defence": 3,
        "movement": 14
    },
    "Abrams\u001eHHC/37": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 16
    },
    "Abrams\u001eC/53": {
        "attack": 4,
        "defence": 5,
        "movement": 14
    },
    "CCB\u001eA/20": {
        "attack": 4,
        "defence": 5,
        "movement": 14
    },
    "CCB\u001eB/769": {
        "attack": 2,
        "defence": 3,
        "movement": 12
    },
    "CCB\u001eC/55": {
        "attack": 3,
        "defence": 5,
        "movement": 14
    },
    "CCB\u001eHHC/20": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 16
    },
    "CCR\u001eC/482": {
        "attack": 2,
        "defence": 3,
        "movement": 12
    },
    "CCR\u001eHHC": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 16
    },
    "Cherry\u001eA/3": {
        "attack": 5,
        "defence": 3,
        "movement": 12
    },
    "Cherry\u001eD/3": {
        "attack": 3,
        "defence": 2,
        "movement": 14
    },
    "Cherry\u001eHHC/3": {
        "attack": 2,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.OTHER,
        "movement": 16
    },
    "Cherry\u001eC/20": {
        "attack": 4,
        "defence": 5,
        "movement": 14
    },
    "Cherry\u001eC/609": {
        "attack": 2,
        "defence": 3,
        "movement": 14
    },
    "Cherry\u001eD/90": {
        "attack": 3,
        "defence": 3,
        "movement": 16
    },
    "Desobry\u001eB/3": {
        "attack": 5,
        "defence": 3,
        "movement": 12
    },
    "Desobry\u001eD/3": {
        "attack": 3,
        "defence": 2,
        "movement": 14
    },
    "Desobry\u001eB/20": {
        "attack": 4,
        "defence": 5,
        "movement": 14
    },
    "Desobry\u001eC/609": {
        "attack": 2,
        "defence": 3,
        "movement": 14
    },
    "Desobry\u001eD/90": {
        "attack": 3,
        "defence": 3,
        "movement": 16
    },
    "O'Hara\u001eB/54": {
        "attack": 4,
        "defence": 5,
        "movement": 14
    },
    "O'Hara\u001eC/16": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    },
    "O'Hara\u001eC/21": {
        "attack": 5,
        "defence": 3,
        "movement": 12
    },
    "O'Hara\u001eD/3": {
        "attack": 3,
        "defence": 2,
        "movement": 14
    },
    "O'Hara\u001eD/90": {
        "attack": 3,
        "defence": 3,
        "movement": 16
    },
    "Misc US\u001e58": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001e73": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001e420": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001e687(-)": {
        "attack": 3,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001e755": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001e969": {
        "attack": 4,
        "defence": 1,
        "range": 1,
        "artyType": ArtyType.YELLOW,
        "movement": 12
    },
    "Misc US\u001eA": {
        "attack": 0,
        "defence": 1,
        "movement": 6
    },
    "Misc US\u001eB": {
        "attack": 0,
        "defence": 1,
        "movement": 6
    },
    "Misc US\u001eC": {
        "attack": 0,
        "defence": 1,
        "movement": 6
    },
    "Misc US\u001eD": {
        "attack": 0,
        "defence": 1,
        "movement": 6
    },
    "Misc US\u001eAdHoc SNAFU": {
        "attack": 1,
        "defence": 3,
        "movement": 6
    }
};


/*
 *
 watch-run bastogne-unit-back.js -- NODE_PATH=~/opt/node/lib/node_modules/ node bastogne-unit-back.js
 */
var fs = require('fs');
var glob = require('glob');
var base = __dirname + '/../../public/games/bastogne/';
var file = base + 'game.json';

fs.readFile(file, 'utf8', function(err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }

    var rejected = [];
    data = JSON.parse(data).pieces.map(function(it) {
        return it.list.filter(function(c) {
            return c.images.length === 2;
        }).map(function(c) {
            return {
                name: it.category + '\x1e' + c.name,
                front: c.images[0],
                back: c.images[1]
            };
        });
    }).reduce(function(a, b) {
        return a.concat(b);
    }).map(function(it) {
        var pattern = __dirname + '/back-info/**/*{,/}' + it.back;
        it.pattern = pattern;
        it.back_info_files = glob.sync(pattern);
        if (it.back_info_files.length > 1) {
            throw it.back_info_files;
        }
        return it;
    }).filter(function(it) {
        return it.back_info_files.length !== 0;
    }).map(function(it) {
        return {
            name: it.name,
            back_info_file: it.back_info_files[0]
        };
    }).map(function(it) {
        return {
            name: it.name,
            info: it.back_info_file.replace(/.*[/]([0-9]*,[0-9]*,[0-9]*).*/, '$1')
        };
    }).filter(function(it) {
        var matches = it.info.match(/[0-9]*,[0-9]*,[0-9]*/);
        if (!matches)
            rejected.push(it);
        return matches;
    }).map(function(it) {
        return {
            name: it.name,
            info: it.info.substr(it.info.lastIndexOf('/') + 1)
                    .split(/,/)
                    .map(function(it){
                        return parseInt(it);
                    })
        };
    }).map(function(it) {
        return {
            name: it.name,
            back: {
                attack: it.info[0],
                defence: it.info[1],
                movement: it.info[2]
            }
        };
    });

    var res = {};
    data.forEach(function(it) {
        var e={};
        /*
        var front = unitInfo[it.name];

        if(front.attack !== it.back.attack) {
            e.attack = it.back.attack;
        }
        if(front.defence !== it.back.defence) {
            e.defence = it.back.defence;
        }
        if(front.movement !== it.back.movement) {
            e.movement = it.back.movement;
        }
        if(Object.keys(e).length > 1) {
            res[it.name] = e;
        }
        */
       res[it.name] = it.back;
    });
    data = res;//*/

    var json = JSON.stringify(data, null, 2);
    fs.writeFile(__dirname + '/units-back-info.json', json);
    console.log(rejected);
});
