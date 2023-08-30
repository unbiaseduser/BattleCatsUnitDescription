package com.sixtyninefourtwenty.bcud.adapters;

import static com.sixtyninefourtwenty.common.utils.ShortenedOnLongClickListener.shortened;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.common.utils.UnitIconRatioImageView;
import com.sixtyninefourtwenty.stuff.Dimensions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

public final class UnitListAdapterGrid extends ListAdapter<Unit, UnitListAdapterGrid.UnitListGridViewHolder> {

    public static int getGridSpans(Context context) {
        return Utils.isDeviceInLandscape(context) ? 5 : 3;
    }

    public UnitListAdapterGrid(Consumer<Unit> onItemClickListener, BiConsumer<View, Unit> onItemLongClickListener) {
        super(Utils.UNIT_DIFFER);
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private final Consumer<Unit> onItemClickListener;
    private final BiConsumer<View, Unit> onItemLongClickListener;

    @NonNull
    @Override
    public UnitListGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var iv = new UnitIconRatioImageView(parent.getContext());
        final var params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final var horizontalMarginDp = Dimensions.pxToDpAsInt(iv.getContext(), 10);
        params.setMargins(horizontalMarginDp, 0, horizontalMarginDp, 0);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new UnitListGridViewHolder(iv,
                pos -> onItemClickListener.accept(getItem(pos)),
                (view, pos) -> onItemLongClickListener.accept(view, getItem(pos)));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitListGridViewHolder holder, int position) {
        AssetImageLoading.loadAssetImage(holder.icon, getItem(position).getLatestFormIconPath(MyApplication.get(holder.icon.getContext()).getUnitExplanationData()));
    }

    static final class UnitListGridViewHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;

        public UnitListGridViewHolder(ImageView icon, IntConsumer onItemClickListener, ObjIntConsumer<View> onItemLongClickListener) {
            super(icon);
            this.icon = icon;
            itemView.setOnClickListener(v -> onItemClickListener.accept(getAbsoluteAdapterPosition()));
            itemView.setOnLongClickListener(shortened(v -> onItemLongClickListener.accept(v, getAbsoluteAdapterPosition())));
        }
    }
}
