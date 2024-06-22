interface Position {
}


interface TCSUnit<Desc extends TCSUnitDesc> {
    state: TCSUnitState;
    desc: Readonly<Desc>;
}

interface TCSUnitState {
    mode: 'fire'|'move';
    steps: number;
    position: Position;
}

type TCSUnitDesc = {
    unitID: string;
    steps: number;
    fire:{
        strength:number;
        type: 'point'|'area';
        range: string;
        trajectory: 'low'|'high';
    };
    move: {
        foot: number;
        wheeled: number;
        tracked: number;
    }
}

interface InfantryUnitDesc extends TCSUnitDesc {
    morale: number;
}
interface WeaponOrVehicleUnitDesc extends TCSUnitDesc {
    category: 'carrier'|'vehicle'|'MG'|'mortar'|'IG';
    type: 'MG'|'3inch'
    defenceStrength: number;
    defenceTarget: 'B-0'|'B-1';
}