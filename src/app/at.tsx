import {ReactNode} from "react";

export function At(props: { x: number, y: number, children: ReactNode }) {
    const x = props.x * (110 + 55);
    const y = props.y * 200 + (props.x % 2 ? 100 : 0);
    return <g transform={`translate(${x},${y})`}>
        {props.children}
    </g>;
}