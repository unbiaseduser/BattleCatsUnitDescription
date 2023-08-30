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
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.ListItemTalentDataBinding;
import com.sixtyninefourtwenty.common.objects.TalentData;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class TalentDataAdapter extends ListAdapter<TalentData, TalentDataAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<TalentData> TALENT_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull TalentData oldItem, @NonNull TalentData newItem) {
            return oldItem.getTalent() == newItem.getTalent();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TalentData oldItem, @NonNull TalentData newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Nullable
    private final BiConsumer<TalentData, TalentDataAdapter> onDeleteClick;
    @Nullable
    private final BiConsumer<TalentData, TalentDataAdapter> onEditClick;
    @Nullable
    private final Consumer<RecyclerView.ViewHolder> onDrag;

    public TalentDataAdapter(@Nullable BiConsumer<TalentData, TalentDataAdapter> onDeleteClick,
                             @Nullable BiConsumer<TalentData, TalentDataAdapter> onEditClick,
                             @Nullable Consumer<RecyclerView.ViewHolder> onDrag) {
        super(TALENT_DIFFER);
        this.onDeleteClick = onDeleteClick;
        this.onEditClick = onEditClick;
        this.onDrag = onDrag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final var binding = ListItemTalentDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding,
                onDeleteClick != null ? pos -> onDeleteClick.accept(getItem(pos), this) : null,
                onEditClick != null ? pos -> onEditClick.accept(getItem(pos), this) : null,
                onDrag);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final var item = getItem(position);
        final var context = holder.binding.getRoot().getContext();
        holder.binding.name.setText(item.getTalent().getInfo(MyApplication.get(context).getTalentInfo()).getAbilityName());
        holder.binding.priority.setText(item.getPriority().getText());
        holder.binding.priorityText.setText(context.getString(R.string.priority_str, context.getString(item.getPriority().getText())));
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemTalentDataBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(ListItemTalentDataBinding binding,
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
