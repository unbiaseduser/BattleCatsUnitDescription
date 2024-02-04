package com.sixtyninefourtwenty.bcud.adapters;

import static java.util.Objects.requireNonNullElseGet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.ListItemTfMaterialBinding;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialSupplier;
import com.sixtyninefourtwenty.common.utils.Formatting;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

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

    private final BiConsumer<View, TFMaterialData> onIconClickListener;
    @Nullable
    private final TFMaterialSupplier tfMaterialSupplier;

    public TFMaterialAdapter(
            BiConsumer<View, TFMaterialData> onIconClickListener,
            @Nullable TFMaterialSupplier tfMaterialSupplier
    ) {
        super(MATERIAL_DIFFER);
        this.onIconClickListener = onIconClickListener;
        this.tfMaterialSupplier = tfMaterialSupplier;
    }

    public TFMaterialAdapter(BiConsumer<View, TFMaterialData> onIconClickListener) {
        this(onIconClickListener, null);
    }

    public TFMaterialAdapter(
            List<TFMaterialData> list,
            BiConsumer<View, TFMaterialData> onIconClickListener,
            @Nullable TFMaterialSupplier tfMaterialSupplier
    ) {
        this(onIconClickListener, tfMaterialSupplier);
        submitList(list);
    }

    public TFMaterialAdapter(
            List<TFMaterialData> list,
            BiConsumer<View, TFMaterialData> onIconClickListener
    ) {
        this(list, onIconClickListener, null);
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
        final var context = holder.binding.getRoot().getContext();
        final var materialData = requireNonNullElseGet(
                tfMaterialSupplier,
                () -> MyApplication.get(context).getMaterialData()
        );
        AssetImageLoading.loadAssetImage(holder.binding.icon, mat.getMaterial(materialData).getPathToIcon());
        holder.binding.number.setText(Formatting.formatANumber(context, mat.getQuantity()));
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemTfMaterialBinding binding;

        public ViewHolder(ListItemTfMaterialBinding binding, ObjIntConsumer<View> onIconClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.icon.setOnClickListener(v -> onIconClickListener.accept(v, getAbsoluteAdapterPosition()));
        }
    }
}
