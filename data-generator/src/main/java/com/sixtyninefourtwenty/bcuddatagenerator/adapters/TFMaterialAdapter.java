package com.sixtyninefourtwenty.bcuddatagenerator.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcuddatagenerator.MyApplication;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.ListItemTfMaterialBinding;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class TFMaterialAdapter extends ListAdapter<TFMaterialData, TFMaterialAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<TFMaterialData> MATERIAL_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull TFMaterialData oldItem, @NonNull TFMaterialData newItem) {
            return oldItem.getMaterialIndex() == newItem.getMaterialIndex();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TFMaterialData oldItem, @NonNull TFMaterialData newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Nullable
    private final BiConsumer<TFMaterialData, TFMaterialAdapter> onDeleteClick;
    @Nullable
    private final BiConsumer<TFMaterialData, TFMaterialAdapter> onEditClick;
    @Nullable
    private final Consumer<RecyclerView.ViewHolder> onDrag;

    public TFMaterialAdapter(@Nullable BiConsumer<TFMaterialData, TFMaterialAdapter> onDeleteClick,
                             @Nullable BiConsumer<TFMaterialData, TFMaterialAdapter> onEditClick,
                             @Nullable Consumer<RecyclerView.ViewHolder> onDrag) {
        super(MATERIAL_DIFFER);
        this.onDeleteClick = onDeleteClick;
        this.onEditClick = onEditClick;
        this.onDrag = onDrag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemTfMaterialBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding,
                onDeleteClick != null ? pos -> onDeleteClick.accept(getItem(pos), this) : null,
                onEditClick != null ? pos -> onEditClick.accept(getItem(pos), this) : null,
                onDrag);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final var item = getItem(position);
        final var app = MyApplication.get(holder.binding.getRoot().getContext());
        holder.binding.name.setText(item.getMaterial(app.getMaterialData()).getInfo(app.getMaterialInfo()).getName());
        holder.binding.quantity.setText(holder.binding.getRoot().getContext().getString(R.string.quantity_num, item.getQuantity()));
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemTfMaterialBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(ListItemTfMaterialBinding binding,
                          @Nullable IntConsumer onDeleteClick,
                          @Nullable IntConsumer onEditClick,
                          @Nullable Consumer<RecyclerView.ViewHolder> onDrag) {
            super(binding.getRoot());
            this.binding = binding;
            if (onDeleteClick != null) {
                binding.deleteButton.setOnClickListener(v -> onDeleteClick.accept(getAdapterPosition()));
            } else {
                binding.deleteButton.setVisibility(View.GONE);
            }
            if (onEditClick != null) {
                binding.editButton.setOnClickListener(v -> onEditClick.accept(getAdapterPosition()));
            } else {
                binding.editButton.setVisibility(View.GONE);
            }
            if (onDrag != null) {
                binding.dragIndicator.setOnTouchListener((v, event) -> {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        onDrag.accept(this);
                    }
                    return false;
                });
            } else {
                binding.dragIndicator.setVisibility(View.GONE);
            }
        }
    }
}
