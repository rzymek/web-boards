/// <reference path="../../common/model.d.ts" />
declare var svgns: string;
interface PlaceOperationData {
    image: string;
    hexid: string;
}
declare class PlaceOperation implements Operation {
    public data: PlaceOperationData;
    constructor(data: PlaceOperationData);
    public run(): void;
    public undo(): void;
}
