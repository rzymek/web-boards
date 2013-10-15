/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../packages/typescript-libs/lib.d.ts" />
/// <reference path="../common/model.d.ts" />
/// <reference path="xml2js.d.ts" />
declare class BasicModule {
    private node;
    constructor(node: any);
    public PlayerRoster(): any;
    public GlobalProperties(): any;
    public Documentation(): any;
    public BasicCommandEncoder(): any;
    public DiceButton(): any;
    public PrototypesContainer(): any;
    public Chatter(): any;
    public NotesWindow(): any;
    public PieceWindow(): any;
    public ChartWindow(): any;
    public Map(f: string, v: string): any;
    public Language(): any;
    public GamePieceImageDefinitions(): any;
    public GlobalOptions(): any;
    public PredefinedSetup(): any;
}
declare function fill(str: string, o): string;
declare function shorten(p: string): string;
interface NamedXML {
    typeName: string;
    xml: any;
}
declare function keys(o: {}): any[];
declare class Foo {
    private xml;
    private types;
    private result;
    constructor(xml: any);
    public process(): string;
    public processType(tag: string, xml: any): void;
}
