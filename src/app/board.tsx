import {equals, range} from 'remeda';
import {Counter} from "./counter.tsx";
import {Hex} from "./hex.tsx";
import {Unit} from "./unit.tsx";
import {useDispatch, useSelector} from "react-redux";
import {counterClicked, State} from "./state/store.tsx";

export function Board() {
    const zoom = 1;
    // const [mode, setMode] = useState<'move' | 'fire'>('move')
    const unit: Unit = {
        mode: 'move',
        defenceStrength: 5,
        fireStrength: 7,
        id: '2-I-26 PG',
        range: 6,
        steps: 5,
    }
    const dispatch = useDispatch();
    const selected = useSelector((state:State) => state.ui.selected);
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
                         selected={equals(selected, {x,y,unit})}
                         onClick={() => dispatch(counterClicked({x, y, unit}))}/>
            )
        )}
    </svg>
}
