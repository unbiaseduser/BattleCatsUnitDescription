package com.sixtyninefourtwenty.bcud.ui.fragments.guidedetails.advents;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.ListItemAdventBossBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemAdventStageBinding;
import com.sixtyninefourtwenty.bcud.objects.AdventBoss;
import com.sixtyninefourtwenty.bcud.objects.AdventStage;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseDialogFragment;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;
import com.sixtyninefourtwenty.stuff.Dimensions;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class GuideDetailsAdventTabListFragment extends BaseFragment<@NonNull RecyclerView> {

    @Override
    protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new RecyclerView(requireContext());
    }

    @Override
    protected void setup(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setHasFixedSize(true);
        view.setAdapter(new AdventBossListAdapter(MyApplication.get(requireContext()).getAdventData().getBosses(),
                boss -> navigate(GuideDetailsAdventFragmentDirections.showSelectAdventStageDialog(boss)),
                boss -> new MaterialAlertDialogBuilder(requireContext())
                        .setMessage(boss.getInfo())
                        .setTitle(R.string.advent_boss_info)
                        .setPositiveButton(getString(R.string.got_it), null)
                        .setNeutralButton(R.string.view_on_wiki, (d, w) -> openWebsite(boss.getWikiUrl()))
                        .show()));
    }

    public static final class SelectAdventStageDialog extends BaseDialogFragment<@NonNull RecyclerView> {

        @Override
        protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater) {
            return new RecyclerView(requireContext());
        }

        @NonNull
        @Override
        protected Dialog initDialog(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
            final var boss = GuideDetailsAdventTabListFragment$SelectAdventStageDialogArgs.fromBundle(getArguments()).getBoss();
            view.setPadding(0, Dimensions.pxToDpAsInt(requireContext(), 16), 0, 0);
            view.setOverScrollMode(View.OVER_SCROLL_NEVER);
            view.setLayoutManager(new LinearLayoutManager(requireContext()));
            view.setHasFixedSize(true);
            view.setAdapter(new AdventStageAdapter(boss.getStages(), stage -> getNavController().navigate(GuideDetailsAdventTabListFragment$SelectAdventStageDialogDirections.goToAdventDetails(stage))));
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(view)
                    .setTitle(R.string.stages)
                    .create();
        }

        private static final class AdventStageAdapter extends ListAdapter<AdventStage, AdventStageAdapter.AdventStageViewHolder> {

            private final Consumer<AdventStage> onStageClick;

            private static final DiffUtil.ItemCallback<AdventStage> STAGE_DIFFER = new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull AdventStage oldItem, @NonNull AdventStage newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull AdventStage oldItem, @NonNull AdventStage newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }
            };

            public AdventStageAdapter(List<AdventStage> stages, Consumer<AdventStage> onStageClick) {
                super(STAGE_DIFFER);
                submitList(stages);
                this.onStageClick = onStageClick;
            }

            @NonNull
            @Override
            public AdventStageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final var binding = ListItemAdventStageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AdventStageViewHolder(binding, pos -> onStageClick.accept(getItem(pos)));
            }

            @Override
            public void onBindViewHolder(@NonNull AdventStageViewHolder holder, int position) {
                final var stage = getItem(position);
                AssetImageLoading.loadAssetImage(holder.binding.stageImg, stage.getImg());
                holder.binding.stageName.setText(stage.getName());
            }

            private static final class AdventStageViewHolder extends RecyclerView.ViewHolder {
                private final ListItemAdventStageBinding binding;

                public AdventStageViewHolder(ListItemAdventStageBinding binding, IntConsumer onClick) {
                    super(binding.getRoot());
                    this.binding = binding;
                    itemView.setOnClickListener(v -> onClick.accept(getAbsoluteAdapterPosition()));
                }
            }
        }
    }

    private static final class AdventBossListAdapter extends ListAdapter<AdventBoss, AdventBossListAdapter.AdventBossViewHolder> {

        private final Consumer<AdventBoss> onBossClick;
        private final Consumer<AdventBoss> onInfoClick;

        private static final DiffUtil.ItemCallback<AdventBoss> BOSS_DIFFER = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull AdventBoss oldItem, @NonNull AdventBoss newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull AdventBoss oldItem, @NonNull AdventBoss newItem) {
                return oldItem.equals(newItem);
            }
        };

        public AdventBossListAdapter(List<AdventBoss> bosses, Consumer<AdventBoss> onBossClick, Consumer<AdventBoss> onInfoClick) {
            super(BOSS_DIFFER);
            submitList(bosses);
            this.onBossClick = onBossClick;
            this.onInfoClick = onInfoClick;
        }

        @NonNull
        @Override
        public AdventBossViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemAdventBossBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new AdventBossViewHolder(binding,
                    pos -> onBossClick.accept(getItem(pos)),
                    pos -> onInfoClick.accept(getItem(pos)));
        }

        @Override
        public void onBindViewHolder(@NonNull AdventBossViewHolder holder, int position) {
            final var boss = getItem(position);
            AssetImageLoading.loadAssetImage(holder.binding.bossIcon, boss.getIconPath());
            holder.binding.bossName.setText(boss.getName());
        }

        private static final class AdventBossViewHolder extends RecyclerView.ViewHolder {
            private final ListItemAdventBossBinding binding;

            public AdventBossViewHolder(ListItemAdventBossBinding binding, IntConsumer onBossClickListener,
                                        IntConsumer onInfoClickListener) {
                super(binding.getRoot());
                this.binding = binding;
                itemView.setOnClickListener(v -> onBossClickListener.accept(getAbsoluteAdapterPosition()));
                binding.bossInfo.setOnClickListener(v -> onInfoClickListener.accept(getAbsoluteAdapterPosition()));
            }
        }
    }
}
