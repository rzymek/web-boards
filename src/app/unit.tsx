export interface Unit {
    id: string;

    fireStrength: number;
    range: number;
    steps: number;
    defenceStrength: number;

    // trajectory: 'low' | 'high';
    // type: 'area' | 'point';
    // target: 'area' | 'point' | 'both';

    mode: 'fire' | 'move';
}