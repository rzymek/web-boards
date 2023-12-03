import {CSSProperties} from "react";
import {Unit} from "./unit.tsx";
import {At} from "./at.tsx";

export function Counter(props: {
    unit: Unit;
    x: number, y: number, onClick(): void
}) {
    const labelHeight = 20;
    const h1 = 50;
    const margin = 8;
    const s1: CSSProperties = {
        stroke: 'black',
        strokeWidth: 0.5,
        fill: 'white',
        fontSize: h1,
        fontWeight: 'bold',
        textAnchor: 'start',
    };
    const outline: CSSProperties = {
        x: -70,
        y: -70,
        fill: 'lightgray',
        strokeWidth: 2,
        stroke: 'gray',
        width: 140,
        height: 140,
    };
    return <At x={props.x} y={props.y}>
        <rect style={outline}/>
        {props.unit.mode == 'move' && <polygon points="-65,60 -35,-50 -5,60 -35,25" style={{
            fill: 'red',
        }}/>}
        <rect x={-70} y={-65} width={140} height={labelHeight} style={{
            fill: 'white'
        }}/>
        <text x={0} y={-65 + labelHeight} width={140} height={labelHeight} style={{
            fill: 'black',
            textAnchor: 'middle',
            alignmentBaseline: 'ideographic',
            fontSize: labelHeight * 0.9,
            fontWeight: 'bold',
        }}>{props.unit.id}
        </text>
        <text x={-70 + margin} y={70 - margin} width={140} height={h1} style={{
            ...s1,
        }}>
            {props.unit.fireStrength}
        </text>
        <text x={-70 + margin + 30} y={70 - margin} width={140} height={h1} style={{
            ...s1,
            fontSize: 38,
        }}>
            {props.unit.range}
        </text>
        <text x={70 - margin - 30} y={70 - margin} width={140} height={h1} style={{
            ...s1,
            fontSize: 38,
            textAnchor: 'end',
            filter: 'url(#solidTextBox)',
        }}>
            {props.unit.steps}
        </text>
        <text x={70 - margin} y={70 - margin} width={140} height={h1} style={{
            ...s1,
            textAnchor: 'end',
        }}>
            {props.unit.defenceStrength}
        </text>
        <rect style={{
            ...outline,
            opacity: 0,
            cursor: 'pointer'
        }} onClick={props.onClick}/>
    </At>
}