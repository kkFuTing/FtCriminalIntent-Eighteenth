package com.edu.bunz.ftcriminalintent.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CrimeItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mItemTouchHelperAdapter;

    public CrimeItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        mItemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

//    允许从右到左侧滑，允许上下拖动等
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

//        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;    //允许上下的拖动
//  int swipeFlags = ItemTouchHelper.LEFT;  //只允许从右向左侧滑
//  return makeMovementFlags(dragFlags,swipeFlags);

        //不允许上下拖动
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

//    当用户拖动一个Item进行上下移动从旧的位置到新的位置的时候会调用该方法
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;

    }

//    当用户左右滑动Item达到删除条件时，会调用该方法，一般手指触摸滑动的距离达到RecyclerView宽度的一半时，
//    再松开手指，此时该Item会继续向原先滑动方向滑过去并且调用onSwiped方法进行删除，否则会反向滑回原来的位置。
//    如果在onSwiped方法内我们没有进行任何操作，即不删除已经滑过去的Item，那么就会留下空白的地方，
//    因为实际上该ItemView还占据着该位置，只是移出了我们的可视范围内罢了。

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //onItemDissmiss是接口方法
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

//    从静止状态变为拖拽或者滑动的时候会回调该方法，参数actionState表示当前的状态。
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemTouchHelperViewHolder.onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

//    当用户操作完毕某个item并且其动画也结束后会调用该方法，
//    一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题。
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        ItemTouchHelperViewHolder itemTouchHelperViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemTouchHelperViewHolder.onItemClear();
    }

}
