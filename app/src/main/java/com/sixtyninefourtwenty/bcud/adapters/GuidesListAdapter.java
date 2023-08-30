package com.sixtyninefourtwenty.bcud.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.databinding.ListItemGuideBinding;
import com.sixtyninefourtwenty.bcud.objects.Guide;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.function.IntConsumer;

public final class GuidesListAdapter extends ListAdapter<Guide, GuidesListAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Guide> GUIDE_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Guide oldItem, @NonNull Guide newItem) {
            return oldItem.getUrl().equals(newItem.getUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Guide oldItem, @NonNull Guide newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final IntConsumer onItemClickListener;

    public GuidesListAdapter(List<Guide> guides, IntConsumer onItemClickListener) {
        super(GUIDE_DIFFER);
        submitList(guides);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemGuideBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final var guide = getItem(position);
        holder.binding.title.setText(guide.getTitle());
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemGuideBinding binding;

        public ViewHolder(ListItemGuideBinding binding, IntConsumer onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> onItemClickListener.accept(getAbsoluteAdapterPosition()));
        }
    }
}
