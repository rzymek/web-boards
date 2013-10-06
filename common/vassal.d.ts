interface VassalMod {
    Language:string;
    Chatter:string;
    Map:Maps;
}
interface Maps {
    [name:string]:VMap;
}
interface  VMap {
    name:string;
}
interface  MapX {
    nameX:string;
}

var x:VassalMod;
var z = x.Map['xx'].name