import {At} from "./at.tsx";

export function Hex({x, y, onClick, selected}: {
    x: number,
    y: number,
    onClick(): void,
    selected?: boolean,
}) {
    return <At x={x} y={y}>
        <circle x={0} y={0} r={2} fill="black"/>
        <polygon points="-55,-100 55,-100 110,0 55,100 -55,100 -110,0"
                 style={{
                     cursor: 'crosshair',
                     fillOpacity: 0.1,
                     fill: selected ? 'blue' : 'red',
                     stroke: 'white',
                     strokeWidth: 2,
                 }}
                 onClick={onClick}
        />
    </At>
}