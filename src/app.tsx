import {range} from 'remeda';
import {CSSProperties, ReactNode, useState} from "react";

interface Unit {
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

function At(props: { x: number, y: number, children: ReactNode }) {
    const x = props.x * (110 + 55);
    const y = props.y * 200 + (props.x % 2 ? 100 : 0);
    return <g transform={`translate(${x},${y})`}>
        {props.children}
    </g>;
}

function Counter(props: {
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

function Hex({x, y}: { x: number, y: number }) {
    return <At x={x} y={y}>
        <circle x={0} y={0} r={2} fill="black"/>
        <polygon points="-55,-100 55,-100 110,0 55,100 -55,100 -110,0"
                 style={{
                     cursor: 'crosshair',
                     fillOpacity: 0,
                     stroke: 'white',
                     strokeWidth: 2,
                 }}
        />
    </At>
}

export function App() {
    const zoom = 1;
    const [mode, setMode] = useState<'move' | 'fire'>('move')
    const unit: Unit = {
        mode,
        defenceStrength: 5,
        fireStrength: 7,
        id: '2-I-26 PG',
        range: 6,
        steps: 5,
    }
    return <svg width="100%" height="100%" style={{inset: 0, position: 'absolute'}}
                viewBox={`0 0 ${zoom * 1000} ${zoom * 1000}`}>
        <defs>
            <filter id="solidTextBox" x="0" y="0" width="1" height="1">
                <feFlood flood-color="red" result="bg"/>
                <feMerge>
                    <feMergeNode in="bg"/>
                    <feMergeNode in="SourceGraphic"/>
                </feMerge>
            </filter>
        </defs>
        {range(-1, 8).map(x =>
            range(-1, 8).map(y =>
                <Hex key={`${x}.${y}`} x={x} y={y}/>
            )
        )}
        {range(1, 3).map(x =>
            range(1, 3).map(y =>
                <Counter key={`${x}.${y}`} x={x} y={y} unit={unit}
                         onClick={() => setMode(m => m === 'move' ? 'fire' : 'move')}/>
            )
        )}
    </svg>
}
