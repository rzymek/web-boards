interface Position {

}

interface Operation {

}

interface OpRunner {
    process(op:Operation):void;
}

class GameCtx {
    board:Board;
    selected:Counter = null;
}

interface Hex {
    pieces():Counter[];
    onEmptyClicked(ctx:GameCtx);
    onStackClicked(ctx:GameCtx, stack:Counter[], pos:Position);
}

interface Counter {
    onSingleClicked(ctx:GameCtx):Operation;
    onPointToEmpty(ctx:GameCtx, empty:Position):Operation;
    onPointToStack(ctx:GameCtx, stack:Counter[], pos:Position);
    onPointTo(ctx:GameCtx, counter:Counter):Operation;
}

interface Board{
    hex(pos:Position):Hex;
}

class SelectionHandler {
    ctx:GameCtx;
    runner:OpRunner;

    onEmptyHexClicked(empty:Position) {
        if (this.ctx.selected == null) {
            return this.ctx.board.hex(empty).onEmptyClicked(this.ctx);
        } else {
            return this.ctx.selected.onPointToEmpty(this.ctx, empty);
        }
    }

    onPositionClicked(position:Position):Operation {
        var area = this.ctx.board.hex(position);
        var stack:Counter[] = area.pieces();
        if (stack.length == 0) {
            return this.onEmptyHexClicked(position);
        } else if (stack.length == 1) {
            return this.onSingleCounterClicked(stack[0]);
        } else {
            return this.onStackClicked(stack, position);
        }
    }

    onSingleCounterClicked(counter:Counter):Operation {
        if (this.ctx.selected == null) {
            return counter.onSingleClicked(this.ctx);
        } else {
            return this.ctx.selected.onPointTo(this.ctx, counter);
        }
    }

    onStackClicked(stack:Counter[], pos:Position):Operation {
        if (this.ctx.selected == null) {
            return this.ctx.board.hex(pos).onStackClicked(this.ctx, stack, pos);
        } else {
            return this.ctx.selected.onPointToStack(this.ctx, stack, pos);
        }
    }
}
