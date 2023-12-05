import {createSlice, Draft, PayloadAction} from "@reduxjs/toolkit";
import {Unit} from "../unit.tsx";
import {equals} from 'remeda'

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
        counterClicked: (state: Draft<typeof initialState>, action: PayloadAction<SelectedCounter>) => {
            if(equals(state.selected, action.payload)) {
                state.selected = undefined;
            }else{
                state.selected = action.payload;
            }
        }
    }
})