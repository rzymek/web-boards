import './index.css'
import {Board} from "./app/board.tsx";
import {createRoot} from "react-dom/client";
import {Provider} from "react-redux";
import {store} from "./app/store.tsx";

const root = createRoot(document.getElementById('app')!!)
root.render(
    <Provider store={store}>
        <Board/>
    </Provider>
)
