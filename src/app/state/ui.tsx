import {createSlice, Draft, PayloadAction} from "@reduxjs/toolkit";
import {Unit} from "../unit.tsx";
import {equals} from 'remeda'

interface Coords {
    x: number,
    y: number,
}

interface SelectedCounter extends Coords {
    unit: Unit,
}

interface State {
    selectedCounter?: SelectedCounter,
    selectedHex?: Coords,
}

const initialState: State = {};

export const ui = createSlice({
    name: 'ui',
    initialState,
    reducers: {
        counterClicked(state: Draft<State>, action: PayloadAction<SelectedCounter>) {
            if (equals(state.selectedCounter, action.payload)) {
                state.selectedCounter = undefined;
            } else {
                state.selectedCounter = action.payload;
            }
        },
        hexClicked(state:Draft<State>, action:PayloadAction<Coords>) {
            state.selectedHex = action.payload;
        }
    }
})