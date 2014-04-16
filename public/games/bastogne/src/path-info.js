getPathInfo = (function() {
    var ROAD = {
        movement: 0.5
    };
    var TRAIL = {
        movement: 1
    };
    var RAIL = {
        movement: 1
    };
    var pathInfo = [{from: "h54_11", to: "h53_11", type: ROAD}, {from: "h53_11", to: "h52_11", type: ROAD}, {from: "h52_11", to: "h51_12", type: ROAD}, {from: "h51_12", to: "h50_12", type: ROAD}, {from: "h50_12", to: "h50_13", type: ROAD}, {from: "h50_13", to: "h49_14", type: ROAD}, {from: "h49_14", to: "h49_13", type: ROAD}, {from: "h49_13", to: "h48_13", type: ROAD}, {from: "h48_13", to: "h47_13", type: ROAD}, {from: "h47_13", to: "h46_13", type: ROAD}, {from: "h46_13", to: "h45_13", type: ROAD}, {from: "h45_13", to: "h44_13", type: ROAD}, {from: "h44_13", to: "h43_14", type: ROAD}, {from: "h43_14", to: "h42_14", type: ROAD}, {from: "h42_14", to: "h41_15", type: ROAD}, {from: "h41_15", to: "h40_15", type: ROAD}, {from: "h40_15", to: "h40_16", type: ROAD}, {from: "h40_16", to: "h39_17", type: ROAD}, {from: "h39_17", to: "h39_18", type: ROAD}, {from: "h39_18", to: "h38_17", type: ROAD}, {from: "h38_17", to: "h37_18", type: ROAD}, {from: "h37_18", to: "h36_18", type: ROAD}, {from: "h36_18", to: "h33_18", type: ROAD}, {from: "h33_18", to: "h32_18", type: ROAD}, {from: "h32_18", to: "h31_18", type: ROAD}, {from: "h31_18", to: "h30_17", type: ROAD}, {from: "h30_17", to: "h29_18", type: ROAD}, {from: "h29_18", to: "h28_18", type: ROAD}, {from: "h51_12", to: "h50_11", type: ROAD}, {from: "h50_11", to: "h49_11", type: ROAD}, {from: "h49_11", to: "h48_10", type: ROAD}, {from: "h48_10", to: "h48_9", type: ROAD}, {from: "h48_9", to: "h47_9", type: ROAD}, {from: "h47_9", to: "h47_8", type: ROAD}, {from: "h47_8", to: "h46_7", type: ROAD}, {from: "h46_7", to: "h45_7", type: ROAD}, {from: "h45_7", to: "h44_6", type: ROAD}, {from: "h44_6", to: "h43_6", type: ROAD}, {from: "h43_6", to: "h42_5", type: ROAD}, {from: "h42_5", to: "h41_5", type: ROAD}, {from: "h41_5", to: "h40_4", type: ROAD}, {from: "h40_4", to: "h39_5", type: ROAD}, {from: "h39_5", to: "h38_4", type: ROAD}, {from: "h38_4", to: "h37_4", type: ROAD}, {from: "h37_4", to: "h36_4", type: ROAD}, {from: "h36_4", to: "h35_5", type: ROAD}, {from: "h37_0", to: "h36_0", type: ROAD}, {from: "h36_0", to: "h36_1", type: ROAD}, {from: "h36_1", to: "h36_2", type: ROAD}, {from: "h36_2", to: "h35_3", type: ROAD}, {from: "h35_3", to: "h35_4", type: ROAD}, {from: "h35_4", to: "h35_5", type: ROAD}, {from: "h35_5", to: "h34_5", type: ROAD}, {from: "h34_5", to: "h34_6", type: ROAD}, {from: "h34_6", to: "h34_7", type: ROAD}, {from: "h34_7", to: "h33_8", type: ROAD}, {from: "h33_8", to: "h33_9", type: ROAD}, {from: "h33_9", to: "h33_10", type: ROAD}, {from: "h33_10", to: "h32_10", type: ROAD}, {from: "h32_10", to: "h32_11", type: ROAD}, {from: "h32_11", to: "h31_12", type: ROAD}, {from: "h31_12", to: "h31_13", type: ROAD}, {from: "h31_13", to: "h30_13", type: ROAD}, {from: "h30_13", to: "h30_14", type: ROAD}, {from: "h30_14", to: "h29_15", type: ROAD}, {from: "h29_15", to: "h29_16", type: ROAD}, {from: "h29_16", to: "h29_17", type: ROAD}, {from: "h48_23", to: "h47_23", type: ROAD}, {from: "h47_23", to: "h46_22", type: ROAD}, {from: "h46_22", to: "h45_23", type: ROAD}, {from: "h45_23", to: "h44_22", type: ROAD}, {from: "h44_22", to: "h43_23", type: ROAD}, {from: "h43_23", to: "h42_23", type: ROAD}, {from: "h42_23", to: "h41_23", type: ROAD}, {from: "h41_23", to: "h40_23", type: ROAD}, {from: "h40_23", to: "h39_24", type: ROAD}, {from: "h39_24", to: "h38_24", type: ROAD}, {from: "h38_24", to: "h37_25", type: ROAD}, {from: "h37_25", to: "h36_24", type: ROAD}, {from: "h36_24", to: "h35_24", type: ROAD}, {from: "h35_24", to: "h35_23", type: ROAD}, {from: "h35_23", to: "h34_22", type: ROAD}, {from: "h34_22", to: "h33_22", type: ROAD}, {from: "h33_22", to: "h32_21", type: ROAD}, {from: "h32_21", to: "h31_21", type: ROAD}, {from: "h31_21", to: "h30_21", type: ROAD}, {from: "h30_21", to: "h29_21", type: ROAD}, {from: "h29_21", to: "h28_20", type: ROAD}, {from: "h26_35", to: "h26_34", type: ROAD}, {from: "h26_34", to: "h27_34", type: ROAD}, {from: "h27_34", to: "h27_33", type: ROAD}, {from: "h27_33", to: "h27_32", type: ROAD}, {from: "h27_32", to: "h27_31", type: ROAD}, {from: "h27_31", to: "h27_30", type: ROAD}, {from: "h27_30", to: "h27_29", type: ROAD}, {from: "h27_29", to: "h28_28", type: ROAD}, {from: "h28_28", to: "h28_27", type: ROAD}, {from: "h28_27", to: "h28_26", type: ROAD}, {from: "h28_26", to: "h28_25", type: ROAD}, {from: "h28_25", to: "h28_24", type: ROAD}, {from: "h28_24", to: "h28_23", type: ROAD}, {from: "h28_23", to: "h28_22", type: ROAD}, {from: "h28_22", to: "h28_21", type: ROAD}, {from: "h28_21", to: "h28_20", type: ROAD}, {from: "h28_20", to: "h27_20", type: ROAD}, {from: "h27_20", to: "h26_19", type: ROAD}, {from: "h26_19", to: "h25_19", type: ROAD}, {from: "h25_19", to: "h24_19", type: ROAD}, {from: "h24_19", to: "h23_20", type: ROAD}, {from: "h23_20", to: "h22_19", type: ROAD}, {from: "h22_19", to: "h21_19", type: ROAD}, {from: "h21_19", to: "h20_18", type: ROAD}, {from: "h20_18", to: "h19_18", type: ROAD}, {from: "h19_18", to: "h18_18", type: ROAD}, {from: "h18_18", to: "h17_18", type: ROAD}, {from: "h17_18", to: "h16_17", type: ROAD}, {from: "h16_17", to: "h15_17", type: ROAD}, {from: "h15_17", to: "h14_16", type: ROAD}, {from: "h14_16", to: "h13_17", type: ROAD}, {from: "h13_17", to: "h12_16", type: ROAD}, {from: "h12_16", to: "h11_16", type: ROAD}, {from: "h11_16", to: "h10_15", type: ROAD}, {from: "h10_15", to: "h9_15", type: ROAD}, {from: "h9_15", to: "h8_14", type: ROAD}, {from: "h8_14", to: "h7_14", type: ROAD}, {from: "h7_14", to: "h6_13", type: ROAD}, {from: "h6_13", to: "h6_12", type: ROAD}, {from: "h6_12", to: "h5_12", type: ROAD}, {from: "h5_12", to: "h4_11", type: ROAD}, {from: "h4_11", to: "h3_11", type: ROAD}, {from: "h3_11", to: "h2_10", type: ROAD}, {from: "h2_10", to: "h1_10", type: ROAD}, {from: "h1_10", to: "h0_9", type: ROAD}, {from: "h0_10", to: "h1_10", type: ROAD}, {from: "h1_10", to: "h2_9", type: ROAD}, {from: "h2_9", to: "h2_8", type: ROAD}, {from: "h2_8", to: "h3_8", type: ROAD}, {from: "h3_8", to: "h4_8", type: ROAD}, {from: "h4_8", to: "h5_8", type: ROAD}, {from: "h5_8", to: "h6_7", type: ROAD}, {from: "h6_7", to: "h7_7", type: ROAD}, {from: "h7_7", to: "h8_6", type: ROAD}, {from: "h8_6", to: "h9_6", type: ROAD}, {from: "h9_6", to: "h10_5", type: ROAD}, {from: "h10_5", to: "h11_6", type: ROAD}, {from: "h11_6", to: "h12_5", type: ROAD}, {from: "h12_5", to: "h12_4", type: ROAD}, {from: "h12_4", to: "h13_4", type: ROAD}, {from: "h13_4", to: "h14_3", type: ROAD}, {from: "h14_3", to: "h15_3", type: ROAD}, {from: "h15_3", to: "h16_2", type: ROAD}, {from: "h16_2", to: "h17_2", type: ROAD}, {from: "h17_2", to: "h18_1", type: ROAD}, {from: "h18_1", to: "h19_1", type: ROAD}, {from: "h19_1", to: "h20_1", type: ROAD}, {from: "h20_1", to: "h21_1", type: ROAD}, {from: "h21_1", to: "h22_1", type: ROAD}, {from: "h22_1", to: "h23_1", type: ROAD}, {from: "h23_1", to: "h24_0", type: ROAD}, {from: "h24_0", to: "h25_0", type: ROAD}, {from: "h19_0", to: "h18_0", type: ROAD}, {from: "h18_0", to: "h18_1", type: ROAD}, {from: "h18_1", to: "h19_2", type: ROAD}, {from: "h19_2", to: "h19_3", type: ROAD}, {from: "h19_3", to: "h20_3", type: ROAD}, {from: "h20_3", to: "h21_4", type: ROAD}, {from: "h21_4", to: "h21_5", type: ROAD}, {from: "h21_5", to: "h22_5", type: ROAD}, {from: "h22_5", to: "h22_6", type: ROAD}, {from: "h22_6", to: "h23_7", type: ROAD}, {from: "h23_7", to: "h22_7", type: ROAD}, {from: "h22_7", to: "h22_8", type: ROAD}, {from: "h22_8", to: "h22_9", type: ROAD}, {from: "h22_9", to: "h23_10", type: ROAD}, {from: "h23_10", to: "h23_11", type: ROAD}, {from: "h23_11", to: "h24_11", type: ROAD}, {from: "h24_11", to: "h24_12", type: ROAD}, {from: "h24_12", to: "h25_13", type: ROAD}, {from: "h25_13", to: "h25_14", type: ROAD}, {from: "h25_14", to: "h25_15", type: ROAD}, {from: "h25_15", to: "h26_15", type: ROAD}, {from: "h26_15", to: "h26_16", type: ROAD}, {from: "h26_16", to: "h27_17", type: ROAD}, {from: "h8_35", to: "h8_34", type: ROAD}, {from: "h9_34", to: "h9_33", type: ROAD}, {from: "h9_33", to: "h10_32", type: ROAD}, {from: "h10_32", to: "h10_31", type: ROAD}, {from: "h10_31", to: "h11_31", type: ROAD}, {from: "h11_31", to: "h11_30", type: ROAD}, {from: "h11_30", to: "h12_29", type: ROAD}, {from: "h12_29", to: "h13_29", type: ROAD}, {from: "h13_29", to: "h14_28", type: ROAD}, {from: "h14_28", to: "h15_28", type: ROAD}, {from: "h15_28", to: "h16_28", type: ROAD}, {from: "h16_28", to: "h17_28", type: ROAD}, {from: "h17_28", to: "h18_27", type: ROAD}, {from: "h18_27", to: "h18_26", type: ROAD}, {from: "h18_26", to: "h19_26", type: ROAD}, {from: "h19_26", to: "h20_25", type: ROAD}, {from: "h20_25", to: "h21_25", type: ROAD}, {from: "h21_25", to: "h22_24", type: ROAD}, {from: "h22_24", to: "h23_24", type: ROAD}, {from: "h23_24", to: "h23_23", type: ROAD}, {from: "h23_23", to: "h24_22", type: ROAD}, {from: "h24_22", to: "h25_22", type: ROAD}, {from: "h25_22", to: "h26_21", type: ROAD}, {from: "h26_21", to: "h26_20", type: ROAD}, {from: "h26_20", to: "h27_20", type: ROAD}, {from: "h27_20", to: "h28_19", type: ROAD}, {from: "h28_19", to: "h28_18", type: ROAD}, {from: "h7_33", to: "h7_32", type: ROAD}, {from: "h7_32", to: "h6_31", type: ROAD}, {from: "h6_31", to: "h6_30", type: ROAD}, {from: "h6_30", to: "h5_30", type: ROAD}, {from: "h5_30", to: "h5_29", type: ROAD}, {from: "h5_29", to: "h4_28", type: ROAD}, {from: "h4_28", to: "h3_28", type: ROAD}, {from: "h3_28", to: "h2_28", type: ROAD}, {from: "h2_28", to: "h1_28", type: ROAD}, {from: "h1_28", to: "h0_27", type: ROAD}, {from: "h9_34", to: "h8_34", type: ROAD}, {from: "h8_34", to: "h8_33", type: ROAD}, {from: "h8_33", to: "h7_33", type: ROAD}, {from: "h27_17", to: "h28_17", type: ROAD}, {from: "h28_17", to: "h28_18", type: ROAD}, {from: "h28_17", to: "h29_17", type: ROAD}, {from: "h27_17", to: "h28_17", type: "interrupted"}, {from: "h28_17", to: "h29_17", type: "interrupted"}, {from: "h54_10", to: "h53_11", type: TRAIL}, {from: "h53_11", to: "h53_12", type: TRAIL}, {from: "h53_12", to: "h53_13", type: TRAIL}, {from: "h53_13", to: "h52_13", type: TRAIL}, {from: "h52_13", to: "h52_14", type: TRAIL}, {from: "h52_14", to: "h52_15", type: TRAIL}, {from: "h54_16", to: "h53_16", type: TRAIL}, {from: "h53_16", to: "h52_15", type: TRAIL}, {from: "h52_15", to: "h51_15", type: TRAIL}, {from: "h51_15", to: "h50_15", type: TRAIL}, {from: "h50_15", to: "h49_16", type: TRAIL}, {from: "h49_16", to: "h50_16", type: TRAIL}, {from: "h50_16", to: "h49_17", type: TRAIL}, {from: "h49_17", to: "h48_17", type: TRAIL}, {from: "h48_17", to: "h47_17", type: TRAIL}, {from: "h47_17", to: "h46_17", type: TRAIL}, {from: "h46_17", to: "h45_18", type: TRAIL}, {from: "h45_18", to: "h44_18", type: TRAIL}, {from: "h44_18", to: "h44_19", type: TRAIL}, {from: "h44_19", to: "h43_20", type: TRAIL}, {from: "h43_20", to: "h42_20", type: TRAIL}, {from: "h42_20", to: "h41_21", type: TRAIL}, {from: "h41_21", to: "h40_21", type: TRAIL}, {from: "h40_21", to: "h39_22", type: TRAIL}, {from: "h39_22", to: "h39_23", type: TRAIL}, {from: "h39_23", to: "h39_24", type: TRAIL}, {from: "h42_23", to: "h42_22", type: TRAIL}, {from: "h42_22", to: "h41_22", type: TRAIL}, {from: "h41_22", to: "h40_22", type: TRAIL}, {from: "h40_22", to: "h40_21", type: TRAIL}, {from: "h51_15", to: "h51_14", type: TRAIL}, {from: "h51_14", to: "h50_13", type: TRAIL}, {from: "h47_23", to: "h47_24", type: TRAIL}, {from: "h47_24", to: "h46_23", type: TRAIL}, {from: "h46_23", to: "h46_22", type: TRAIL}, {from: "h45_21", to: "h45_20", type: TRAIL}, {from: "h45_20", to: "h44_19", type: TRAIL}, {from: "h44_18", to: "h43_18", type: TRAIL}, {from: "h43_18", to: "h42_17", type: TRAIL}, {from: "h42_17", to: "h41_18", type: TRAIL}, {from: "h41_18", to: "h40_17", type: TRAIL}, {from: "h40_17", to: "h40_16", type: TRAIL}, {from: "h40_16", to: "h39_16", type: TRAIL}, {from: "h39_16", to: "h38_16", type: TRAIL}, {from: "h38_16", to: "h38_15", type: TRAIL}, {from: "h38_15", to: "h37_16", type: TRAIL}, {from: "h37_16", to: "h36_15", type: TRAIL}, {from: "h36_15", to: "h35_15", type: TRAIL}, {from: "h35_15", to: "h35_14", type: TRAIL}, {from: "h35_14", to: "h34_13", type: TRAIL}, {from: "h34_13", to: "h34_12", type: TRAIL}, {from: "h34_12", to: "h34_11", type: TRAIL}, {from: "h34_11", to: "h33_11", type: TRAIL}, {from: "h33_11", to: "h33_10", type: TRAIL}, {from: "h33_10", to: "h33_9", type: TRAIL}, {from: "h33_9", to: "h32_8", type: TRAIL}, {from: "h32_8", to: "h31_8", type: TRAIL}, {from: "h31_8", to: "h30_7", type: TRAIL}, {from: "h30_7", to: "h29_7", type: TRAIL}, {from: "h29_7", to: "h28_7", type: TRAIL}, {from: "h28_7", to: "h27_8", type: TRAIL}, {from: "h27_8", to: "h26_7", type: TRAIL}, {from: "h26_7", to: "h25_7", type: TRAIL}, {from: "h25_7", to: "h24_7", type: TRAIL}, {from: "h24_7", to: "h23_8", type: TRAIL}, {from: "h23_8", to: "h22_7", type: TRAIL}, {from: "h33_11", to: "h33_10", type: "interrupted"}, {from: "h33_10", to: "h32_10", type: "interrupted"}, {from: "h53_0", to: "h53_1", type: RAIL}, {from: "h53_1", to: "h52_1", type: RAIL}, {from: "h52_1", to: "h51_2", type: RAIL}, {from: "h51_2", to: "h50_2", type: RAIL}, {from: "h50_2", to: "h49_3", type: RAIL}, {from: "h49_3", to: "h48_3", type: RAIL}, {from: "h48_3", to: "h47_3", type: RAIL}, {from: "h47_3", to: "h46_3", type: RAIL}, {from: "h46_3", to: "h45_4", type: RAIL}, {from: "h45_4", to: "h45_5", type: RAIL}, {from: "h45_5", to: "h44_5", type: RAIL}, {from: "h44_5", to: "h43_6", type: RAIL}, {from: "h43_6", to: "h42_6", type: RAIL}, {from: "h42_6", to: "h41_7", type: RAIL}, {from: "h41_7", to: "h40_7", type: RAIL}, {from: "h40_7", to: "h39_8", type: RAIL}, {from: "h39_8", to: "h39_9", type: RAIL}, {from: "h39_9", to: "h38_9", type: RAIL}, {from: "h38_9", to: "h38_10", type: RAIL}, {from: "h38_10", to: "h37_11", type: RAIL}, {from: "h37_11", to: "h36_11", type: RAIL}, {from: "h36_11", to: "h36_12", type: RAIL}, {from: "h36_12", to: "h35_13", type: RAIL}, {from: "h35_13", to: "h34_13", type: RAIL}, {from: "h34_13", to: "h33_14", type: RAIL}, {from: "h33_14", to: "h32_14", type: RAIL}, {from: "h32_14", to: "h32_15", type: RAIL}, {from: "h32_15", to: "h31_16", type: RAIL}, {from: "h31_16", to: "h31_17", type: RAIL}, {from: "h31_17", to: "h30_17", type: RAIL}, {from: "h30_17", to: "h29_18", type: RAIL}, {from: "h29_18", to: "h28_18", type: RAIL}, {from: "h28_18", to: "h27_19", type: RAIL}, {from: "h27_19", to: "h27_20", type: RAIL}, {from: "h27_20", to: "h26_20", type: RAIL}, {from: "h26_20", to: "h25_21", type: RAIL}, {from: "h25_21", to: "h24_21", type: RAIL}, {from: "h24_21", to: "h24_22", type: RAIL}, {from: "h24_22", to: "h23_23", type: RAIL}, {from: "h23_23", to: "h22_23", type: RAIL}, {from: "h22_23", to: "h21_24", type: RAIL}, {from: "h21_24", to: "h20_24", type: RAIL}, {from: "h20_24", to: "h19_25", type: RAIL}, {from: "h19_25", to: "h18_25", type: RAIL}, {from: "h18_25", to: "h17_26", type: RAIL}, {from: "h17_26", to: "h17_27", type: RAIL}, {from: "h45_21", to: "h46_21", type: TRAIL}, {from: "h46_21", to: "h46_22", type: TRAIL}, {from: "h17_27", to: "h16_27", type: RAIL}, {from: "h16_27", to: "h15_28", type: RAIL}, {from: "h15_28", to: "h14_28", type: RAIL}, {from: "h14_28", to: "h13_29", type: RAIL}, {from: "h13_29", to: "h12_29", type: RAIL}, {from: "h12_29", to: "h11_30", type: RAIL}, {from: "h11_30", to: "h10_30", type: RAIL}, {from: "h10_30", to: "h10_31", type: RAIL}, {from: "h10_31", to: "h9_32", type: RAIL}, {from: "h9_32", to: "h8_32", type: RAIL}, {from: "h8_32", to: "h7_33", type: RAIL}, {from: "h7_33", to: "h6_33", type: RAIL}, {from: "h6_33", to: "h5_34", type: RAIL}, {from: "h5_34", to: "h4_34", type: RAIL}, {from: "h4_34", to: "h3_35", type: RAIL}, {from: "h54_7", to: "h53_7", type: TRAIL}, {from: "h53_7", to: "h52_7", type: TRAIL}, {from: "h52_7", to: "h51_7", type: TRAIL}, {from: "h51_7", to: "h50_7", type: TRAIL}, {from: "h50_7", to: "h50_8", type: TRAIL}, {from: "h50_8", to: "h49_9", type: TRAIL}, {from: "h49_9", to: "h48_9", type: TRAIL}, {from: "h48_9", to: "h48_10", type: TRAIL}, {from: "h48_10", to: "h48_11", type: TRAIL}, {from: "h48_11", to: "h48_12", type: TRAIL}, {from: "h48_12", to: "h48_13", type: TRAIL}, {from: "h36_4", to: "h37_5", type: TRAIL}, {from: "h37_5", to: "h38_5", type: TRAIL}, {from: "h38_5", to: "h38_6", type: TRAIL}, {from: "h38_6", to: "h39_7", type: TRAIL}, {from: "h39_7", to: "h40_7", type: TRAIL}, {from: "h40_7", to: "h41_8", type: TRAIL}, {from: "h41_8", to: "h41_9", type: TRAIL}, {from: "h41_9", to: "h42_9", type: TRAIL}, {from: "h42_9", to: "h42_10", type: TRAIL}, {from: "h42_10", to: "h43_11", type: TRAIL}, {from: "h43_11", to: "h43_12", type: TRAIL}, {from: "h43_12", to: "h42_12", type: TRAIL}, {from: "h42_12", to: "h43_13", type: TRAIL}, {from: "h43_13", to: "h43_14", type: TRAIL}, {from: "h42_9", to: "h43_9", type: TRAIL}, {from: "h43_9", to: "h43_8", type: TRAIL}, {from: "h43_8", to: "h43_7", type: TRAIL}, {from: "h43_7", to: "h43_6", type: TRAIL}, {from: "h43_6", to: "h44_5", type: TRAIL}, {from: "h44_5", to: "h44_4", type: TRAIL}, {from: "h44_4", to: "h44_3", type: TRAIL}, {from: "h44_3", to: "h44_2", type: TRAIL}, {from: "h44_2", to: "h43_2", type: TRAIL}, {from: "h43_2", to: "h43_1", type: TRAIL}, {from: "h43_1", to: "h42_0", type: TRAIL}, {from: "h42_0", to: "h41_0", type: TRAIL}, {from: "h41_0", to: "h40_0", type: TRAIL}, {from: "h47_0", to: "h47_1", type: TRAIL}, {from: "h47_1", to: "h47_2", type: TRAIL}, {from: "h47_2", to: "h47_3", type: TRAIL}, {from: "h47_3", to: "h46_3", type: TRAIL}, {from: "h46_3", to: "h45_4", type: TRAIL}, {from: "h45_4", to: "h44_4", type: TRAIL}, {from: "h42_12", to: "h41_13", type: TRAIL}, {from: "h41_13", to: "h40_13", type: TRAIL}, {from: "h40_13", to: "h39_14", type: TRAIL}, {from: "h39_14", to: "h38_13", type: TRAIL}, {from: "h38_13", to: "h37_14", type: TRAIL}, {from: "h37_14", to: "h36_14", type: TRAIL}, {from: "h36_14", to: "h35_15", type: TRAIL}, {from: "h35_15", to: "h34_15", type: TRAIL}, {from: "h34_15", to: "h33_16", type: TRAIL}, {from: "h33_16", to: "h32_16", type: TRAIL}, {from: "h32_16", to: "h31_17", type: TRAIL}, {from: "h31_17", to: "h30_17", type: TRAIL}, {from: "h35_15", to: "h35_16", type: TRAIL}, {from: "h35_16", to: "h36_16", type: TRAIL}, {from: "h36_16", to: "h36_17", type: TRAIL}, {from: "h36_17", to: "h36_18", type: TRAIL}, {from: "h36_18", to: "h35_19", type: TRAIL}, {from: "h35_19", to: "h34_19", type: TRAIL}, {from: "h34_19", to: "h34_20", type: TRAIL}, {from: "h34_20", to: "h33_21", type: TRAIL}, {from: "h33_21", to: "h33_22", type: TRAIL}, {from: "h33_22", to: "h33_23", type: TRAIL}, {from: "h33_23", to: "h33_24", type: TRAIL}, {from: "h33_24", to: "h33_25", type: TRAIL}, {from: "h34_19", to: "h33_19", type: TRAIL}, {from: "h33_19", to: "h32_19", type: TRAIL}, {from: "h32_19", to: "h31_19", type: TRAIL}, {from: "h31_19", to: "h30_18", type: TRAIL}, {from: "h30_18", to: "h29_19", type: TRAIL}, {from: "h29_19", to: "h28_18", type: TRAIL}, {from: "h33_19", to: "h33_20", type: TRAIL}, {from: "h33_20", to: "h34_20", type: TRAIL}, {from: "h29_19", to: "h29_20", type: TRAIL}, {from: "h28_19", to: "h29_20", type: TRAIL}, {from: "h29_20", to: "h30_20", type: TRAIL}, {from: "h30_20", to: "h31_21", type: TRAIL}, {from: "h31_21", to: "h31_22", type: TRAIL}, {from: "h31_22", to: "h32_22", type: TRAIL}, {from: "h32_22", to: "h33_23", type: TRAIL}, {from: "h33_23", to: "h34_23", type: TRAIL}, {from: "h34_23", to: "h35_24", type: TRAIL}, {from: "h39_22", to: "h38_21", type: TRAIL}, {from: "h38_21", to: "h37_21", type: TRAIL}, {from: "h37_21", to: "h36_20", type: TRAIL}, {from: "h36_20", to: "h35_21", type: TRAIL}, {from: "h35_21", to: "h34_20", type: TRAIL}, {from: "h32_0", to: "h32_1", type: TRAIL}, {from: "h32_1", to: "h33_2", type: TRAIL}, {from: "h33_2", to: "h34_1", type: TRAIL}, {from: "h34_1", to: "h34_2", type: TRAIL}, {from: "h34_2", to: "h34_3", type: TRAIL}, {from: "h34_3", to: "h34_4", type: TRAIL}, {from: "h34_4", to: "h35_5", type: TRAIL}, {from: "h34_4", to: "h33_5", type: TRAIL}, {from: "h33_5", to: "h33_6", type: TRAIL}, {from: "h33_6", to: "h32_6", type: TRAIL}, {from: "h32_6", to: "h32_7", type: TRAIL}, {from: "h32_7", to: "h32_8", type: TRAIL}, {from: "h32_8", to: "h31_9", type: TRAIL}, {from: "h31_9", to: "h31_10", type: TRAIL}, {from: "h31_10", to: "h31_11", type: TRAIL}, {from: "h31_11", to: "h31_12", type: TRAIL}, {from: "h33_5", to: "h32_5", type: TRAIL}, {from: "h32_5", to: "h31_6", type: TRAIL}, {from: "h31_6", to: "h30_6", type: TRAIL}, {from: "h30_6", to: "h30_7", type: TRAIL}, {from: "h23_7", to: "h24_6", type: TRAIL}, {from: "h24_6", to: "h24_5", type: TRAIL}, {from: "h24_5", to: "h24_4", type: TRAIL}, {from: "h24_4", to: "h25_4", type: TRAIL}, {from: "h25_4", to: "h25_3", type: TRAIL}, {from: "h25_3", to: "h25_2", type: TRAIL}, {from: "h25_2", to: "h25_1", type: TRAIL}, {from: "h25_1", to: "h25_0", type: TRAIL}, {from: "h48_32", to: "h47_33", type: TRAIL}, {from: "h47_33", to: "h46_32", type: TRAIL}, {from: "h46_32", to: "h45_33", type: TRAIL}, {from: "h45_33", to: "h44_32", type: TRAIL}, {from: "h44_32", to: "h43_32", type: TRAIL}, {from: "h43_32", to: "h42_31", type: TRAIL}, {from: "h42_31", to: "h41_31", type: TRAIL}, {from: "h41_31", to: "h41_30", type: TRAIL}, {from: "h41_30", to: "h40_29", type: TRAIL}, {from: "h43_32", to: "h43_33", type: TRAIL}, {from: "h43_35", to: "h44_34", type: TRAIL}, {from: "h43_33", to: "h43_34", type: TRAIL}, {from: "h43_34", to: "h44_34", type: TRAIL}, {from: "h48_26", to: "h47_27", type: TRAIL}, {from: "h47_27", to: "h47_28", type: TRAIL}, {from: "h47_28", to: "h46_28", type: TRAIL}, {from: "h46_28", to: "h45_28", type: TRAIL}, {from: "h45_28", to: "h45_29", type: TRAIL}, {from: "h45_29", to: "h45_30", type: TRAIL}, {from: "h45_30", to: "h44_30", type: TRAIL}, {from: "h44_30", to: "h43_31", type: TRAIL}, {from: "h43_31", to: "h42_30", type: TRAIL}, {from: "h42_30", to: "h42_31", type: TRAIL}, {from: "h42_31", to: "h41_32", type: TRAIL}, {from: "h41_32", to: "h40_32", type: TRAIL}, {from: "h40_32", to: "h39_33", type: TRAIL}, {from: "h39_33", to: "h38_33", type: TRAIL}, {from: "h38_33", to: "h37_34", type: TRAIL}, {from: "h40_32", to: "h40_33", type: TRAIL}, {from: "h40_33", to: "h40_34", type: TRAIL}, {from: "h38_33", to: "h38_34", type: TRAIL}, {from: "h38_34", to: "h39_35", type: TRAIL}, {from: "h37_34", to: "h36_34", type: TRAIL}, {from: "h36_34", to: "h36_33", type: TRAIL}, {from: "h36_33", to: "h35_33", type: TRAIL}, {from: "h35_33", to: "h35_32", type: TRAIL}, {from: "h35_32", to: "h35_31", type: TRAIL}, {from: "h35_31", to: "h34_31", type: TRAIL}, {from: "h34_31", to: "h33_31", type: TRAIL}, {from: "h33_31", to: "h33_30", type: TRAIL}, {from: "h33_30", to: "h33_29", type: TRAIL}, {from: "h33_29", to: "h32_29", type: TRAIL}, {from: "h32_29", to: "h32_28", type: TRAIL}, {from: "h32_28", to: "h31_28", type: TRAIL}, {from: "h31_28", to: "h30_27", type: TRAIL}, {from: "h30_27", to: "h30_26", type: TRAIL}, {from: "h30_26", to: "h30_25", type: TRAIL}, {from: "h30_25", to: "h29_25", type: TRAIL}, {from: "h29_25", to: "h29_24", type: TRAIL}, {from: "h29_24", to: "h29_23", type: TRAIL}, {from: "h29_23", to: "h29_22", type: TRAIL}, {from: "h29_22", to: "h28_21", type: TRAIL}, {from: "h32_28", to: "h31_29", type: TRAIL}, {from: "h31_29", to: "h30_29", type: TRAIL}, {from: "h36_33", to: "h35_34", type: TRAIL}, {from: "h35_34", to: "h34_34", type: TRAIL}, {from: "h34_34", to: "h33_35", type: TRAIL}, {from: "h33_35", to: "h32_34", type: TRAIL}, {from: "h32_34", to: "h32_33", type: TRAIL}, {from: "h32_33", to: "h31_33", type: TRAIL}, {from: "h31_33", to: "h31_32", type: TRAIL}, {from: "h31_32", to: "h31_31", type: TRAIL}, {from: "h31_31", to: "h31_30", type: TRAIL}, {from: "h31_30", to: "h30_29", type: TRAIL}, {from: "h30_29", to: "h29_29", type: TRAIL}, {from: "h29_29", to: "h28_28", type: TRAIL}, {from: "h28_28", to: "h28_27", type: TRAIL}, {from: "h28_27", to: "h27_28", type: TRAIL}, {from: "h27_28", to: "h26_27", type: TRAIL}, {from: "h26_27", to: "h26_26", type: TRAIL}, {from: "h26_26", to: "h25_26", type: TRAIL}, {from: "h25_26", to: "h24_25", type: TRAIL}, {from: "h29_29", to: "h28_28", type: "interrupted"}, {from: "h28_28", to: "h27_29", type: "interrupted"}, {from: "h31_28", to: "h30_27", type: "interrupted"}, {from: "h30_27", to: "h29_28", type: "interrupted"}, {from: "h31_33", to: "h30_33", type: TRAIL}, {from: "h30_33", to: "h29_33", type: TRAIL}, {from: "h29_33", to: "h28_32", type: TRAIL}, {from: "h28_32", to: "h27_32", type: TRAIL}, {from: "h27_32", to: "h26_31", type: TRAIL}, {from: "h26_31", to: "h25_32", type: TRAIL}, {from: "h25_32", to: "h24_31", type: TRAIL}, {from: "h24_31", to: "h24_30", type: TRAIL}, {from: "h24_30", to: "h23_31", type: TRAIL}, {from: "h23_31", to: "h23_32", type: TRAIL}, {from: "h23_32", to: "h23_33", type: TRAIL}, {from: "h23_33", to: "h22_33", type: TRAIL}, {from: "h22_33", to: "h21_33", type: TRAIL}, {from: "h21_33", to: "h20_33", type: TRAIL}, {from: "h20_33", to: "h19_34", type: TRAIL}, {from: "h19_34", to: "h18_34", type: TRAIL}, {from: "h18_34", to: "h17_35", type: TRAIL}, {from: "h15_35", to: "h16_34", type: TRAIL}, {from: "h16_34", to: "h17_34", type: TRAIL}, {from: "h17_34", to: "h18_33", type: TRAIL}, {from: "h18_33", to: "h18_32", type: TRAIL}, {from: "h18_32", to: "h19_33", type: TRAIL}, {from: "h19_33", to: "h20_33", type: TRAIL}, {from: "h20_33", to: "h20_34", type: TRAIL}, {from: "h20_34", to: "h20_35", type: TRAIL}, {from: "h18_32", to: "h19_32", type: TRAIL}, {from: "h19_32", to: "h19_31", type: TRAIL}, {from: "h19_31", to: "h20_30", type: TRAIL}, {from: "h20_30", to: "h20_29", type: TRAIL}, {from: "h20_29", to: "h21_29", type: TRAIL}, {from: "h21_29", to: "h21_28", type: TRAIL}, {from: "h21_28", to: "h22_27", type: TRAIL}, {from: "h22_27", to: "h23_27", type: TRAIL}, {from: "h23_27", to: "h23_26", type: TRAIL}, {from: "h23_26", to: "h24_25", type: TRAIL}, {from: "h24_25", to: "h24_24", type: TRAIL}, {from: "h24_24", to: "h25_24", type: TRAIL}, {from: "h25_24", to: "h25_23", type: TRAIL}, {from: "h25_23", to: "h26_22", type: TRAIL}, {from: "h26_22", to: "h27_22", type: TRAIL}, {from: "h27_22", to: "h27_21", type: TRAIL}, {from: "h27_21", to: "h27_20", type: TRAIL}, {from: "h18_32", to: "h17_32", type: TRAIL}, {from: "h17_32", to: "h18_31", type: TRAIL}, {from: "h18_31", to: "h17_31", type: TRAIL}, {from: "h17_31", to: "h16_30", type: TRAIL}, {from: "h16_30", to: "h16_29", type: TRAIL}, {from: "h16_29", to: "h15_29", type: TRAIL}, {from: "h15_29", to: "h15_28", type: TRAIL}, {from: "h15_28", to: "h14_28", type: TRAIL}, {from: "h14_28", to: "h14_27", type: TRAIL}, {from: "h14_27", to: "h13_27", type: TRAIL}, {from: "h13_27", to: "h14_26", type: TRAIL}, {from: "h14_26", to: "h15_26", type: TRAIL}, {from: "h15_26", to: "h16_25", type: TRAIL}, {from: "h16_25", to: "h17_25", type: TRAIL}, {from: "h17_25", to: "h18_25", type: TRAIL}, {from: "h18_25", to: "h18_26", type: TRAIL}, {from: "h18_26", to: "h19_27", type: TRAIL}, {from: "h19_27", to: "h20_27", type: TRAIL}, {from: "h20_27", to: "h21_28", type: TRAIL}, {from: "h21_28", to: "h22_28", type: TRAIL}, {from: "h22_28", to: "h22_29", type: TRAIL}, {from: "h22_29", to: "h23_30", type: TRAIL}, {from: "h23_30", to: "h23_31", type: TRAIL}, {from: "h13_29", to: "h14_28", type: "interrupted"}, {from: "h14_28", to: "h14_27", type: "interrupted"}, {from: "h17_25", to: "h17_24", type: TRAIL}, {from: "h17_24", to: "h17_23", type: TRAIL}, {from: "h17_23", to: "h17_22", type: TRAIL}, {from: "h17_22", to: "h18_21", type: TRAIL}, {from: "h18_21", to: "h17_21", type: TRAIL}, {from: "h17_21", to: "h17_20", type: TRAIL}, {from: "h17_20", to: "h18_19", type: TRAIL}, {from: "h18_19", to: "h18_18", type: TRAIL}, {from: "h18_19", to: "h19_20", type: TRAIL}, {from: "h19_20", to: "h20_19", type: TRAIL}, {from: "h20_19", to: "h21_19", type: TRAIL}, {from: "h18_18", to: "h18_19", type: "interrupted"}, {from: "h18_19", to: "h19_20", type: "interrupted"}, {from: "h17_20", to: "h16_20", type: TRAIL}, {from: "h16_20", to: "h15_20", type: TRAIL}, {from: "h15_20", to: "h14_20", type: TRAIL}, {from: "h14_20", to: "h13_20", type: TRAIL}, {from: "h13_20", to: "h12_20", type: TRAIL}, {from: "h12_20", to: "h11_21", type: TRAIL}, {from: "h11_21", to: "h11_22", type: TRAIL}, {from: "h11_22", to: "h11_23", type: TRAIL}, {from: "h11_23", to: "h11_24", type: TRAIL}, {from: "h11_24", to: "h11_25", type: TRAIL}, {from: "h11_25", to: "h12_25", type: TRAIL}, {from: "h12_25", to: "h12_26", type: TRAIL}, {from: "h12_26", to: "h13_27", type: TRAIL}, {from: "h13_27", to: "h12_27", type: TRAIL}, {from: "h12_27", to: "h11_27", type: TRAIL}, {from: "h11_27", to: "h10_27", type: TRAIL}, {from: "h10_27", to: "h9_28", type: TRAIL}, {from: "h9_28", to: "h10_28", type: TRAIL}, {from: "h10_28", to: "h10_29", type: TRAIL}, {from: "h10_29", to: "h11_30", type: TRAIL}, {from: "h11_22", to: "h10_22", type: TRAIL}, {from: "h10_22", to: "h9_22", type: TRAIL}, {from: "h9_22", to: "h8_22", type: TRAIL}, {from: "h8_22", to: "h7_23", type: TRAIL}, {from: "h7_23", to: "h6_23", type: TRAIL}, {from: "h6_23", to: "h6_24", type: TRAIL}, {from: "h6_24", to: "h5_25", type: TRAIL}, {from: "h5_25", to: "h5_26", type: TRAIL}, {from: "h5_26", to: "h5_27", type: TRAIL}, {from: "h5_27", to: "h5_28", type: TRAIL}, {from: "h5_28", to: "h5_29", type: TRAIL}, {from: "h9_28", to: "h8_28", type: TRAIL}, {from: "h8_28", to: "h7_28", type: TRAIL}, {from: "h7_28", to: "h7_29", type: TRAIL}, {from: "h7_29", to: "h6_29", type: TRAIL}, {from: "h6_29", to: "h5_30", type: TRAIL}, {from: "h5_30", to: "h4_30", type: TRAIL}, {from: "h4_30", to: "h4_31", type: TRAIL}, {from: "h4_31", to: "h3_32", type: TRAIL}, {from: "h3_32", to: "h2_32", type: TRAIL}, {from: "h2_32", to: "h2_33", type: TRAIL}, {from: "h2_33", to: "h1_34", type: TRAIL}, {from: "h1_34", to: "h1_35", type: TRAIL}, {from: "h4_28", to: "h4_27", type: TRAIL}, {from: "h4_27", to: "h3_27", type: TRAIL}, {from: "h3_27", to: "h3_26", type: TRAIL}, {from: "h3_26", to: "h2_25", type: TRAIL}, {from: "h2_25", to: "h1_25", type: TRAIL}, {from: "h1_25", to: "h1_24", type: TRAIL}, {from: "h1_24", to: "h0_23", type: TRAIL}, {from: "h0_17", to: "h1_18", type: TRAIL}, {from: "h1_18", to: "h2_17", type: TRAIL}, {from: "h2_17", to: "h3_18", type: TRAIL}, {from: "h3_18", to: "h4_17", type: TRAIL}, {from: "h4_17", to: "h5_17", type: TRAIL}, {from: "h5_17", to: "h6_16", type: TRAIL}, {from: "h6_16", to: "h7_16", type: TRAIL}, {from: "h7_16", to: "h8_15", type: TRAIL}, {from: "h8_15", to: "h9_15", type: TRAIL}, {from: "h11_27", to: "h10_26", type: TRAIL}, {from: "h10_26", to: "h9_26", type: TRAIL}, {from: "h9_26", to: "h8_25", type: TRAIL}, {from: "h8_25", to: "h7_25", type: TRAIL}, {from: "h7_25", to: "h7_24", type: TRAIL}, {from: "h7_24", to: "h6_23", type: TRAIL}, {from: "h6_23", to: "h5_23", type: TRAIL}, {from: "h5_23", to: "h5_22", type: TRAIL}, {from: "h5_22", to: "h4_22", type: TRAIL}, {from: "h4_22", to: "h4_21", type: TRAIL}, {from: "h4_21", to: "h3_21", type: TRAIL}, {from: "h3_21", to: "h3_20", type: TRAIL}, {from: "h3_20", to: "h3_19", type: TRAIL}, {from: "h3_19", to: "h2_18", type: TRAIL}, {from: "h2_18", to: "h2_17", type: TRAIL}, {from: "h2_17", to: "h2_16", type: TRAIL}, {from: "h2_16", to: "h2_15", type: TRAIL}, {from: "h2_15", to: "h1_15", type: TRAIL}, {from: "h1_15", to: "h0_14", type: TRAIL}, {from: "h0_14", to: "h0_13", type: TRAIL}, {from: "h4_21", to: "h5_21", type: TRAIL}, {from: "h6_16", to: "h5_16", type: TRAIL}, {from: "h5_16", to: "h4_15", type: TRAIL}, {from: "h4_15", to: "h5_15", type: TRAIL}, {from: "h5_15", to: "h6_14", type: TRAIL}, {from: "h6_14", to: "h6_13", type: TRAIL}, {from: "h6_13", to: "h7_13", type: TRAIL}, {from: "h7_13", to: "h7_12", type: TRAIL}, {from: "h7_12", to: "h6_11", type: TRAIL}, {from: "h6_11", to: "h6_10", type: TRAIL}, {from: "h6_10", to: "h6_9", type: TRAIL}, {from: "h6_9", to: "h6_8", type: TRAIL}, {from: "h6_8", to: "h6_7", type: TRAIL}, {from: "h7_13", to: "h8_12", type: TRAIL}, {from: "h8_12", to: "h9_13", type: TRAIL}, {from: "h9_13", to: "h10_12", type: TRAIL}, {from: "h10_12", to: "h11_13", type: TRAIL}, {from: "h11_13", to: "h12_12", type: TRAIL}, {from: "h10_15", to: "h10_14", type: TRAIL}, {from: "h10_14", to: "h11_14", type: TRAIL}, {from: "h11_14", to: "h11_13", type: TRAIL}, {from: "h11_14", to: "h11_13", type: "interrupted"}, {from: "h11_13", to: "h10_12", type: "interrupted"}, {from: "h9_6", to: "h8_5", type: TRAIL}, {from: "h8_5", to: "h7_5", type: TRAIL}, {from: "h7_5", to: "h7_4", type: TRAIL}, {from: "h7_4", to: "h7_3", type: TRAIL}, {from: "h7_3", to: "h7_2", type: TRAIL}, {from: "h7_2", to: "h6_1", type: TRAIL}, {from: "h6_1", to: "h5_2", type: TRAIL}, {from: "h5_2", to: "h4_2", type: TRAIL}, {from: "h4_2", to: "h3_2", type: TRAIL}, {from: "h3_2", to: "h3_1", type: TRAIL}, {from: "h3_1", to: "h2_0", type: TRAIL}, {from: "h11_6", to: "h12_6", type: TRAIL}, {from: "h12_6", to: "h13_6", type: TRAIL}, {from: "h13_6", to: "h13_7", type: TRAIL}, {from: "h13_7", to: "h14_7", type: TRAIL}, {from: "h14_7", to: "h15_8", type: TRAIL}, {from: "h15_8", to: "h15_9", type: TRAIL}, {from: "h15_9", to: "h16_8", type: TRAIL}, {from: "h16_8", to: "h17_8", type: TRAIL}, {from: "h17_8", to: "h17_9", type: TRAIL}, {from: "h17_9", to: "h18_9", type: TRAIL}, {from: "h18_9", to: "h18_10", type: TRAIL}, {from: "h18_10", to: "h19_11", type: TRAIL}, {from: "h19_11", to: "h18_11", type: TRAIL}, {from: "h18_11", to: "h18_12", type: TRAIL}, {from: "h18_12", to: "h17_13", type: TRAIL}, {from: "h17_13", to: "h16_13", type: TRAIL}, {from: "h16_13", to: "h15_14", type: TRAIL}, {from: "h15_14", to: "h14_14", type: TRAIL}, {from: "h14_14", to: "h14_15", type: TRAIL}, {from: "h14_15", to: "h14_16", type: TRAIL}, {from: "h16_13", to: "h15_13", type: TRAIL}, {from: "h15_13", to: "h14_13", type: TRAIL}, {from: "h14_13", to: "h13_13", type: TRAIL}, {from: "h13_13", to: "h12_12", type: TRAIL}, {from: "h19_18", to: "h19_17", type: TRAIL}, {from: "h19_17", to: "h20_16", type: TRAIL}, {from: "h20_16", to: "h21_16", type: TRAIL}, {from: "h21_16", to: "h22_15", type: TRAIL}, {from: "h22_15", to: "h22_14", type: TRAIL}, {from: "h22_14", to: "h21_14", type: TRAIL}, {from: "h21_14", to: "h21_13", type: TRAIL}, {from: "h21_13", to: "h20_12", type: TRAIL}, {from: "h20_12", to: "h20_11", type: TRAIL}, {from: "h20_11", to: "h19_12", type: TRAIL}, {from: "h19_12", to: "h19_11", type: TRAIL}, {from: "h19_11", to: "h19_10", type: TRAIL}, {from: "h19_10", to: "h20_9", type: TRAIL}, {from: "h20_9", to: "h21_9", type: TRAIL}, {from: "h21_9", to: "h22_8", type: TRAIL}, {from: "h29_16", to: "h28_15", type: TRAIL}, {from: "h28_15", to: "h27_16", type: TRAIL}, {from: "h27_16", to: "h26_16", type: TRAIL}, {from: "h26_16", to: "h25_16", type: TRAIL}, {from: "h25_16", to: "h24_15", type: TRAIL}, {from: "h24_15", to: "h23_15", type: TRAIL}, {from: "h23_15", to: "h22_14", type: TRAIL}, {from: "h25_16", to: "h25_15", type: TRAIL}, {from: "h40_34", to: "h40_35", type: TRAIL}];
    return function(from, to) {
        return _.head(pathInfo.filter(function(it) {
            return it.from === from && it.to === to ||
                    it.from === to && it.to === from;
        }).map(function(it) {
            return it.type;
        }));
    };
})();