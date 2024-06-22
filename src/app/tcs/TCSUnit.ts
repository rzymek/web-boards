interface Position{}
interface TCSUnit {
    unitID: string;
    mode: 'fire'|'move';
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

interface InfantryUnit extends TCSUnit {
    morale: number;
}
interface WeaponOrVehicleUnit extends TCSUnit {
    category:'carrier'|'vehicle'|'MG'|'mortar'|'IG';
    type:'MG'|'3inch'
    defenceStrength: number;
    defenceTarget: 'B-0'|'B-1';
}