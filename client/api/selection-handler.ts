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
    onPointTo(ctx:GameCtx,counter:Counter):Operation;
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

/*

 public final void onClicked(CounterInfo counter) {
 runner.process(onSingleCounterClicked(counter));
 }

 public final void onSelect(CounterInfo counter) {
 runner.process(onSelected(counter));
 }

 public boolean canSelect(CounterInfo counter) {
 return true;
 }

 protected Operation onSelected(CounterInfo counter) {
 return null;
 }

 protected Operation onSingleCounterClicked(CounterInfo counter) {
 if (ctx.selected == null) {
 return counter.onSingleClicked(ctx);
 } else {
 return ctx.selected.onPointTo(ctx, counter);
 }
 }



 protected Operation onEmptyHexClicked(Position empty) {
 if (ctx.selected == null) {
 return ctx.board.getInfo(empty).onEmptyClicked(ctx, empty);
 } else {
 return ctx.selected.onPointToEmpty(ctx, empty);
 }
 }

 protected Operation onStackClicked(List<CounterInfo> stack, Position pos) {
 if (ctx.selected == null) {
 return ctx.board.getInfo(pos).onStackClicked(ctx, stack, pos);
 } else {
 return ctx.selected.onPointToStack(ctx, stack, pos);
 }
 }
 }
 */