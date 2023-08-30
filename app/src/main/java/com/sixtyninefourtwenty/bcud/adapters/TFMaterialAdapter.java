package com.sixtyninefourtwenty.bcud.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.databinding.ListItemTfMaterialBinding;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.utils.Formatting;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

public final class TFMaterialAdapter extends ListAdapter<TFMaterialData, TFMaterialAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<TFMaterialData> MATERIAL_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull TFMaterialData oldItem, @NonNull TFMaterialData newItem) {
            return oldItem.getMaterial() == newItem.getMaterial();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TFMaterialData oldItem, @NonNull TFMaterialData newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final BiConsumer<View, TFMaterialData> onIconClickListener;

    public TFMaterialAdapter(BiConsumer<View, TFMaterialData> onIconClickListener) {
        super(MATERIAL_DIFFER);
        this.onIconClickListener = onIconClickListener;
    }

    public TFMaterialAdapter(List<TFMaterialData> list, BiConsumer<View, TFMaterialData> onIconClickListener) {
        this(onIconClickListener);
        submitList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemTfMaterialBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, (v, pos) -> onIconClickListener.accept(v, getItem(pos)));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final var mat = getItem(position);
        AssetImageLoading.loadAssetImage(holder.binding.icon, mat.getMaterial().getPathToIcon());
        holder.binding.number.setText(Formatting.formatANumber(holder.binding.getRoot().getContext(), mat.getQuantity()));
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemTfMaterialBinding binding;

        public ViewHolder(ListItemTfMaterialBinding binding, ObjIntConsumer<View> onIconClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.icon.setOnClickListener(v -> onIconClickListener.accept(v, getAbsoluteAdapterPosition()));
        }
    }
}
