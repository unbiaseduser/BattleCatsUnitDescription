package com.sixtyninefourtwenty.bcud.adapters;

import static com.sixtyninefourtwenty.stuff.listeners.ShortOnLongClickListener.shorten;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.databinding.ListItemReasonBinding;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.common.interfaces.TriConsumer;

import java.util.function.ObjIntConsumer;

public final class FavoriteReasonAdapter extends ListAdapter<FavoriteReason, FavoriteReasonAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<FavoriteReason> REASON_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavoriteReason oldItem, @NonNull FavoriteReason newItem) {
            return oldItem.getUid() == newItem.getUid();
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavoriteReason oldItem, @NonNull FavoriteReason newItem) {
            return oldItem.getReason().equals(newItem.getReason());
        }
    };

    private final TriConsumer<FavoriteReasonAdapter, View, FavoriteReason> onReasonLongClick;

    public FavoriteReasonAdapter(TriConsumer<FavoriteReasonAdapter, View, FavoriteReason> onReasonLongClick) {
        super(REASON_DIFFER);
        this.onReasonLongClick = onReasonLongClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemReasonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, (v, pos) -> onReasonLongClick.accept(this, v, getItem(pos)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.reason.setText(getItem(position).getReason());
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemReasonBinding binding;

        public ViewHolder(ListItemReasonBinding binding, ObjIntConsumer<View> onReasonLongClick) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnLongClickListener(shorten(v -> onReasonLongClick.accept(v, getAbsoluteAdapterPosition())));
        }
    }
}
