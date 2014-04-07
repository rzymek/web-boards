(function(){

Attack = function(data) {
    if(data.server)
        return;
    var hex = byId(data.target);
    var backup = {
        odds: hex.odds,
        arrows: hex.attackArrows
    };
    var undo = RemoveElementOp({
        elements: hex.attackArrows.concat(hex.odds.id)
    });
    delete hex.odds;
    delete hex.attackArrows;

    //TODO: roll the dice, display results
    return function() {
        undo();
        hex.odds = backup.odds;
        hex.attackArrows = backup.arrows;
    };
};

var CRT = [
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
 

getHexInfo = (function() {
    var OPEN = {
        movement: 1,
        defenceMod: 1,
        barrage: 0
    };
    var FOREST = {
        movement: 2,
        defenceMod: 2,
        barrage: -1
    };
    var FOREST_RUNE = {
        movement: 3,
        defenceMod: 2,
        barrage: -1
    };
    var OPEN_RUNE = {
        movement: 2,
        defenceMod: 1,
        barrage: 0
    };
    var CITY = {
        movement: 1,
        defenceMod: 2,
        barrage: +1
    };

    var hexInfo = {h0_34: FOREST_RUNE, h2_34: FOREST_RUNE, h4_34: FOREST, h6_34: FOREST_RUNE, h8_34: FOREST, h10_34: FOREST, h14_34: OPEN_RUNE, h16_34: OPEN_RUNE, h18_34: FOREST, h22_34: FOREST, h24_34: FOREST_RUNE, h26_34: FOREST, h28_34: FOREST, h30_34: FOREST, h32_34: FOREST, h34_34: FOREST, h36_34: FOREST_RUNE, h40_34: FOREST_RUNE, h44_34: FOREST, h46_34: FOREST, h48_34: FOREST_RUNE, h1_35: FOREST, h3_35: FOREST, h7_35: FOREST_RUNE, h11_35: FOREST, h17_35: FOREST, h19_35: FOREST, h21_35: FOREST, h23_35: FOREST, h25_35: FOREST, h27_35: FOREST, h29_35: FOREST, h31_35: FOREST_RUNE, h33_35: OPEN_RUNE, h35_35: FOREST, h37_35: FOREST, h41_35: FOREST_RUNE, h45_35: FOREST, h47_35: FOREST_RUNE, h0_33: FOREST, h2_33: OPEN_RUNE, h4_33: FOREST, h6_33: FOREST, h8_33: FOREST, h10_33: OPEN_RUNE, h12_33: FOREST_RUNE, h16_33: FOREST_RUNE, h18_33: FOREST_RUNE, h20_33: FOREST, h24_33: OPEN_RUNE, h26_33: FOREST, h30_33: FOREST_RUNE, h32_33: FOREST, h36_33: CITY, h40_33: FOREST_RUNE, h42_33: FOREST, h44_33: FOREST, h46_33: FOREST_RUNE, h48_33: FOREST, h1_34: FOREST_RUNE, h3_34: FOREST, h5_34: FOREST, h7_34: FOREST, h9_34: FOREST, h11_34: OPEN_RUNE, h13_34: OPEN_RUNE, h15_34: OPEN_RUNE, h17_34: OPEN_RUNE, h19_34: FOREST, h21_34: FOREST, h25_34: FOREST, h27_34: FOREST, h29_34: FOREST, h31_34: FOREST_RUNE, h33_34: FOREST_RUNE, h37_34: FOREST, h41_34: FOREST, h43_34: FOREST, h45_34: FOREST, h47_34: FOREST_RUNE, h2_32: OPEN_RUNE, h4_32: FOREST, h6_32: FOREST, h8_32: FOREST, h10_32: FOREST_RUNE, h12_32: FOREST, h14_32: FOREST_RUNE, h16_32: FOREST_RUNE, h18_32: FOREST, h20_32: OPEN_RUNE, h22_32: OPEN_RUNE, h24_32: OPEN_RUNE, h26_32: FOREST, h28_32: FOREST_RUNE, h30_32: FOREST, h32_32: FOREST, h34_32: FOREST, h36_32: FOREST_RUNE, h40_32: OPEN_RUNE, h42_32: OPEN_RUNE, h44_32: FOREST_RUNE, h46_32: FOREST, h48_32: FOREST, h1_33: FOREST, h3_33: FOREST, h5_33: FOREST, h7_33: FOREST, h9_33: FOREST_RUNE, h11_33: FOREST_RUNE, h13_33: FOREST, h15_33: FOREST_RUNE, h17_33: FOREST, h19_33: FOREST, h21_33: OPEN_RUNE, h23_33: CITY, h25_33: FOREST, h27_33: FOREST, h29_33: FOREST_RUNE, h31_33: FOREST, h33_33: FOREST_RUNE, h37_33: FOREST, h39_33: OPEN_RUNE, h41_33: FOREST_RUNE, h43_33: FOREST, h45_33: FOREST_RUNE, h47_33: FOREST, h0_31: FOREST, h4_31: FOREST_RUNE, h6_31: FOREST_RUNE, h8_31: FOREST_RUNE, h10_31: FOREST, h12_31: FOREST, h14_31: FOREST, h16_31: FOREST_RUNE, h20_31: CITY, h24_31: OPEN_RUNE, h26_31: FOREST, h28_31: FOREST, h30_31: FOREST, h32_31: FOREST_RUNE, h34_31: FOREST, h36_31: FOREST_RUNE, h38_31: OPEN_RUNE, h40_31: OPEN_RUNE, h42_31: CITY, h46_31: FOREST, h48_31: FOREST, h3_32: OPEN_RUNE, h7_32: FOREST_RUNE, h9_32: FOREST_RUNE, h11_32: FOREST, h13_32: FOREST_RUNE, h15_32: FOREST, h17_32: FOREST, h19_32: OPEN_RUNE, h25_32: OPEN_RUNE, h27_32: FOREST_RUNE, h29_32: FOREST_RUNE, h31_32: FOREST, h33_32: FOREST_RUNE, h35_32: FOREST_RUNE, h37_32: FOREST, h39_32: OPEN_RUNE, h45_32: FOREST, h47_32: FOREST, h0_30: FOREST, h2_30: FOREST, h4_30: OPEN_RUNE, h6_30: OPEN_RUNE, h8_30: FOREST, h12_30: FOREST, h14_30: FOREST_RUNE, h16_30: FOREST, h18_30: FOREST, h20_30: FOREST, h24_30: OPEN_RUNE, h26_30: FOREST, h28_30: FOREST_RUNE, h30_30: FOREST, h32_30: FOREST_RUNE, h34_30: FOREST, h36_30: FOREST_RUNE, h38_30: FOREST, h40_30: OPEN_RUNE, h46_30: FOREST_RUNE, h48_30: FOREST_RUNE, h1_31: FOREST, h7_31: FOREST_RUNE, h9_31: FOREST, h11_31: FOREST, h13_31: FOREST_RUNE, h15_31: FOREST_RUNE, h17_31: FOREST, h21_31: FOREST, h23_31: CITY, h25_31: OPEN_RUNE, h27_31: FOREST, h29_31: FOREST_RUNE, h31_31: FOREST, h33_31: FOREST, h35_31: FOREST, h37_31: FOREST, h45_31: FOREST, h47_31: FOREST_RUNE, h0_29: FOREST, h2_29: FOREST, h4_29: OPEN_RUNE, h6_29: OPEN_RUNE, h8_29: FOREST, h12_29: FOREST, h14_29: FOREST, h20_29: OPEN_RUNE, h22_29: FOREST_RUNE, h26_29: FOREST, h28_29: FOREST, h30_29: CITY, h32_29: FOREST, h34_29: FOREST_RUNE, h36_29: FOREST_RUNE, h38_29: FOREST, h42_29: FOREST, h44_29: FOREST, h46_29: FOREST, h48_29: FOREST_RUNE, h1_30: FOREST, h5_30: OPEN_RUNE, h7_30: FOREST, h11_30: FOREST, h13_30: FOREST, h15_30: FOREST, h17_30: FOREST, h23_30: OPEN_RUNE, h25_30: OPEN_RUNE, h27_30: FOREST, h29_30: FOREST, h31_30: FOREST_RUNE, h33_30: FOREST, h35_30: FOREST_RUNE, h37_30: FOREST, h45_30: FOREST, h47_30: FOREST, h0_28: FOREST_RUNE, h2_28: OPEN_RUNE, h4_28: OPEN_RUNE, h6_28: OPEN_RUNE, h10_28: OPEN_RUNE, h12_28: FOREST_RUNE, h22_28: OPEN_RUNE, h24_28: OPEN_RUNE, h32_28: FOREST, h34_28: FOREST_RUNE, h36_28: FOREST_RUNE, h38_28: FOREST, h40_28: FOREST, h42_28: FOREST_RUNE, h44_28: FOREST_RUNE, h48_28: FOREST_RUNE, h1_29: FOREST, h5_29: CITY, h7_29: FOREST, h9_29: FOREST, h15_29: FOREST, h21_29: OPEN_RUNE, h23_29: FOREST_RUNE, h25_29: FOREST_RUNE, h29_29: FOREST, h33_29: FOREST, h35_29: FOREST, h37_29: FOREST_RUNE, h39_29: FOREST, h41_29: FOREST_RUNE, h47_29: FOREST, h0_27: FOREST, h10_27: OPEN_RUNE, h18_27: FOREST, h20_27: OPEN_RUNE, h24_27: FOREST, h26_27: FOREST, h28_27: CITY, h32_27: FOREST, h34_27: FOREST, h36_27: FOREST, h38_27: FOREST, h40_27: FOREST_RUNE, h42_27: FOREST, h44_27: FOREST_RUNE, h46_27: FOREST_RUNE, h48_27: FOREST, h1_28: OPEN_RUNE, h3_28: OPEN_RUNE, h9_28: CITY, h11_28: OPEN_RUNE, h19_28: OPEN_RUNE, h21_28: CITY, h23_28: FOREST, h25_28: FOREST_RUNE, h33_28: FOREST, h35_28: FOREST, h37_28: FOREST, h39_28: FOREST, h41_28: FOREST_RUNE, h43_28: FOREST_RUNE, h45_28: FOREST_RUNE, h47_28: FOREST_RUNE, h0_26: FOREST_RUNE, h4_26: OPEN_RUNE, h10_26: FOREST_RUNE, h12_26: OPEN_RUNE, h14_26: OPEN_RUNE, h16_26: OPEN_RUNE, h20_26: FOREST, h22_26: FOREST, h24_26: FOREST_RUNE, h26_26: FOREST, h28_26: OPEN_RUNE, h34_26: FOREST, h36_26: FOREST, h38_26: FOREST, h40_26: FOREST_RUNE, h42_26: FOREST, h44_26: FOREST, h46_26: FOREST_RUNE, h48_26: FOREST, h1_27: FOREST, h3_27: OPEN_RUNE, h7_27: FOREST, h11_27: OPEN_RUNE, h13_27: CITY, h15_27: OPEN_RUNE, h17_27: OPEN_RUNE, h19_27: FOREST, h21_27: FOREST, h23_27: FOREST, h25_27: FOREST_RUNE, h33_27: FOREST, h35_27: FOREST, h37_27: FOREST, h39_27: FOREST_RUNE, h41_27: FOREST_RUNE, h43_27: FOREST_RUNE, h45_27: FOREST, h47_27: FOREST, h0_25: FOREST, h8_25: OPEN_RUNE, h12_25: FOREST, h16_25: OPEN_RUNE, h20_25: FOREST, h22_25: FOREST, h24_25: FOREST, h26_25: FOREST, h30_25: FOREST_RUNE, h32_25: OPEN_RUNE, h34_25: FOREST, h36_25: FOREST_RUNE, h38_25: FOREST, h40_25: FOREST, h42_25: FOREST_RUNE, h44_25: FOREST_RUNE, h46_25: FOREST_RUNE, h48_25: FOREST, h3_26: CITY, h9_26: OPEN_RUNE, h13_26: OPEN_RUNE, h15_26: OPEN_RUNE, h19_26: FOREST, h21_26: FOREST, h23_26: FOREST, h25_26: FOREST, h29_26: OPEN_RUNE, h31_26: FOREST_RUNE, h33_26: FOREST, h35_26: FOREST, h37_26: FOREST, h39_26: FOREST, h41_26: FOREST, h43_26: FOREST_RUNE, h45_26: FOREST_RUNE, h47_26: FOREST, h0_24: FOREST, h2_24: FOREST, h8_24: OPEN_RUNE, h12_24: FOREST, h20_24: FOREST, h22_24: FOREST, h26_24: FOREST_RUNE, h34_24: OPEN_RUNE, h36_24: FOREST_RUNE, h38_24: FOREST_RUNE, h40_24: FOREST, h42_24: FOREST, h44_24: FOREST_RUNE, h46_24: FOREST_RUNE, h48_24: FOREST, h1_25: FOREST, h13_25: FOREST, h15_25: OPEN_RUNE, h17_25: CITY, h21_25: FOREST, h23_25: FOREST, h25_25: FOREST_RUNE, h27_25: FOREST, h31_25: FOREST, h35_25: FOREST, h37_25: FOREST, h39_25: FOREST, h41_25: FOREST_RUNE, h43_25: FOREST, h45_25: FOREST, h47_25: FOREST, h0_23: FOREST, h2_23: FOREST_RUNE, h4_23: FOREST, h6_23: OPEN_RUNE, h10_23: OPEN_RUNE, h12_23: FOREST, h14_23: FOREST_RUNE, h16_23: FOREST, h18_23: FOREST, h20_23: FOREST, h22_23: FOREST, h24_23: OPEN_RUNE, h26_23: OPEN_RUNE, h30_23: FOREST_RUNE, h32_23: OPEN_RUNE, h36_23: FOREST, h38_23: FOREST_RUNE, h42_23: FOREST, h44_23: FOREST, h46_23: CITY, h48_23: FOREST, h1_24: FOREST, h3_24: FOREST, h7_24: OPEN_RUNE, h9_24: OPEN_RUNE, h13_24: FOREST, h15_24: FOREST, h17_24: OPEN_RUNE, h21_24: FOREST, h23_24: FOREST, h27_24: OPEN_RUNE, h31_24: OPEN_RUNE, h33_24: OPEN_RUNE, h35_24: OPEN_RUNE, h37_24: FOREST_RUNE, h41_24: FOREST, h43_24: FOREST, h45_24: FOREST_RUNE, h0_22: FOREST_RUNE, h2_22: FOREST, h4_22: OPEN_RUNE, h10_22: OPEN_RUNE, h12_22: FOREST, h14_22: FOREST, h16_22: FOREST, h18_22: FOREST, h20_22: FOREST_RUNE, h22_22: FOREST_RUNE, h24_22: OPEN_RUNE, h36_22: FOREST, h38_22: FOREST_RUNE, h42_22: FOREST, h44_22: OPEN_RUNE, h46_22: FOREST, h48_22: FOREST, h1_23: FOREST_RUNE, h3_23: OPEN_RUNE, h5_23: FOREST_RUNE, h7_23: OPEN_RUNE, h13_23: FOREST_RUNE, h15_23: FOREST, h17_23: FOREST_RUNE, h19_23: FOREST_RUNE, h21_23: FOREST_RUNE, h27_23: OPEN_RUNE, h29_23: OPEN_RUNE, h31_23: OPEN_RUNE, h33_23: CITY, h35_23: FOREST, h37_23: FOREST, h39_23: FOREST, h43_23: FOREST, h8_21: FOREST, h12_21: FOREST_RUNE, h14_21: FOREST_RUNE, h16_21: FOREST, h18_21: FOREST, h20_21: FOREST, h22_21: OPEN_RUNE, h24_21: OPEN_RUNE, h28_21: OPEN_RUNE, h30_21: OPEN_RUNE, h34_21: FOREST, h36_21: FOREST, h40_21: CITY, h42_21: FOREST, h44_21: FOREST_RUNE, h46_21: FOREST, h48_21: FOREST, h3_22: FOREST, h11_22: CITY, h13_22: FOREST_RUNE, h15_22: FOREST_RUNE, h17_22: FOREST, h19_22: FOREST, h21_22: FOREST_RUNE, h23_22: FOREST_RUNE, h27_22: OPEN_RUNE, h35_22: FOREST, h37_22: FOREST, h39_22: OPEN_RUNE, h43_22: FOREST, h45_22: OPEN_RUNE, h47_22: FOREST, h4_20: OPEN_RUNE, h6_20: OPEN_RUNE, h8_20: FOREST, h12_20: FOREST, h14_20: FOREST, h16_20: FOREST, h20_20: FOREST, h22_20: FOREST, h26_20: OPEN_RUNE, h28_20: OPEN_RUNE, h32_20: OPEN_RUNE, h34_20: OPEN_RUNE, h36_20: FOREST_RUNE, h38_20: FOREST, h42_20: FOREST_RUNE, h44_20: OPEN_RUNE, h46_20: FOREST, h48_20: FOREST, h3_21: FOREST_RUNE, h5_21: CITY, h9_21: FOREST, h13_21: FOREST_RUNE, h15_21: FOREST, h21_21: FOREST, h25_21: OPEN_RUNE, h27_21: OPEN_RUNE, h31_21: OPEN_RUNE, h35_21: FOREST, h37_21: FOREST_RUNE, h41_21: CITY, h43_21: FOREST, h45_21: OPEN_RUNE, h47_21: FOREST, h4_19: FOREST, h8_19: FOREST, h10_19: FOREST, h12_19: FOREST, h14_19: FOREST, h20_19: FOREST, h24_19: OPEN_RUNE, h26_19: CITY, h28_19: CITY, h32_19: CITY, h36_19: FOREST_RUNE, h38_19: FOREST, h44_19: OPEN_RUNE, h46_19: FOREST, h48_19: FOREST, h3_20: FOREST, h5_20: OPEN_RUNE, h9_20: FOREST, h13_20: FOREST, h15_20: FOREST, h17_20: CITY, h27_20: CITY, h31_20: OPEN_RUNE, h35_20: OPEN_RUNE, h37_20: FOREST, h43_20: FOREST_RUNE, h45_20: FOREST_RUNE, h47_20: FOREST, h0_18: FOREST, h8_18: FOREST, h10_18: FOREST_RUNE, h12_18: FOREST_RUNE, h14_18: FOREST_RUNE, h16_18: FOREST, h20_18: FOREST, h26_18: OPEN_RUNE, h28_18: CITY, h36_18: CITY, h38_18: FOREST, h40_18: FOREST, h42_18: FOREST, h44_18: CITY, h9_19: FOREST, h11_19: FOREST, h13_19: FOREST, h17_19: OPEN_RUNE, h19_19: FOREST, h25_19: OPEN_RUNE, h27_19: CITY, h29_19: CITY, h33_19: OPEN_RUNE, h37_19: FOREST_RUNE, h41_19: FOREST, h43_19: FOREST_RUNE, h45_19: FOREST, h4_17: OPEN_RUNE, h8_17: OPEN_RUNE, h10_17: FOREST, h12_17: FOREST_RUNE, h14_17: OPEN_RUNE, h16_17: FOREST, h20_17: FOREST, h24_17: OPEN_RUNE, h26_17: OPEN_RUNE, h36_17: OPEN_RUNE, h42_17: FOREST_RUNE, h9_18: FOREST_RUNE, h11_18: FOREST, h15_18: FOREST, h17_18: FOREST_RUNE, h23_18: OPEN_RUNE, h27_18: OPEN_RUNE, h39_18: FOREST_RUNE, h43_18: OPEN_RUNE, h0_16: FOREST, h2_16: FOREST, h6_16: OPEN_RUNE, h10_16: FOREST, h14_16: CITY, h16_16: OPEN_RUNE, h20_16: FOREST, h22_16: FOREST_RUNE, h26_16: OPEN_RUNE, h36_16: OPEN_RUNE, h40_16: FOREST, h42_16: FOREST_RUNE, h50_16: CITY, h1_17: FOREST, h3_17: OPEN_RUNE, h5_17: OPEN_RUNE, h7_17: OPEN_RUNE, h9_17: FOREST_RUNE, h13_17: OPEN_RUNE, h17_17: OPEN_RUNE, h25_17: OPEN_RUNE, h39_17: CITY, h0_15: FOREST, h2_15: FOREST_RUNE, h12_15: OPEN_RUNE, h16_15: OPEN_RUNE, h24_15: OPEN_RUNE, h28_15: FOREST, h36_15: CITY, h40_15: OPEN_RUNE, h42_15: FOREST, h46_15: FOREST, h52_15: CITY, h1_16: FOREST, h5_16: CITY, h13_16: OPEN_RUNE, h15_16: OPEN_RUNE, h19_16: OPEN_RUNE, h23_16: OPEN_RUNE, h25_16: OPEN_RUNE, h29_16: CITY, h39_16: CITY, h41_16: FOREST, h43_16: FOREST_RUNE, h45_16: FOREST, h0_14: FOREST, h2_14: FOREST, h12_14: OPEN_RUNE, h22_14: CITY, h28_14: FOREST, h32_14: FOREST, h34_14: OPEN_RUNE, h38_14: FOREST, h40_14: OPEN_RUNE, h42_14: FOREST_RUNE, h46_14: FOREST_RUNE, h50_14: FOREST, h1_15: FOREST, h3_15: OPEN_RUNE, h13_15: FOREST, h17_15: FOREST, h19_15: OPEN_RUNE, h25_15: CITY, h27_15: FOREST, h35_15: OPEN_RUNE, h37_15: OPEN_RUNE, h41_15: FOREST, h43_15: FOREST_RUNE, h45_15: FOREST, h49_15: FOREST, h2_13: FOREST, h12_13: FOREST_RUNE, h18_13: FOREST_RUNE, h22_13: FOREST, h24_13: FOREST, h28_13: FOREST, h32_13: FOREST, h34_13: FOREST, h36_13: FOREST, h38_13: FOREST, h42_13: FOREST_RUNE, h46_13: FOREST_RUNE, h48_13: CITY, h1_14: FOREST, h3_14: FOREST, h13_14: FOREST, h19_14: OPEN_RUNE, h27_14: FOREST, h31_14: FOREST, h33_14: FOREST, h39_14: FOREST, h41_14: OPEN_RUNE, h45_14: FOREST, h47_14: FOREST, h49_14: FOREST, h0_12: FOREST_RUNE, h4_12: FOREST, h10_12: OPEN_RUNE, h12_12: CITY, h14_12: FOREST, h16_12: FOREST_RUNE, h18_12: OPEN_RUNE, h22_12: FOREST, h24_12: FOREST, h26_12: FOREST_RUNE, h28_12: FOREST, h30_12: FOREST_RUNE, h32_12: FOREST, h34_12: FOREST, h36_12: FOREST, h38_12: FOREST, h42_12: CITY, h46_12: FOREST_RUNE, h48_12: FOREST, h50_12: FOREST, h52_12: FOREST_RUNE, h54_12: FOREST, h7_13: CITY, h13_13: OPEN_RUNE, h15_13: FOREST, h19_13: FOREST_RUNE, h21_13: FOREST, h23_13: FOREST, h25_13: FOREST_RUNE, h27_13: FOREST_RUNE, h29_13: FOREST, h31_13: FOREST, h33_13: FOREST, h35_13: FOREST, h37_13: FOREST, h39_13: FOREST, h45_13: FOREST_RUNE, h47_13: FOREST, h49_13: CITY, h51_13: FOREST, h53_13: FOREST_RUNE, h0_11: FOREST_RUNE, h2_11: OPEN_RUNE, h6_11: OPEN_RUNE, h12_11: OPEN_RUNE, h14_11: FOREST, h16_11: FOREST_RUNE, h20_11: FOREST_RUNE, h22_11: FOREST, h24_11: FOREST, h26_11: FOREST, h28_11: FOREST_RUNE, h30_11: FOREST_RUNE, h32_11: FOREST, h34_11: FOREST, h36_11: FOREST, h38_11: FOREST, h40_11: OPEN_RUNE, h42_11: OPEN_RUNE, h44_11: FOREST, h46_11: FOREST_RUNE, h48_11: FOREST, h50_11: FOREST, h52_11: FOREST_RUNE, h1_12: OPEN_RUNE, h3_12: FOREST, h9_12: OPEN_RUNE, h11_12: OPEN_RUNE, h13_12: OPEN_RUNE, h15_12: FOREST, h17_12: FOREST, h19_12: FOREST_RUNE, h21_12: FOREST, h23_12: FOREST, h25_12: FOREST, h27_12: FOREST_RUNE, h29_12: FOREST, h31_12: FOREST, h33_12: FOREST, h35_12: FOREST, h37_12: FOREST, h43_12: FOREST, h47_12: FOREST, h49_12: FOREST, h51_12: FOREST, h53_12: FOREST, h10_10: FOREST, h12_10: FOREST, h16_10: FOREST_RUNE, h20_10: FOREST_RUNE, h22_10: FOREST, h26_10: FOREST, h28_10: FOREST, h30_10: FOREST, h36_10: FOREST, h38_10: FOREST_RUNE, h44_10: FOREST_RUNE, h46_10: FOREST_RUNE, h48_10: FOREST, h50_10: FOREST_RUNE, h52_10: FOREST_RUNE, h54_10: CITY, h1_11: OPEN_RUNE, h3_11: OPEN_RUNE, h7_11: OPEN_RUNE, h9_11: FOREST, h13_11: OPEN_RUNE, h15_11: FOREST, h17_11: FOREST, h19_11: CITY, h21_11: FOREST, h25_11: FOREST, h27_11: FOREST, h29_11: FOREST_RUNE, h31_11: FOREST, h33_11: FOREST, h35_11: FOREST_RUNE, h37_11: FOREST, h39_11: OPEN_RUNE, h41_11: OPEN_RUNE, h43_11: FOREST, h45_11: FOREST_RUNE, h47_11: FOREST, h51_11: FOREST, h2_9: FOREST, h8_9: FOREST, h10_9: FOREST, h12_9: FOREST, h16_9: FOREST, h20_9: FOREST, h26_9: FOREST, h28_9: FOREST, h30_9: FOREST, h36_9: FOREST, h38_9: FOREST, h40_9: FOREST, h42_9: CITY, h46_9: FOREST_RUNE, h52_9: FOREST, h7_10: FOREST_RUNE, h9_10: FOREST, h11_10: FOREST, h13_10: FOREST_RUNE, h15_10: FOREST_RUNE, h21_10: FOREST, h25_10: FOREST, h27_10: FOREST, h29_10: FOREST, h35_10: FOREST_RUNE, h37_10: FOREST, h43_10: FOREST_RUNE, h45_10: FOREST, h47_10: FOREST, h51_10: FOREST_RUNE, h0_8: FOREST, h2_8: FOREST, h4_8: OPEN_RUNE, h8_8: FOREST_RUNE, h10_8: FOREST_RUNE, h16_8: FOREST, h20_8: FOREST, h22_8: OPEN_RUNE, h24_8: OPEN_RUNE, h26_8: FOREST_RUNE, h28_8: FOREST, h32_8: OPEN_RUNE, h34_8: OPEN_RUNE, h36_8: FOREST, h38_8: FOREST, h42_8: FOREST, h44_8: FOREST, h46_8: FOREST_RUNE, h48_8: FOREST, h50_8: OPEN_RUNE, h1_9: FOREST, h7_9: FOREST_RUNE, h9_9: FOREST, h11_9: CITY, h13_9: FOREST_RUNE, h15_9: FOREST_RUNE, h19_9: FOREST, h21_9: OPEN_RUNE, h23_9: OPEN_RUNE, h25_9: OPEN_RUNE, h27_9: FOREST_RUNE, h29_9: FOREST, h33_9: CITY, h35_9: FOREST_RUNE, h37_9: FOREST, h45_9: FOREST, h47_9: FOREST, h49_9: CITY, h51_9: FOREST_RUNE, h53_9: FOREST, h0_7: FOREST, h2_7: FOREST, h4_7: FOREST, h6_7: CITY, h8_7: FOREST_RUNE, h10_7: FOREST, h14_7: CITY, h16_7: FOREST_RUNE, h20_7: FOREST, h24_7: OPEN_RUNE, h28_7: FOREST, h30_7: CITY, h36_7: FOREST, h38_7: FOREST_RUNE, h44_7: FOREST, h46_7: FOREST, h48_7: FOREST, h50_7: OPEN_RUNE, h52_7: FOREST, h54_7: FOREST, h1_8: FOREST, h3_8: FOREST, h5_8: OPEN_RUNE, h7_8: OPEN_RUNE, h9_8: FOREST_RUNE, h11_8: FOREST, h13_8: FOREST_RUNE, h15_8: OPEN_RUNE, h17_8: OPEN_RUNE, h19_8: FOREST_RUNE, h23_8: OPEN_RUNE, h27_8: OPEN_RUNE, h29_8: FOREST, h33_8: OPEN_RUNE, h35_8: FOREST_RUNE, h37_8: FOREST, h39_8: FOREST, h41_8: FOREST, h45_8: FOREST, h47_8: FOREST, h49_8: FOREST, h0_6: FOREST, h2_6: FOREST, h4_6: FOREST, h6_6: FOREST, h8_6: FOREST_RUNE, h10_6: FOREST, h12_6: OPEN_RUNE, h14_6: OPEN_RUNE, h16_6: FOREST, h18_6: FOREST, h20_6: OPEN_RUNE, h22_6: OPEN_RUNE, h26_6: FOREST, h28_6: FOREST, h36_6: FOREST_RUNE, h38_6: FOREST_RUNE, h46_6: FOREST, h48_6: FOREST, h50_6: OPEN_RUNE, h52_6: OPEN_RUNE, h54_6: FOREST, h1_7: FOREST, h3_7: FOREST, h5_7: FOREST, h11_7: FOREST, h17_7: FOREST, h19_7: FOREST_RUNE, h21_7: FOREST, h23_7: CITY, h25_7: CITY, h27_7: FOREST, h29_7: FOREST, h31_7: OPEN_RUNE, h33_7: OPEN_RUNE, h35_7: FOREST_RUNE, h37_7: FOREST_RUNE, h39_7: FOREST_RUNE, h45_7: FOREST, h47_7: FOREST, h49_7: FOREST, h51_7: OPEN_RUNE, h53_7: FOREST, h0_5: FOREST, h2_5: FOREST, h4_5: FOREST, h6_5: FOREST_RUNE, h8_5: OPEN_RUNE, h10_5: FOREST_RUNE, h14_5: FOREST, h16_5: FOREST, h20_5: FOREST, h22_5: OPEN_RUNE, h26_5: FOREST, h28_5: FOREST_RUNE, h30_5: FOREST, h32_5: OPEN_RUNE, h34_5: FOREST_RUNE, h38_5: FOREST, h42_5: OPEN_RUNE, h46_5: FOREST, h48_5: FOREST_RUNE, h50_5: FOREST, h52_5: OPEN_RUNE, h1_6: FOREST, h3_6: FOREST, h5_6: FOREST, h9_6: CITY, h11_6: FOREST_RUNE, h13_6: OPEN_RUNE, h15_6: FOREST, h17_6: FOREST, h21_6: FOREST_RUNE, h23_6: OPEN_RUNE, h27_6: FOREST, h29_6: FOREST, h33_6: FOREST_RUNE, h35_6: OPEN_RUNE, h37_6: FOREST, h39_6: FOREST, h43_6: CITY, h45_6: FOREST_RUNE, h47_6: FOREST_RUNE, h49_6: FOREST, h51_6: FOREST_RUNE, h53_6: FOREST, h0_4: FOREST, h2_4: FOREST, h4_4: FOREST, h6_4: FOREST, h10_4: FOREST_RUNE, h18_4: FOREST, h20_4: FOREST, h22_4: FOREST, h26_4: FOREST, h28_4: FOREST_RUNE, h30_4: FOREST, h34_4: OPEN_RUNE, h38_4: FOREST, h42_4: FOREST_RUNE, h46_4: FOREST_RUNE, h48_4: FOREST, h50_4: FOREST_RUNE, h52_4: OPEN_RUNE, h54_4: OPEN_RUNE, h1_5: FOREST_RUNE, h3_5: FOREST, h5_5: FOREST, h7_5: OPEN_RUNE, h9_5: FOREST, h11_5: FOREST, h15_5: FOREST, h17_5: FOREST, h19_5: CITY, h21_5: FOREST, h23_5: FOREST_RUNE, h25_5: FOREST, h27_5: FOREST, h29_5: FOREST_RUNE, h33_5: CITY, h35_5: CITY, h37_5: FOREST, h39_5: FOREST, h45_5: FOREST_RUNE, h47_5: FOREST, h49_5: FOREST_RUNE, h53_5: OPEN_RUNE, h2_3: FOREST, h4_3: FOREST, h6_3: FOREST, h10_3: FOREST_RUNE, h12_3: FOREST, h14_3: CITY, h16_3: FOREST, h18_3: FOREST_RUNE, h20_3: FOREST, h22_3: FOREST_RUNE, h24_3: FOREST, h26_3: FOREST, h28_3: FOREST, h30_3: FOREST, h32_3: FOREST, h34_3: FOREST_RUNE, h36_3: OPEN_RUNE, h38_3: FOREST, h40_3: FOREST_RUNE, h42_3: FOREST_RUNE, h46_3: FOREST, h48_3: FOREST_RUNE, h50_3: FOREST, h52_3: FOREST, h1_4: FOREST_RUNE, h3_4: FOREST, h5_4: FOREST, h7_4: FOREST_RUNE, h9_4: FOREST_RUNE, h11_4: FOREST, h13_4: CITY, h17_4: FOREST, h19_4: FOREST, h21_4: FOREST, h23_4: FOREST, h25_4: FOREST, h27_4: FOREST_RUNE, h29_4: FOREST, h31_4: OPEN_RUNE, h33_4: FOREST_RUNE, h35_4: OPEN_RUNE, h39_4: FOREST, h45_4: FOREST, h47_4: FOREST_RUNE, h49_4: FOREST_RUNE, h51_4: OPEN_RUNE, h53_4: FOREST, h2_2: FOREST_RUNE, h4_2: FOREST, h6_2: FOREST, h8_2: FOREST_RUNE, h10_2: FOREST, h12_2: FOREST_RUNE, h14_2: FOREST, h16_2: FOREST_RUNE, h18_2: FOREST_RUNE, h22_2: FOREST_RUNE, h24_2: FOREST, h26_2: FOREST, h28_2: FOREST_RUNE, h30_2: FOREST, h32_2: OPEN_RUNE, h40_2: FOREST, h42_2: FOREST_RUNE, h44_2: FOREST, h46_2: FOREST_RUNE, h50_2: FOREST, h52_2: FOREST, h1_3: FOREST_RUNE, h3_3: FOREST, h5_3: FOREST, h7_3: FOREST, h9_3: FOREST_RUNE, h11_3: FOREST_RUNE, h13_3: FOREST, h15_3: FOREST_RUNE, h17_3: FOREST_RUNE, h19_3: FOREST, h21_3: FOREST, h23_3: FOREST, h25_3: FOREST, h27_3: FOREST_RUNE, h29_3: FOREST, h31_3: OPEN_RUNE, h33_3: FOREST_RUNE, h37_3: FOREST, h39_3: FOREST, h41_3: FOREST_RUNE, h49_3: FOREST, h51_3: FOREST, h53_3: FOREST, h2_1: FOREST_RUNE, h4_1: FOREST, h6_1: FOREST, h8_1: FOREST, h10_1: OPEN_RUNE, h12_1: FOREST_RUNE, h14_1: FOREST_RUNE, h22_1: FOREST, h30_1: FOREST, h32_1: OPEN_RUNE, h34_1: OPEN_RUNE, h40_1: FOREST_RUNE, h42_1: FOREST_RUNE, h46_1: OPEN_RUNE, h50_1: FOREST_RUNE, h52_1: FOREST, h1_2: FOREST, h3_2: FOREST, h5_2: FOREST, h7_2: FOREST, h9_2: FOREST, h11_2: FOREST, h13_2: FOREST, h15_2: FOREST, h21_2: FOREST_RUNE, h23_2: FOREST, h27_2: FOREST, h29_2: FOREST_RUNE, h31_2: FOREST, h33_2: CITY, h35_2: OPEN_RUNE, h45_2: FOREST, h49_2: FOREST_RUNE, h51_2: FOREST, h0_0: FOREST, h2_0: CITY, h4_0: FOREST, h6_0: FOREST, h8_0: FOREST_RUNE, h10_0: CITY, h12_0: FOREST_RUNE, h14_0: FOREST, h16_0: OPEN_RUNE, h18_0: CITY, h20_0: OPEN_RUNE, h22_0: FOREST, h28_0: FOREST_RUNE, h30_0: FOREST, h32_0: OPEN_RUNE, h36_0: FOREST, h40_0: FOREST_RUNE, h42_0: OPEN_RUNE, h44_0: FOREST_RUNE, h46_0: FOREST, h48_0: FOREST, h50_0: OPEN_RUNE, h52_0: FOREST, h54_0: FOREST, h3_1: FOREST, h5_1: FOREST_RUNE, h7_1: FOREST, h9_1: FOREST, h11_1: FOREST_RUNE, h13_1: FOREST, h15_1: FOREST_RUNE, h21_1: FOREST_RUNE, h29_1: OPEN_RUNE, h31_1: FOREST, h35_1: FOREST, h41_1: FOREST_RUNE, h43_1: OPEN_RUNE, h45_1: FOREST_RUNE, h49_1: OPEN_RUNE, h51_1: FOREST_RUNE, h53_1: FOREST, h3_0: FOREST, h5_0: FOREST, h7_0: FOREST_RUNE, h9_0: FOREST, h11_0: FOREST, h13_0: FOREST, h15_0: FOREST, h19_0: OPEN_RUNE, h21_0: OPEN_RUNE, h23_0: FOREST, h27_0: OPEN_RUNE, h29_0: FOREST_RUNE, h31_0: OPEN_RUNE, h33_0: FOREST_RUNE, h35_0: FOREST, h39_0: CITY, h41_0: CITY, h43_0: FOREST, h45_0: FOREST, h51_0: FOREST_RUNE, h53_0: FOREST};
    
    return function(hexId) {
        return hexInfo[hexId] || OPEN;
    };
})();

function getOddsText(odds) {
    return odds.getElementsByTagNameNS(SVGNS, 'text')[0];
}
function removeAttack(sourceHex, targetHex) {
    delete targetHex.attacking[sourceHex.id];
    var remove = toArray(byId('overlays').children).filter(function(element) {
        return element.from && element.to;
    }).filter(function(element) {
        return (element.from.id === sourceHex.id && element.to.id === targetHex.id);
    });
    remove.forEach(function(it) {
        delete targetHex.attackArrows[targetHex.attackArrows.indexOf(it.id)];
        it.remove();
    });
    if (targetHex.attacking.length === 0) {
        delete targetHex.attacking;
    }
}
function setAttack(sourceHex, targetHex) {
    targetHex.attacking[sourceHex.id] = sourceHex;
    var arrow = sprites.attackArrow.cloneNode(true);
    arrow.id = 'arrow_' + targetHex.id + "_" + sourceHex.id;
    arrow.from = sourceHex;
    arrow.to = targetHex;
    placeArrow(arrow, sourceHex, targetHex, 'overlays');
    if (!targetHex.attackArrows) {
        targetHex.attackArrows = [];
    }
    targetHex.attackArrows.push(arrow.id);
}
function setOdds(targetHex, value) {
    if (!targetHex.odds) {
        targetHex.odds = sprites.target.cloneNode(true);
        targetHex.odds.id = targetHex.id + "_odds";
        copyTransformation(targetHex, targetHex.odds);
        byId('overlays').appendChild(targetHex.odds);
    }
    getOddsText(targetHex.odds).textContent = value;
}
function removeOdds(targetHex) {
    if (targetHex.odds) {
        targetHex.odds.remove();
        delete targetHex.odds;
    }
}
JoinAttack = function(data) {
    function sum(a, b) {
        return (a || 0) + b;
    }

    var targetHex = byId(data.targetHex);
    var sourceHex = byId(data.sourceHex);
    if (!targetHex.attacking) {
        targetHex.attacking = {};
    }
    var undo;
    if (sourceHex.id in targetHex.attacking) {
        removeAttack(sourceHex, targetHex);
        undo = setAttack;
    } else {
        setAttack(sourceHex, targetHex);
        undo = removeAttack;
    }
    var targetHexInfo = getHexInfo(targetHex.id);

    var defence = targetHex.stack.map(function(unit) {
        return getUnitInfo(unit).defence;
    }).reduce(sum, 0) * targetHexInfo.defenceMod;

    var attack = _.values(targetHex.attacking).map(function(hex) {
        return hex.stack.map(function(unit) {
            return getUnitInfo(unit).attack;
        }).reduce(sum, 0);
    }).reduce(sum, 0);

    var initialOdds = targetHex.odds ? getOddsText(targetHex.odds).textContent : null;
    if (attack > 0) {
        setOdds(targetHex, attack + ':' + defence);
    } else {
        removeOdds(targetHex);
    }
    if (targetHex.odds) {
        targetHex.odds.children[0].style.stroke = targetHexInfo.defenceMod > 1 ? 'black' : 'none';
    }

    targetHex.odds.style.pointerEvents = 'auto';
    targetHex.odds.onclick = function() {
        if (getSelectedId() !== null && isAttack(byId(getSelectedId()), targetHex)) {
            $(targetHex).click();
            return;
        }
        Operations.insert({
            op: 'Attack',
            target: targetHex.id,
            server: {
                roll: {
                    count: 2,
                    sides: 6
                }
            }
        });
    };

    return function() {
        if (initialOdds === null) {
            removeOdds(targetHex);
        } else {
            setOdds(targetHex, initialOdds);
        }
        undo(sourceHex, targetHex);
    };
};


var isAttack = function(counter, targetHex) {
    var targetStackOwner = (targetHex.stack && targetHex.stack.map(function(it) {
        return ownerByCategory[it.category];
    })[0]);
    var counterOwner = ownerByCategory[counter.category];
    return (targetStackOwner !== undefined && counterOwner !== undefined && counterOwner !== targetStackOwner);
};

gameModule = function() {
    var original = {
        MoveOp: MoveOp,
        moveTo: moveTo
    };
    moveTo = function(counter, targetHex) {
        if (isAttack(counter, targetHex)) {
            Operations.insert({
                op: 'JoinAttack',
                sourceHex: counter.position.id,
                targetHex: targetHex.id,
            });
        } else {
            original.moveTo(counter, targetHex);
        }
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

var pathInfo = [{from: "h54_11", to: "h53_11", type: "road"}, {from: "h53_11", to: "h52_11", type: "road"}, {from: "h52_11", to: "h51_12", type: "road"}, {from: "h51_12", to: "h50_12", type: "road"}, {from: "h50_12", to: "h50_13", type: "road"}, {from: "h50_13", to: "h49_14", type: "road"}, {from: "h49_14", to: "h49_13", type: "road"}, {from: "h49_13", to: "h48_13", type: "road"}, {from: "h48_13", to: "h47_13", type: "road"}, {from: "h47_13", to: "h46_13", type: "road"}, {from: "h46_13", to: "h45_13", type: "road"}, {from: "h45_13", to: "h44_13", type: "road"}, {from: "h44_13", to: "h43_14", type: "road"}, {from: "h43_14", to: "h42_14", type: "road"}, {from: "h42_14", to: "h41_15", type: "road"}, {from: "h41_15", to: "h40_15", type: "road"}, {from: "h40_15", to: "h40_16", type: "road"}, {from: "h40_16", to: "h39_17", type: "road"}, {from: "h39_17", to: "h39_18", type: "road"}, {from: "h39_18", to: "h38_17", type: "road"}, {from: "h38_17", to: "h37_18", type: "road"}, {from: "h37_18", to: "h36_18", type: "road"}, {from: "h36_18", to: "h33_18", type: "road"}, {from: "h33_18", to: "h32_18", type: "road"}, {from: "h32_18", to: "h31_18", type: "road"}, {from: "h31_18", to: "h30_17", type: "road"}, {from: "h30_17", to: "h29_18", type: "road"}, {from: "h29_18", to: "h28_18", type: "road"}, {from: "h51_12", to: "h50_11", type: "road"}, {from: "h50_11", to: "h49_11", type: "road"}, {from: "h49_11", to: "h48_10", type: "road"}, {from: "h48_10", to: "h48_9", type: "road"}, {from: "h48_9", to: "h47_9", type: "road"}, {from: "h47_9", to: "h47_8", type: "road"}, {from: "h47_8", to: "h46_7", type: "road"}, {from: "h46_7", to: "h45_7", type: "road"}, {from: "h45_7", to: "h44_6", type: "road"}, {from: "h44_6", to: "h43_6", type: "road"}, {from: "h43_6", to: "h42_5", type: "road"}, {from: "h42_5", to: "h41_5", type: "road"}, {from: "h41_5", to: "h40_4", type: "road"}, {from: "h40_4", to: "h39_5", type: "road"}, {from: "h39_5", to: "h38_4", type: "road"}, {from: "h38_4", to: "h37_4", type: "road"}, {from: "h37_4", to: "h36_4", type: "road"}, {from: "h36_4", to: "h35_5", type: "road"}, {from: "h37_0", to: "h36_0", type: "road"}, {from: "h36_0", to: "h36_1", type: "road"}, {from: "h36_1", to: "h36_2", type: "road"}, {from: "h36_2", to: "h35_3", type: "road"}, {from: "h35_3", to: "h35_4", type: "road"}, {from: "h35_4", to: "h35_5", type: "road"}, {from: "h35_5", to: "h34_5", type: "road"}, {from: "h34_5", to: "h34_6", type: "road"}, {from: "h34_6", to: "h34_7", type: "road"}, {from: "h34_7", to: "h33_8", type: "road"}, {from: "h33_8", to: "h33_9", type: "road"}, {from: "h33_9", to: "h33_10", type: "road"}, {from: "h33_10", to: "h32_10", type: "road"}, {from: "h32_10", to: "h32_11", type: "road"}, {from: "h32_11", to: "h31_12", type: "road"}, {from: "h31_12", to: "h31_13", type: "road"}, {from: "h31_13", to: "h30_13", type: "road"}, {from: "h30_13", to: "h30_14", type: "road"}, {from: "h30_14", to: "h29_15", type: "road"}, {from: "h29_15", to: "h29_16", type: "road"}, {from: "h29_16", to: "h29_17", type: "road"}, {from: "h48_23", to: "h47_23", type: "road"}, {from: "h47_23", to: "h46_22", type: "road"}, {from: "h46_22", to: "h45_23", type: "road"}, {from: "h45_23", to: "h44_22", type: "road"}, {from: "h44_22", to: "h43_23", type: "road"}, {from: "h43_23", to: "h42_23", type: "road"}, {from: "h42_23", to: "h41_23", type: "road"}, {from: "h41_23", to: "h40_23", type: "road"}, {from: "h40_23", to: "h39_24", type: "road"}, {from: "h39_24", to: "h38_24", type: "road"}, {from: "h38_24", to: "h37_25", type: "road"}, {from: "h37_25", to: "h36_24", type: "road"}, {from: "h36_24", to: "h35_24", type: "road"}, {from: "h35_24", to: "h35_23", type: "road"}, {from: "h35_23", to: "h34_22", type: "road"}, {from: "h34_22", to: "h33_22", type: "road"}, {from: "h33_22", to: "h32_21", type: "road"}, {from: "h32_21", to: "h31_21", type: "road"}, {from: "h31_21", to: "h30_21", type: "road"}, {from: "h30_21", to: "h29_21", type: "road"}, {from: "h29_21", to: "h28_20", type: "road"}, {from: "h26_35", to: "h26_34", type: "road"}, {from: "h26_34", to: "h27_34", type: "road"}, {from: "h27_34", to: "h27_33", type: "road"}, {from: "h27_33", to: "h27_32", type: "road"}, {from: "h27_32", to: "h27_31", type: "road"}, {from: "h27_31", to: "h27_30", type: "road"}, {from: "h27_30", to: "h27_29", type: "road"}, {from: "h27_29", to: "h28_28", type: "road"}, {from: "h28_28", to: "h28_27", type: "road"}, {from: "h28_27", to: "h28_26", type: "road"}, {from: "h28_26", to: "h28_25", type: "road"}, {from: "h28_25", to: "h28_24", type: "road"}, {from: "h28_24", to: "h28_23", type: "road"}, {from: "h28_23", to: "h28_22", type: "road"}, {from: "h28_22", to: "h28_21", type: "road"}, {from: "h28_21", to: "h28_20", type: "road"}, {from: "h28_20", to: "h27_20", type: "road"}, {from: "h27_20", to: "h26_19", type: "road"}, {from: "h26_19", to: "h25_19", type: "road"}, {from: "h25_19", to: "h24_19", type: "road"}, {from: "h24_19", to: "h23_20", type: "road"}, {from: "h23_20", to: "h22_19", type: "road"}, {from: "h22_19", to: "h21_19", type: "road"}, {from: "h21_19", to: "h20_18", type: "road"}, {from: "h20_18", to: "h19_18", type: "road"}, {from: "h19_18", to: "h18_18", type: "road"}, {from: "h18_18", to: "h17_18", type: "road"}, {from: "h17_18", to: "h16_17", type: "road"}, {from: "h16_17", to: "h15_17", type: "road"}, {from: "h15_17", to: "h14_16", type: "road"}, {from: "h14_16", to: "h13_17", type: "road"}, {from: "h13_17", to: "h12_16", type: "road"}, {from: "h12_16", to: "h11_16", type: "road"}, {from: "h11_16", to: "h10_15", type: "road"}, {from: "h10_15", to: "h9_15", type: "road"}, {from: "h9_15", to: "h8_14", type: "road"}, {from: "h8_14", to: "h7_14", type: "road"}, {from: "h7_14", to: "h6_13", type: "road"}, {from: "h6_13", to: "h6_12", type: "road"}, {from: "h6_12", to: "h5_12", type: "road"}, {from: "h5_12", to: "h4_11", type: "road"}, {from: "h4_11", to: "h3_11", type: "road"}, {from: "h3_11", to: "h2_10", type: "road"}, {from: "h2_10", to: "h1_10", type: "road"}, {from: "h1_10", to: "h0_9", type: "road"}, {from: "h0_10", to: "h1_10", type: "road"}, {from: "h1_10", to: "h2_9", type: "road"}, {from: "h2_9", to: "h2_8", type: "road"}, {from: "h2_8", to: "h3_8", type: "road"}, {from: "h3_8", to: "h4_8", type: "road"}, {from: "h4_8", to: "h5_8", type: "road"}, {from: "h5_8", to: "h6_7", type: "road"}, {from: "h6_7", to: "h7_7", type: "road"}, {from: "h7_7", to: "h8_6", type: "road"}, {from: "h8_6", to: "h9_6", type: "road"}, {from: "h9_6", to: "h10_5", type: "road"}, {from: "h10_5", to: "h11_6", type: "road"}, {from: "h11_6", to: "h12_5", type: "road"}, {from: "h12_5", to: "h12_4", type: "road"}, {from: "h12_4", to: "h13_4", type: "road"}, {from: "h13_4", to: "h14_3", type: "road"}, {from: "h14_3", to: "h15_3", type: "road"}, {from: "h15_3", to: "h16_2", type: "road"}, {from: "h16_2", to: "h17_2", type: "road"}, {from: "h17_2", to: "h18_1", type: "road"}, {from: "h18_1", to: "h19_1", type: "road"}, {from: "h19_1", to: "h20_1", type: "road"}, {from: "h20_1", to: "h21_1", type: "road"}, {from: "h21_1", to: "h22_1", type: "road"}, {from: "h22_1", to: "h23_1", type: "road"}, {from: "h23_1", to: "h24_0", type: "road"}, {from: "h24_0", to: "h25_0", type: "road"}, {from: "h19_0", to: "h18_0", type: "road"}, {from: "h18_0", to: "h18_1", type: "road"}, {from: "h18_1", to: "h19_2", type: "road"}, {from: "h19_2", to: "h19_3", type: "road"}, {from: "h19_3", to: "h20_3", type: "road"}, {from: "h20_3", to: "h21_4", type: "road"}, {from: "h21_4", to: "h21_5", type: "road"}, {from: "h21_5", to: "h22_5", type: "road"}, {from: "h22_5", to: "h22_6", type: "road"}, {from: "h22_6", to: "h23_7", type: "road"}, {from: "h23_7", to: "h22_7", type: "road"}, {from: "h22_7", to: "h22_8", type: "road"}, {from: "h22_8", to: "h22_9", type: "road"}, {from: "h22_9", to: "h23_10", type: "road"}, {from: "h23_10", to: "h23_11", type: "road"}, {from: "h23_11", to: "h24_11", type: "road"}, {from: "h24_11", to: "h24_12", type: "road"}, {from: "h24_12", to: "h25_13", type: "road"}, {from: "h25_13", to: "h25_14", type: "road"}, {from: "h25_14", to: "h25_15", type: "road"}, {from: "h25_15", to: "h26_15", type: "road"}, {from: "h26_15", to: "h26_16", type: "road"}, {from: "h26_16", to: "h27_17", type: "road"}, {from: "h8_35", to: "h8_34", type: "road"}, {from: "h9_34", to: "h9_33", type: "road"}, {from: "h9_33", to: "h10_32", type: "road"}, {from: "h10_32", to: "h10_31", type: "road"}, {from: "h10_31", to: "h11_31", type: "road"}, {from: "h11_31", to: "h11_30", type: "road"}, {from: "h11_30", to: "h12_29", type: "road"}, {from: "h12_29", to: "h13_29", type: "road"}, {from: "h13_29", to: "h14_28", type: "road"}, {from: "h14_28", to: "h15_28", type: "road"}, {from: "h15_28", to: "h16_28", type: "road"}, {from: "h16_28", to: "h17_28", type: "road"}, {from: "h17_28", to: "h18_27", type: "road"}, {from: "h18_27", to: "h18_26", type: "road"}, {from: "h18_26", to: "h19_26", type: "road"}, {from: "h19_26", to: "h20_25", type: "road"}, {from: "h20_25", to: "h21_25", type: "road"}, {from: "h21_25", to: "h22_24", type: "road"}, {from: "h22_24", to: "h23_24", type: "road"}, {from: "h23_24", to: "h23_23", type: "road"}, {from: "h23_23", to: "h24_22", type: "road"}, {from: "h24_22", to: "h25_22", type: "road"}, {from: "h25_22", to: "h26_21", type: "road"}, {from: "h26_21", to: "h26_20", type: "road"}, {from: "h26_20", to: "h27_20", type: "road"}, {from: "h27_20", to: "h28_19", type: "road"}, {from: "h28_19", to: "h28_18", type: "road"}, {from: "h7_33", to: "h7_32", type: "road"}, {from: "h7_32", to: "h6_31", type: "road"}, {from: "h6_31", to: "h6_30", type: "road"}, {from: "h6_30", to: "h5_30", type: "road"}, {from: "h5_30", to: "h5_29", type: "road"}, {from: "h5_29", to: "h4_28", type: "road"}, {from: "h4_28", to: "h3_28", type: "road"}, {from: "h3_28", to: "h2_28", type: "road"}, {from: "h2_28", to: "h1_28", type: "road"}, {from: "h1_28", to: "h0_27", type: "road"}, {from: "h9_34", to: "h8_34", type: "road"}, {from: "h8_34", to: "h8_33", type: "road"}, {from: "h8_33", to: "h7_33", type: "road"}, {from: "h27_17", to: "h28_17", type: "road"}, {from: "h28_17", to: "h28_18", type: "road"}, {from: "h28_17", to: "h29_17", type: "road"}, {from: "h27_17", to: "h28_17", type: "interrupted"}, {from: "h28_17", to: "h29_17", type: "interrupted"}, {from: "h54_10", to: "h53_11", type: "trail"}, {from: "h53_11", to: "h53_12", type: "trail"}, {from: "h53_12", to: "h53_13", type: "trail"}, {from: "h53_13", to: "h52_13", type: "trail"}, {from: "h52_13", to: "h52_14", type: "trail"}, {from: "h52_14", to: "h52_15", type: "trail"}, {from: "h54_16", to: "h53_16", type: "trail"}, {from: "h53_16", to: "h52_15", type: "trail"}, {from: "h52_15", to: "h51_15", type: "trail"}, {from: "h51_15", to: "h50_15", type: "trail"}, {from: "h50_15", to: "h49_16", type: "trail"}, {from: "h49_16", to: "h50_16", type: "trail"}, {from: "h50_16", to: "h49_17", type: "trail"}, {from: "h49_17", to: "h48_17", type: "trail"}, {from: "h48_17", to: "h47_17", type: "trail"}, {from: "h47_17", to: "h46_17", type: "trail"}, {from: "h46_17", to: "h45_18", type: "trail"}, {from: "h45_18", to: "h44_18", type: "trail"}, {from: "h44_18", to: "h44_19", type: "trail"}, {from: "h44_19", to: "h43_20", type: "trail"}, {from: "h43_20", to: "h42_20", type: "trail"}, {from: "h42_20", to: "h41_21", type: "trail"}, {from: "h41_21", to: "h40_21", type: "trail"}, {from: "h40_21", to: "h39_22", type: "trail"}, {from: "h39_22", to: "h39_23", type: "trail"}, {from: "h39_23", to: "h39_24", type: "trail"}, {from: "h42_23", to: "h42_22", type: "trail"}, {from: "h42_22", to: "h41_22", type: "trail"}, {from: "h41_22", to: "h40_22", type: "trail"}, {from: "h40_22", to: "h40_21", type: "trail"}, {from: "h51_15", to: "h51_14", type: "trail"}, {from: "h51_14", to: "h50_13", type: "trail"}, {from: "h47_23", to: "h47_24", type: "trail"}, {from: "h47_24", to: "h46_23", type: "trail"}, {from: "h46_23", to: "h46_22", type: "trail"}, {from: "h45_21", to: "h45_20", type: "trail"}, {from: "h45_20", to: "h44_19", type: "trail"}, {from: "h44_18", to: "h43_18", type: "trail"}, {from: "h43_18", to: "h42_17", type: "trail"}, {from: "h42_17", to: "h41_18", type: "trail"}, {from: "h41_18", to: "h40_17", type: "trail"}, {from: "h40_17", to: "h40_16", type: "trail"}, {from: "h40_16", to: "h39_16", type: "trail"}, {from: "h39_16", to: "h38_16", type: "trail"}, {from: "h38_16", to: "h38_15", type: "trail"}, {from: "h38_15", to: "h37_16", type: "trail"}, {from: "h37_16", to: "h36_15", type: "trail"}, {from: "h36_15", to: "h35_15", type: "trail"}, {from: "h35_15", to: "h35_14", type: "trail"}, {from: "h35_14", to: "h34_13", type: "trail"}, {from: "h34_13", to: "h34_12", type: "trail"}, {from: "h34_12", to: "h34_11", type: "trail"}, {from: "h34_11", to: "h33_11", type: "trail"}, {from: "h33_11", to: "h33_10", type: "trail"}, {from: "h33_10", to: "h33_9", type: "trail"}, {from: "h33_9", to: "h32_8", type: "trail"}, {from: "h32_8", to: "h31_8", type: "trail"}, {from: "h31_8", to: "h30_7", type: "trail"}, {from: "h30_7", to: "h29_7", type: "trail"}, {from: "h29_7", to: "h28_7", type: "trail"}, {from: "h28_7", to: "h27_8", type: "trail"}, {from: "h27_8", to: "h26_7", type: "trail"}, {from: "h26_7", to: "h25_7", type: "trail"}, {from: "h25_7", to: "h24_7", type: "trail"}, {from: "h24_7", to: "h23_8", type: "trail"}, {from: "h23_8", to: "h22_7", type: "trail"}, {from: "h33_11", to: "h33_10", type: "interrupted"}, {from: "h33_10", to: "h32_10", type: "interrupted"}, {from: "h53_0", to: "h53_1", type: "rail"}, {from: "h53_1", to: "h52_1", type: "rail"}, {from: "h52_1", to: "h51_2", type: "rail"}, {from: "h51_2", to: "h50_2", type: "rail"}, {from: "h50_2", to: "h49_3", type: "rail"}, {from: "h49_3", to: "h48_3", type: "rail"}, {from: "h48_3", to: "h47_3", type: "rail"}, {from: "h47_3", to: "h46_3", type: "rail"}, {from: "h46_3", to: "h45_4", type: "rail"}, {from: "h45_4", to: "h45_5", type: "rail"}, {from: "h45_5", to: "h44_5", type: "rail"}, {from: "h44_5", to: "h43_6", type: "rail"}, {from: "h43_6", to: "h42_6", type: "rail"}, {from: "h42_6", to: "h41_7", type: "rail"}, {from: "h41_7", to: "h40_7", type: "rail"}, {from: "h40_7", to: "h39_8", type: "rail"}, {from: "h39_8", to: "h39_9", type: "rail"}, {from: "h39_9", to: "h38_9", type: "rail"}, {from: "h38_9", to: "h38_10", type: "rail"}, {from: "h38_10", to: "h37_11", type: "rail"}, {from: "h37_11", to: "h36_11", type: "rail"}, {from: "h36_11", to: "h36_12", type: "rail"}, {from: "h36_12", to: "h35_13", type: "rail"}, {from: "h35_13", to: "h34_13", type: "rail"}, {from: "h34_13", to: "h33_14", type: "rail"}, {from: "h33_14", to: "h32_14", type: "rail"}, {from: "h32_14", to: "h32_15", type: "rail"}, {from: "h32_15", to: "h31_16", type: "rail"}, {from: "h31_16", to: "h31_17", type: "rail"}, {from: "h31_17", to: "h30_17", type: "rail"}, {from: "h30_17", to: "h29_18", type: "rail"}, {from: "h29_18", to: "h28_18", type: "rail"}, {from: "h28_18", to: "h27_19", type: "rail"}, {from: "h27_19", to: "h27_20", type: "rail"}, {from: "h27_20", to: "h26_20", type: "rail"}, {from: "h26_20", to: "h25_21", type: "rail"}, {from: "h25_21", to: "h24_21", type: "rail"}, {from: "h24_21", to: "h24_22", type: "rail"}, {from: "h24_22", to: "h23_23", type: "rail"}, {from: "h23_23", to: "h22_23", type: "rail"}, {from: "h22_23", to: "h21_24", type: "rail"}, {from: "h21_24", to: "h20_24", type: "rail"}, {from: "h20_24", to: "h19_25", type: "rail"}, {from: "h19_25", to: "h18_25", type: "rail"}, {from: "h18_25", to: "h17_26", type: "rail"}, {from: "h17_26", to: "h17_27", type: "rail"}, {from: "h45_21", to: "h46_21", type: "trail"}, {from: "h46_21", to: "h46_22", type: "trail"}, {from: "h17_27", to: "h16_27", type: "rail"}, {from: "h16_27", to: "h15_28", type: "rail"}, {from: "h15_28", to: "h14_28", type: "rail"}, {from: "h14_28", to: "h13_29", type: "rail"}, {from: "h13_29", to: "h12_29", type: "rail"}, {from: "h12_29", to: "h11_30", type: "rail"}, {from: "h11_30", to: "h10_30", type: "rail"}, {from: "h10_30", to: "h10_31", type: "rail"}, {from: "h10_31", to: "h9_32", type: "rail"}, {from: "h9_32", to: "h8_32", type: "rail"}, {from: "h8_32", to: "h7_33", type: "rail"}, {from: "h7_33", to: "h6_33", type: "rail"}, {from: "h6_33", to: "h5_34", type: "rail"}, {from: "h5_34", to: "h4_34", type: "rail"}, {from: "h4_34", to: "h3_35", type: "rail"}, {from: "h54_7", to: "h53_7", type: "trail"}, {from: "h53_7", to: "h52_7", type: "trail"}, {from: "h52_7", to: "h51_7", type: "trail"}, {from: "h51_7", to: "h50_7", type: "trail"}, {from: "h50_7", to: "h50_8", type: "trail"}, {from: "h50_8", to: "h49_9", type: "trail"}, {from: "h49_9", to: "h48_9", type: "trail"}, {from: "h48_9", to: "h48_10", type: "trail"}, {from: "h48_10", to: "h48_11", type: "trail"}, {from: "h48_11", to: "h48_12", type: "trail"}, {from: "h48_12", to: "h48_13", type: "trail"}, {from: "h36_4", to: "h37_5", type: "trail"}, {from: "h37_5", to: "h38_5", type: "trail"}, {from: "h38_5", to: "h38_6", type: "trail"}, {from: "h38_6", to: "h39_7", type: "trail"}, {from: "h39_7", to: "h40_7", type: "trail"}, {from: "h40_7", to: "h41_8", type: "trail"}, {from: "h41_8", to: "h41_9", type: "trail"}, {from: "h41_9", to: "h42_9", type: "trail"}, {from: "h42_9", to: "h42_10", type: "trail"}, {from: "h42_10", to: "h43_11", type: "trail"}, {from: "h43_11", to: "h43_12", type: "trail"}, {from: "h43_12", to: "h42_12", type: "trail"}, {from: "h42_12", to: "h43_13", type: "trail"}, {from: "h43_13", to: "h43_14", type: "trail"}, {from: "h42_9", to: "h43_9", type: "trail"}, {from: "h43_9", to: "h43_8", type: "trail"}, {from: "h43_8", to: "h43_7", type: "trail"}, {from: "h43_7", to: "h43_6", type: "trail"}, {from: "h43_6", to: "h44_5", type: "trail"}, {from: "h44_5", to: "h44_4", type: "trail"}, {from: "h44_4", to: "h44_3", type: "trail"}, {from: "h44_3", to: "h44_2", type: "trail"}, {from: "h44_2", to: "h43_2", type: "trail"}, {from: "h43_2", to: "h43_1", type: "trail"}, {from: "h43_1", to: "h42_0", type: "trail"}, {from: "h42_0", to: "h41_0", type: "trail"}, {from: "h41_0", to: "h40_0", type: "trail"}, {from: "h47_0", to: "h47_1", type: "trail"}, {from: "h47_1", to: "h47_2", type: "trail"}, {from: "h47_2", to: "h47_3", type: "trail"}, {from: "h47_3", to: "h46_3", type: "trail"}, {from: "h46_3", to: "h45_4", type: "trail"}, {from: "h45_4", to: "h44_4", type: "trail"}, {from: "h42_12", to: "h41_13", type: "trail"}, {from: "h41_13", to: "h40_13", type: "trail"}, {from: "h40_13", to: "h39_14", type: "trail"}, {from: "h39_14", to: "h38_13", type: "trail"}, {from: "h38_13", to: "h37_14", type: "trail"}, {from: "h37_14", to: "h36_14", type: "trail"}, {from: "h36_14", to: "h35_15", type: "trail"}, {from: "h35_15", to: "h34_15", type: "trail"}, {from: "h34_15", to: "h33_16", type: "trail"}, {from: "h33_16", to: "h32_16", type: "trail"}, {from: "h32_16", to: "h31_17", type: "trail"}, {from: "h31_17", to: "h30_17", type: "trail"}, {from: "h35_15", to: "h35_16", type: "trail"}, {from: "h35_16", to: "h36_16", type: "trail"}, {from: "h36_16", to: "h36_17", type: "trail"}, {from: "h36_17", to: "h36_18", type: "trail"}, {from: "h36_18", to: "h35_19", type: "trail"}, {from: "h35_19", to: "h34_19", type: "trail"}, {from: "h34_19", to: "h34_20", type: "trail"}, {from: "h34_20", to: "h33_21", type: "trail"}, {from: "h33_21", to: "h33_22", type: "trail"}, {from: "h33_22", to: "h33_23", type: "trail"}, {from: "h33_23", to: "h33_24", type: "trail"}, {from: "h33_24", to: "h33_25", type: "trail"}, {from: "h34_19", to: "h33_19", type: "trail"}, {from: "h33_19", to: "h32_19", type: "trail"}, {from: "h32_19", to: "h31_19", type: "trail"}, {from: "h31_19", to: "h30_18", type: "trail"}, {from: "h30_18", to: "h29_19", type: "trail"}, {from: "h29_19", to: "h28_18", type: "trail"}, {from: "h33_19", to: "h33_20", type: "trail"}, {from: "h33_20", to: "h34_20", type: "trail"}, {from: "h29_19", to: "h29_20", type: "trail"}, {from: "h28_19", to: "h29_20", type: "trail"}, {from: "h29_20", to: "h30_20", type: "trail"}, {from: "h30_20", to: "h31_21", type: "trail"}, {from: "h31_21", to: "h31_22", type: "trail"}, {from: "h31_22", to: "h32_22", type: "trail"}, {from: "h32_22", to: "h33_23", type: "trail"}, {from: "h33_23", to: "h34_23", type: "trail"}, {from: "h34_23", to: "h35_24", type: "trail"}, {from: "h39_22", to: "h38_21", type: "trail"}, {from: "h38_21", to: "h37_21", type: "trail"}, {from: "h37_21", to: "h36_20", type: "trail"}, {from: "h36_20", to: "h35_21", type: "trail"}, {from: "h35_21", to: "h34_20", type: "trail"}, {from: "h32_0", to: "h32_1", type: "trail"}, {from: "h32_1", to: "h33_2", type: "trail"}, {from: "h33_2", to: "h34_1", type: "trail"}, {from: "h34_1", to: "h34_2", type: "trail"}, {from: "h34_2", to: "h34_3", type: "trail"}, {from: "h34_3", to: "h34_4", type: "trail"}, {from: "h34_4", to: "h35_5", type: "trail"}, {from: "h34_4", to: "h33_5", type: "trail"}, {from: "h33_5", to: "h33_6", type: "trail"}, {from: "h33_6", to: "h32_6", type: "trail"}, {from: "h32_6", to: "h32_7", type: "trail"}, {from: "h32_7", to: "h32_8", type: "trail"}, {from: "h32_8", to: "h31_9", type: "trail"}, {from: "h31_9", to: "h31_10", type: "trail"}, {from: "h31_10", to: "h31_11", type: "trail"}, {from: "h31_11", to: "h31_12", type: "trail"}, {from: "h33_5", to: "h32_5", type: "trail"}, {from: "h32_5", to: "h31_6", type: "trail"}, {from: "h31_6", to: "h30_6", type: "trail"}, {from: "h30_6", to: "h30_7", type: "trail"}, {from: "h23_7", to: "h24_6", type: "trail"}, {from: "h24_6", to: "h24_5", type: "trail"}, {from: "h24_5", to: "h24_4", type: "trail"}, {from: "h24_4", to: "h25_4", type: "trail"}, {from: "h25_4", to: "h25_3", type: "trail"}, {from: "h25_3", to: "h25_2", type: "trail"}, {from: "h25_2", to: "h25_1", type: "trail"}, {from: "h25_1", to: "h25_0", type: "trail"}, {from: "h48_32", to: "h47_33", type: "trail"}, {from: "h47_33", to: "h46_32", type: "trail"}, {from: "h46_32", to: "h45_33", type: "trail"}, {from: "h45_33", to: "h44_32", type: "trail"}, {from: "h44_32", to: "h43_32", type: "trail"}, {from: "h43_32", to: "h42_31", type: "trail"}, {from: "h42_31", to: "h41_31", type: "trail"}, {from: "h41_31", to: "h41_30", type: "trail"}, {from: "h41_30", to: "h40_29", type: "trail"}, {from: "h43_32", to: "h43_33", type: "trail"}, {from: "h43_35", to: "h44_34", type: "trail"}, {from: "h43_33", to: "h43_34", type: "trail"}, {from: "h43_34", to: "h44_34", type: "trail"}, {from: "h48_26", to: "h47_27", type: "trail"}, {from: "h47_27", to: "h47_28", type: "trail"}, {from: "h47_28", to: "h46_28", type: "trail"}, {from: "h46_28", to: "h45_28", type: "trail"}, {from: "h45_28", to: "h45_29", type: "trail"}, {from: "h45_29", to: "h45_30", type: "trail"}, {from: "h45_30", to: "h44_30", type: "trail"}, {from: "h44_30", to: "h43_31", type: "trail"}, {from: "h43_31", to: "h42_30", type: "trail"}, {from: "h42_30", to: "h42_31", type: "trail"}, {from: "h42_31", to: "h41_32", type: "trail"}, {from: "h41_32", to: "h40_32", type: "trail"}, {from: "h40_32", to: "h39_33", type: "trail"}, {from: "h39_33", to: "h38_33", type: "trail"}, {from: "h38_33", to: "h37_34", type: "trail"}, {from: "h40_32", to: "h40_33", type: "trail"}, {from: "h40_33", to: "h40_34", type: "trail"}, {from: "h38_33", to: "h38_34", type: "trail"}, {from: "h38_34", to: "h39_35", type: "trail"}, {from: "h37_34", to: "h36_34", type: "trail"}, {from: "h36_34", to: "h36_33", type: "trail"}, {from: "h36_33", to: "h35_33", type: "trail"}, {from: "h35_33", to: "h35_32", type: "trail"}, {from: "h35_32", to: "h35_31", type: "trail"}, {from: "h35_31", to: "h34_31", type: "trail"}, {from: "h34_31", to: "h33_31", type: "trail"}, {from: "h33_31", to: "h33_30", type: "trail"}, {from: "h33_30", to: "h33_29", type: "trail"}, {from: "h33_29", to: "h32_29", type: "trail"}, {from: "h32_29", to: "h32_28", type: "trail"}, {from: "h32_28", to: "h31_28", type: "trail"}, {from: "h31_28", to: "h30_27", type: "trail"}, {from: "h30_27", to: "h30_26", type: "trail"}, {from: "h30_26", to: "h30_25", type: "trail"}, {from: "h30_25", to: "h29_25", type: "trail"}, {from: "h29_25", to: "h29_24", type: "trail"}, {from: "h29_24", to: "h29_23", type: "trail"}, {from: "h29_23", to: "h29_22", type: "trail"}, {from: "h29_22", to: "h28_21", type: "trail"}, {from: "h32_28", to: "h31_29", type: "trail"}, {from: "h31_29", to: "h30_29", type: "trail"}, {from: "h36_33", to: "h35_34", type: "trail"}, {from: "h35_34", to: "h34_34", type: "trail"}, {from: "h34_34", to: "h33_35", type: "trail"}, {from: "h33_35", to: "h32_34", type: "trail"}, {from: "h32_34", to: "h32_33", type: "trail"}, {from: "h32_33", to: "h31_33", type: "trail"}, {from: "h31_33", to: "h31_32", type: "trail"}, {from: "h31_32", to: "h31_31", type: "trail"}, {from: "h31_31", to: "h31_30", type: "trail"}, {from: "h31_30", to: "h30_29", type: "trail"}, {from: "h30_29", to: "h29_29", type: "trail"}, {from: "h29_29", to: "h28_28", type: "trail"}, {from: "h28_28", to: "h28_27", type: "trail"}, {from: "h28_27", to: "h27_28", type: "trail"}, {from: "h27_28", to: "h26_27", type: "trail"}, {from: "h26_27", to: "h26_26", type: "trail"}, {from: "h26_26", to: "h25_26", type: "trail"}, {from: "h25_26", to: "h24_25", type: "trail"}, {from: "h29_29", to: "h28_28", type: "interrupted"}, {from: "h28_28", to: "h27_29", type: "interrupted"}, {from: "h31_28", to: "h30_27", type: "interrupted"}, {from: "h30_27", to: "h29_28", type: "interrupted"}, {from: "h31_33", to: "h30_33", type: "trail"}, {from: "h30_33", to: "h29_33", type: "trail"}, {from: "h29_33", to: "h28_32", type: "trail"}, {from: "h28_32", to: "h27_32", type: "trail"}, {from: "h27_32", to: "h26_31", type: "trail"}, {from: "h26_31", to: "h25_32", type: "trail"}, {from: "h25_32", to: "h24_31", type: "trail"}, {from: "h24_31", to: "h24_30", type: "trail"}, {from: "h24_30", to: "h23_31", type: "trail"}, {from: "h23_31", to: "h23_32", type: "trail"}, {from: "h23_32", to: "h23_33", type: "trail"}, {from: "h23_33", to: "h22_33", type: "trail"}, {from: "h22_33", to: "h21_33", type: "trail"}, {from: "h21_33", to: "h20_33", type: "trail"}, {from: "h20_33", to: "h19_34", type: "trail"}, {from: "h19_34", to: "h18_34", type: "trail"}, {from: "h18_34", to: "h17_35", type: "trail"}, {from: "h15_35", to: "h16_34", type: "trail"}, {from: "h16_34", to: "h17_34", type: "trail"}, {from: "h17_34", to: "h18_33", type: "trail"}, {from: "h18_33", to: "h18_32", type: "trail"}, {from: "h18_32", to: "h19_33", type: "trail"}, {from: "h19_33", to: "h20_33", type: "trail"}, {from: "h20_33", to: "h20_34", type: "trail"}, {from: "h20_34", to: "h20_35", type: "trail"}, {from: "h18_32", to: "h19_32", type: "trail"}, {from: "h19_32", to: "h19_31", type: "trail"}, {from: "h19_31", to: "h20_30", type: "trail"}, {from: "h20_30", to: "h20_29", type: "trail"}, {from: "h20_29", to: "h21_29", type: "trail"}, {from: "h21_29", to: "h21_28", type: "trail"}, {from: "h21_28", to: "h22_27", type: "trail"}, {from: "h22_27", to: "h23_27", type: "trail"}, {from: "h23_27", to: "h23_26", type: "trail"}, {from: "h23_26", to: "h24_25", type: "trail"}, {from: "h24_25", to: "h24_24", type: "trail"}, {from: "h24_24", to: "h25_24", type: "trail"}, {from: "h25_24", to: "h25_23", type: "trail"}, {from: "h25_23", to: "h26_22", type: "trail"}, {from: "h26_22", to: "h27_22", type: "trail"}, {from: "h27_22", to: "h27_21", type: "trail"}, {from: "h27_21", to: "h27_20", type: "trail"}, {from: "h18_32", to: "h17_32", type: "trail"}, {from: "h17_32", to: "h18_31", type: "trail"}, {from: "h18_31", to: "h17_31", type: "trail"}, {from: "h17_31", to: "h16_30", type: "trail"}, {from: "h16_30", to: "h16_29", type: "trail"}, {from: "h16_29", to: "h15_29", type: "trail"}, {from: "h15_29", to: "h15_28", type: "trail"}, {from: "h15_28", to: "h14_28", type: "trail"}, {from: "h14_28", to: "h14_27", type: "trail"}, {from: "h14_27", to: "h13_27", type: "trail"}, {from: "h13_27", to: "h14_26", type: "trail"}, {from: "h14_26", to: "h15_26", type: "trail"}, {from: "h15_26", to: "h16_25", type: "trail"}, {from: "h16_25", to: "h17_25", type: "trail"}, {from: "h17_25", to: "h18_25", type: "trail"}, {from: "h18_25", to: "h18_26", type: "trail"}, {from: "h18_26", to: "h19_27", type: "trail"}, {from: "h19_27", to: "h20_27", type: "trail"}, {from: "h20_27", to: "h21_28", type: "trail"}, {from: "h21_28", to: "h22_28", type: "trail"}, {from: "h22_28", to: "h22_29", type: "trail"}, {from: "h22_29", to: "h23_30", type: "trail"}, {from: "h23_30", to: "h23_31", type: "trail"}, {from: "h13_29", to: "h14_28", type: "interrupted"}, {from: "h14_28", to: "h14_27", type: "interrupted"}, {from: "h17_25", to: "h17_24", type: "trail"}, {from: "h17_24", to: "h17_23", type: "trail"}, {from: "h17_23", to: "h17_22", type: "trail"}, {from: "h17_22", to: "h18_21", type: "trail"}, {from: "h18_21", to: "h17_21", type: "trail"}, {from: "h17_21", to: "h17_20", type: "trail"}, {from: "h17_20", to: "h18_19", type: "trail"}, {from: "h18_19", to: "h18_18", type: "trail"}, {from: "h18_19", to: "h19_20", type: "trail"}, {from: "h19_20", to: "h20_19", type: "trail"}, {from: "h20_19", to: "h21_19", type: "trail"}, {from: "h18_18", to: "h18_19", type: "interrupted"}, {from: "h18_19", to: "h19_20", type: "interrupted"}, {from: "h17_20", to: "h16_20", type: "trail"}, {from: "h16_20", to: "h15_20", type: "trail"}, {from: "h15_20", to: "h14_20", type: "trail"}, {from: "h14_20", to: "h13_20", type: "trail"}, {from: "h13_20", to: "h12_20", type: "trail"}, {from: "h12_20", to: "h11_21", type: "trail"}, {from: "h11_21", to: "h11_22", type: "trail"}, {from: "h11_22", to: "h11_23", type: "trail"}, {from: "h11_23", to: "h11_24", type: "trail"}, {from: "h11_24", to: "h11_25", type: "trail"}, {from: "h11_25", to: "h12_25", type: "trail"}, {from: "h12_25", to: "h12_26", type: "trail"}, {from: "h12_26", to: "h13_27", type: "trail"}, {from: "h13_27", to: "h12_27", type: "trail"}, {from: "h12_27", to: "h11_27", type: "trail"}, {from: "h11_27", to: "h10_27", type: "trail"}, {from: "h10_27", to: "h9_28", type: "trail"}, {from: "h9_28", to: "h10_28", type: "trail"}, {from: "h10_28", to: "h10_29", type: "trail"}, {from: "h10_29", to: "h11_30", type: "trail"}, {from: "h11_22", to: "h10_22", type: "trail"}, {from: "h10_22", to: "h9_22", type: "trail"}, {from: "h9_22", to: "h8_22", type: "trail"}, {from: "h8_22", to: "h7_23", type: "trail"}, {from: "h7_23", to: "h6_23", type: "trail"}, {from: "h6_23", to: "h6_24", type: "trail"}, {from: "h6_24", to: "h5_25", type: "trail"}, {from: "h5_25", to: "h5_26", type: "trail"}, {from: "h5_26", to: "h5_27", type: "trail"}, {from: "h5_27", to: "h5_28", type: "trail"}, {from: "h5_28", to: "h5_29", type: "trail"}, {from: "h9_28", to: "h8_28", type: "trail"}, {from: "h8_28", to: "h7_28", type: "trail"}, {from: "h7_28", to: "h7_29", type: "trail"}, {from: "h7_29", to: "h6_29", type: "trail"}, {from: "h6_29", to: "h5_30", type: "trail"}, {from: "h5_30", to: "h4_30", type: "trail"}, {from: "h4_30", to: "h4_31", type: "trail"}, {from: "h4_31", to: "h3_32", type: "trail"}, {from: "h3_32", to: "h2_32", type: "trail"}, {from: "h2_32", to: "h2_33", type: "trail"}, {from: "h2_33", to: "h1_34", type: "trail"}, {from: "h1_34", to: "h1_35", type: "trail"}, {from: "h4_28", to: "h4_27", type: "trail"}, {from: "h4_27", to: "h3_27", type: "trail"}, {from: "h3_27", to: "h3_26", type: "trail"}, {from: "h3_26", to: "h2_25", type: "trail"}, {from: "h2_25", to: "h1_25", type: "trail"}, {from: "h1_25", to: "h1_24", type: "trail"}, {from: "h1_24", to: "h0_23", type: "trail"}, {from: "h0_17", to: "h1_18", type: "trail"}, {from: "h1_18", to: "h2_17", type: "trail"}, {from: "h2_17", to: "h3_18", type: "trail"}, {from: "h3_18", to: "h4_17", type: "trail"}, {from: "h4_17", to: "h5_17", type: "trail"}, {from: "h5_17", to: "h6_16", type: "trail"}, {from: "h6_16", to: "h7_16", type: "trail"}, {from: "h7_16", to: "h8_15", type: "trail"}, {from: "h8_15", to: "h9_15", type: "trail"}, {from: "h11_27", to: "h10_26", type: "trail"}, {from: "h10_26", to: "h9_26", type: "trail"}, {from: "h9_26", to: "h8_25", type: "trail"}, {from: "h8_25", to: "h7_25", type: "trail"}, {from: "h7_25", to: "h7_24", type: "trail"}, {from: "h7_24", to: "h6_23", type: "trail"}, {from: "h6_23", to: "h5_23", type: "trail"}, {from: "h5_23", to: "h5_22", type: "trail"}, {from: "h5_22", to: "h4_22", type: "trail"}, {from: "h4_22", to: "h4_21", type: "trail"}, {from: "h4_21", to: "h3_21", type: "trail"}, {from: "h3_21", to: "h3_20", type: "trail"}, {from: "h3_20", to: "h3_19", type: "trail"}, {from: "h3_19", to: "h2_18", type: "trail"}, {from: "h2_18", to: "h2_17", type: "trail"}, {from: "h2_17", to: "h2_16", type: "trail"}, {from: "h2_16", to: "h2_15", type: "trail"}, {from: "h2_15", to: "h1_15", type: "trail"}, {from: "h1_15", to: "h0_14", type: "trail"}, {from: "h0_14", to: "h0_13", type: "trail"}, {from: "h4_21", to: "h5_21", type: "trail"}, {from: "h6_16", to: "h5_16", type: "trail"}, {from: "h5_16", to: "h4_15", type: "trail"}, {from: "h4_15", to: "h5_15", type: "trail"}, {from: "h5_15", to: "h6_14", type: "trail"}, {from: "h6_14", to: "h6_13", type: "trail"}, {from: "h6_13", to: "h7_13", type: "trail"}, {from: "h7_13", to: "h7_12", type: "trail"}, {from: "h7_12", to: "h6_11", type: "trail"}, {from: "h6_11", to: "h6_10", type: "trail"}, {from: "h6_10", to: "h6_9", type: "trail"}, {from: "h6_9", to: "h6_8", type: "trail"}, {from: "h6_8", to: "h6_7", type: "trail"}, {from: "h7_13", to: "h8_12", type: "trail"}, {from: "h8_12", to: "h9_13", type: "trail"}, {from: "h9_13", to: "h10_12", type: "trail"}, {from: "h10_12", to: "h11_13", type: "trail"}, {from: "h11_13", to: "h12_12", type: "trail"}, {from: "h10_15", to: "h10_14", type: "trail"}, {from: "h10_14", to: "h11_14", type: "trail"}, {from: "h11_14", to: "h11_13", type: "trail"}, {from: "h11_14", to: "h11_13", type: "interrupted"}, {from: "h11_13", to: "h10_12", type: "interrupted"}, {from: "h9_6", to: "h8_5", type: "trail"}, {from: "h8_5", to: "h7_5", type: "trail"}, {from: "h7_5", to: "h7_4", type: "trail"}, {from: "h7_4", to: "h7_3", type: "trail"}, {from: "h7_3", to: "h7_2", type: "trail"}, {from: "h7_2", to: "h6_1", type: "trail"}, {from: "h6_1", to: "h5_2", type: "trail"}, {from: "h5_2", to: "h4_2", type: "trail"}, {from: "h4_2", to: "h3_2", type: "trail"}, {from: "h3_2", to: "h3_1", type: "trail"}, {from: "h3_1", to: "h2_0", type: "trail"}, {from: "h11_6", to: "h12_6", type: "trail"}, {from: "h12_6", to: "h13_6", type: "trail"}, {from: "h13_6", to: "h13_7", type: "trail"}, {from: "h13_7", to: "h14_7", type: "trail"}, {from: "h14_7", to: "h15_8", type: "trail"}, {from: "h15_8", to: "h15_9", type: "trail"}, {from: "h15_9", to: "h16_8", type: "trail"}, {from: "h16_8", to: "h17_8", type: "trail"}, {from: "h17_8", to: "h17_9", type: "trail"}, {from: "h17_9", to: "h18_9", type: "trail"}, {from: "h18_9", to: "h18_10", type: "trail"}, {from: "h18_10", to: "h19_11", type: "trail"}, {from: "h19_11", to: "h18_11", type: "trail"}, {from: "h18_11", to: "h18_12", type: "trail"}, {from: "h18_12", to: "h17_13", type: "trail"}, {from: "h17_13", to: "h16_13", type: "trail"}, {from: "h16_13", to: "h15_14", type: "trail"}, {from: "h15_14", to: "h14_14", type: "trail"}, {from: "h14_14", to: "h14_15", type: "trail"}, {from: "h14_15", to: "h14_16", type: "trail"}, {from: "h16_13", to: "h15_13", type: "trail"}, {from: "h15_13", to: "h14_13", type: "trail"}, {from: "h14_13", to: "h13_13", type: "trail"}, {from: "h13_13", to: "h12_12", type: "trail"}, {from: "h19_18", to: "h19_17", type: "trail"}, {from: "h19_17", to: "h20_16", type: "trail"}, {from: "h20_16", to: "h21_16", type: "trail"}, {from: "h21_16", to: "h22_15", type: "trail"}, {from: "h22_15", to: "h22_14", type: "trail"}, {from: "h22_14", to: "h21_14", type: "trail"}, {from: "h21_14", to: "h21_13", type: "trail"}, {from: "h21_13", to: "h20_12", type: "trail"}, {from: "h20_12", to: "h20_11", type: "trail"}, {from: "h20_11", to: "h19_12", type: "trail"}, {from: "h19_12", to: "h19_11", type: "trail"}, {from: "h19_11", to: "h19_10", type: "trail"}, {from: "h19_10", to: "h20_9", type: "trail"}, {from: "h20_9", to: "h21_9", type: "trail"}, {from: "h21_9", to: "h22_8", type: "trail"}, {from: "h29_16", to: "h28_15", type: "trail"}, {from: "h28_15", to: "h27_16", type: "trail"}, {from: "h27_16", to: "h26_16", type: "trail"}, {from: "h26_16", to: "h25_16", type: "trail"}, {from: "h25_16", to: "h24_15", type: "trail"}, {from: "h24_15", to: "h23_15", type: "trail"}, {from: "h23_15", to: "h22_14", type: "trail"}, {from: "h25_16", to: "h25_15", type: "trail"}, {from: "h40_34", to: "h40_35", type: "trail"}]

var ArtyType = {
    OTHER: 0,
    YELLOW: 1,
    GUNS_88: 2
};

getUnitInfo = (function() {
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
    
    return function(counter) {
        return unitInfo[counter.category + '\x1e' + counter.name];
    };
})();
})();
