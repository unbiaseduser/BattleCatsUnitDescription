package com.sixtyninefourtwenty.bcud.adapters;

import static java.util.Objects.requireNonNullElseGet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.ListItemUnitListBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.Utils;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

public final class UnitListAdapterList extends ListAdapter<Unit, UnitListAdapterList.UnitListViewHolderList> {

    public UnitListAdapterList(
            Consumer<Unit> onItemClickListener,
            BiConsumer<View, Unit> onButtonMenuClickListener,
            @Nullable UnitExplanationSupplier unitExplanationSupplier
    ) {
        super(Utils.UNIT_DIFFER);
        this.onItemClickListener = onItemClickListener;
        this.onButtonMenuClickListener = onButtonMenuClickListener;
        this.unitExplanationSupplier = unitExplanationSupplier;
    }

    public UnitListAdapterList(
            Consumer<Unit> onItemClickListener,
            BiConsumer<View, Unit> onButtonMenuClickListener
    ) {
        this(onItemClickListener, onButtonMenuClickListener, null);
    }

    private final Consumer<Unit> onItemClickListener;
    private final BiConsumer<View, Unit> onButtonMenuClickListener;
    @Nullable
    private final UnitExplanationSupplier unitExplanationSupplier;

    @NonNull
    @Override
    public UnitListViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemUnitListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UnitListViewHolderList(binding,
                pos -> onItemClickListener.accept(getItem(pos)),
                (view, pos) -> onButtonMenuClickListener.accept(view, getItem(pos)));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitListViewHolderList holder, int position) {
        final var unit = getItem(position);
        final var context = holder.binding.getRoot().getContext();
        final var unitExplanationData = requireNonNullElseGet(
                unitExplanationSupplier,
                () -> MyApplication.get(context).getUnitExplanationData()
        );
        AssetImageLoading.loadAssetImage(holder.binding.unitIcon, unit.getLatestFormIconPath(unitExplanationData));
        holder.binding.unitName.setText(unit.getExplanation(unitExplanationData).getFirstFormName());
    }

    public static final class UnitListViewHolderList extends RecyclerView.ViewHolder {
        private final ListItemUnitListBinding binding;

        public UnitListViewHolderList(ListItemUnitListBinding binding, IntConsumer onItemClickListener, ObjIntConsumer<View> onButtonMenuClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> onItemClickListener.accept(getAbsoluteAdapterPosition()));
            binding.menuButton.setOnClickListener(v -> onButtonMenuClickListener.accept(v, getAbsoluteAdapterPosition()));
        }
    }
}
