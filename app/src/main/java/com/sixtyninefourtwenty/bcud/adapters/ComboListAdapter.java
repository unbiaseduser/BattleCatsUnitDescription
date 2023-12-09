package com.sixtyninefourtwenty.bcud.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.ListItemComboBinding;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.common.utils.UnitIconRatioImageView;
import com.sixtyninefourtwenty.stuff.Dimensions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import kotlin.Pair;

public final class ComboListAdapter extends ListAdapter<Combo, ComboListAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Combo> COMBO_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Combo oldItem, @NonNull Combo newItem) {
            return oldItem.getIndex() == newItem.getIndex();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Combo oldItem, @NonNull Combo newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private final Consumer<Unit> onUnitInComboClickListener;

    public ComboListAdapter(Consumer<Unit> onUnitInComboClickListener) {
        super(COMBO_DIFFER);
        this.onUnitInComboClickListener = onUnitInComboClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemComboBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, viewPool);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final var combo = getItem(position);
        final var context = holder.itemView.getContext();
        holder.binding.name.setText(combo.getName(MyApplication.get(context).getComboNameData()));
        holder.binding.typeLevel.setText(combo.getType().getEffectDesc(MyApplication.get(context).getComboEffectDescData(), combo.getLevel()));

        final var existingLayoutMan = holder.binding.list.getLayoutManager();
        if (existingLayoutMan == null) {
            final var lm = new GridLayoutManager(holder.itemView.getContext(), Combo.MAX_NUM_OF_UNITS);
            lm.setInitialPrefetchItemCount(combo.getUnitsAndForms().size());
            holder.binding.list.setLayoutManager(lm);
        } else if (existingLayoutMan instanceof GridLayoutManager glm) {
            glm.setInitialPrefetchItemCount(combo.getUnitsAndForms().size());
        }

        final var existingAdapter = holder.binding.list.getAdapter();
        if (existingAdapter == null) {
            final var adapter = new UnitListComboAdapter(onUnitInComboClickListener);
            adapter.submitList(combo.getUnitsAndForms());
            holder.binding.list.setAdapter(adapter);
        } else if (existingAdapter instanceof UnitListComboAdapter ulca) {
            ulca.submitList(combo.getUnitsAndForms());
        }
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemComboBinding binding;

        public ViewHolder(ListItemComboBinding binding, RecyclerView.RecycledViewPool pool) {
            super(binding.getRoot());
            this.binding = binding;
            binding.list.setRecycledViewPool(pool);
            binding.list.setHasFixedSize(true);
        }
    }

    /**
     * Child adapter.
     */
    private static final class UnitListComboAdapter extends ListAdapter<Pair<Unit, Unit.Form>, UnitListComboAdapter.ViewHolder> {
        private final Consumer<Unit> onItemClickListener;

        private static final DiffUtil.ItemCallback<Pair<Unit, Unit.Form>> PAIR_ITEM_CALLBACK = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Pair<Unit, Unit.Form> oldItem, @NonNull Pair<Unit, Unit.Form> newItem) {
                return oldItem.getFirst().equals(newItem.getFirst());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Pair<Unit, Unit.Form> oldItem, @NonNull Pair<Unit, Unit.Form> newItem) {
                return oldItem.equals(newItem);
            }
        };

        public UnitListComboAdapter(Consumer<Unit> onItemClickListener) {
            super(PAIR_ITEM_CALLBACK);
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var iv = new UnitIconRatioImageView(parent.getContext());
            final var params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final var horizontalMarginDp = Dimensions.pxToDpAsInt(iv.getContext(), 3);
            params.setMargins(horizontalMarginDp, 0, horizontalMarginDp, 0);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new ViewHolder(iv, pos -> onItemClickListener.accept(getItem(pos).getFirst()));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var item = getItem(position);
            final var unit = item.getFirst();
            final var form = item.getSecond();
            AssetImageLoading.loadAssetImage(holder.iv, unit.getIconPathForForm(form, MyApplication.get(holder.iv.getContext()).getUnitExplanationData()));
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView iv;

            public ViewHolder(ImageView iv, IntConsumer onItemClickListener) {
                super(iv);
                this.iv = iv;
                itemView.setOnClickListener(v -> onItemClickListener.accept(getAbsoluteAdapterPosition()));
            }
        }
    }
}
