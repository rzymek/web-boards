import {configureStore} from "@reduxjs/toolkit";
import {ui} from "./ui.tsx";

export const {counterClicked} = ui.actions;
export const store = configureStore({
    reducer: {
        ui: ui.reducer
    }
});

export type State = ReturnType<typeof store.getState>;