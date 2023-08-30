package com.sixtyninefourtwenty.common.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.interfaces.BiIntConsumer;

@NonNullTypesByDefault
public final class RecyclerViews {

    private RecyclerViews() {}

    public static ItemTouchHelper createVerticalDragDropTouchHelper(boolean isLongPressDragEnabled,
                                                                    BiIntConsumer onViewHoldersMoved) {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean isLongPressDragEnabled() {
                return isLongPressDragEnabled;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                onViewHoldersMoved.accept(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
    }

    public static ItemTouchHelper createVerticalDragDropTouchHelper(BiIntConsumer onViewHoldersMoved) {
        return createVerticalDragDropTouchHelper(false, onViewHoldersMoved);
    }
}
