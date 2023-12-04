import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Unit} from "../unit.tsx";

interface SelectedCounter {
    x: number,
    y: number,
    unit: Unit,
}

const initialState = {
    selected: undefined as (SelectedCounter | undefined)
} as const;

export const ui = createSlice({
    name: 'ui',
    initialState,
    reducers: {
        counterClicked: (state, action: PayloadAction<SelectedCounter>) => {
            if (state.selected) {
                state.selected = action.payload;
            } else {
                state.selected = undefined;
            }
        }
    }
})