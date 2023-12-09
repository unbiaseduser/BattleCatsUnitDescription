package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.talentpriority;

import static com.sixtyninefourtwenty.bcud.utils.Utils.UNIT_DIFFER;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.DialogTalentDetailsBinding;
import com.sixtyninefourtwenty.bcud.databinding.HpTpTabBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemUnitTpBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.BalloonFactory;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingDialogFragment;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.SearchViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.UnitDataViewModel;
import com.sixtyninefourtwenty.common.interfaces.TriConsumer;
import com.sixtyninefourtwenty.common.objects.Talent;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

public abstract class TalentPriorityTabFragment extends BaseViewBindingFragment<@NonNull HpTpTabBinding> {
    protected abstract Talent.UnitType getTalentPriorityType();

    @Override
    protected @NonNull HpTpTabBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return HpTpTabBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected void setup(@NonNull HpTpTabBinding binding, @Nullable Bundle savedInstanceState) {
        final var searchModel = SearchViewModel.get(requireActivity());
        final var unitModel = UnitDataViewModel.get(requireActivity());
        final var spn = binding.spinner;
        spn.setItemSelectedListener(position -> {
            switch (position) {
                case 0 -> unitModel.setTalentPriorityType(getTalentPriorityType(), Talent.Priority.TOP);
                case 1 -> unitModel.setTalentPriorityType(getTalentPriorityType(), Talent.Priority.HIGH);
                case 2 -> unitModel.setTalentPriorityType(getTalentPriorityType(), Talent.Priority.MID);
                case 3 -> unitModel.setTalentPriorityType(getTalentPriorityType(), Talent.Priority.LOW);
                case 4 -> unitModel.setTalentPriorityType(getTalentPriorityType(), Talent.Priority.DONT);
            }
        });
        spn.setItemStringArrayRes(R.array.talent_priorities);
        final var rcv = binding.list;
        rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rcv.setHasFixedSize(true);
        rcv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        final var adapter = new UnitListTPAdapter(unit -> getNavController().navigate(TalentPriorityFragmentDirections.actionNavTalentPriorityToNavUdpUnitInfo(unit)),
                (v, unit, priority) -> BalloonFactory.createWithUsualSettings(requireContext())
                        .setIconDrawableResource(R.drawable.info)
                        .setText(unit.getTalents(priority).get((int) v.getTag()).getInfo(MyApplication.get(requireContext()).getTalentInfo()).getAbilityName())
                        .setOnBalloonClickListener(b -> getNavController().navigate(TalentPriorityFragmentDirections.showTalentDetailsDialog(unit.getTalents(priority).get((int) v.getTag()))))
                        .build()
                        .showAlignBottom(v));
        rcv.setAdapter(adapter);

        searchModel.getQuery().observe(getViewLifecycleOwner(), query -> unitModel.setTalentPriorityQuery(getTalentPriorityType(), query));

        unitModel.getTalentPriorityLiveData(getTalentPriorityType()).observe(getViewLifecycleOwner(), pair ->
                adapter.updateUnits(pair._1, pair._2));
    }

    public static final class TalentDetailsDialog extends BaseViewBindingDialogFragment<DialogTalentDetailsBinding> {

        @Override
        protected @NonNull DialogTalentDetailsBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogTalentDetailsBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogTalentDetailsBinding binding, @Nullable Bundle savedInstanceState) {
            final var talent = TalentPriorityTabFragment$TalentDetailsDialogArgs.fromBundle(requireArguments()).getTalent();
            AssetImageLoading.loadAssetImage(binding.talentIcon, talent.getPathToIcon());
            binding.talentName.setText(talent.getInfo(MyApplication.get(requireContext()).getTalentInfo()).getAbilityName());
            binding.talentExplanation.setText(talent.getInfo(MyApplication.get(requireContext()).getTalentInfo()).getAbilityExplanation());
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(binding.getRoot())
                    .setPositiveButton(R.string.got_it, null)
                    .create();
        }

    }

    private static final class UnitListTPAdapter extends ListAdapter<Unit, UnitListTPAdapter.ViewHolder> {

        private Talent.Priority currentPriority = Talent.Priority.TOP;

        @SuppressLint("NotifyDataSetChanged")
        public void updateUnits(List<Unit> units, Talent.Priority priority) {
            submitList(units);
            currentPriority = priority;
            notifyDataSetChanged();
        }

        public UnitListTPAdapter(Consumer<Unit> onItemClickListener,
                                 TriConsumer<View, Unit, Talent.Priority> onTalentIconClickListener) {
            super(UNIT_DIFFER);
            this.onItemClickListener = onItemClickListener;
            this.onTalentIconClickListener = onTalentIconClickListener;
        }

        private final Consumer<Unit> onItemClickListener;
        private final TriConsumer<View, Unit, Talent.Priority> onTalentIconClickListener;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemUnitTpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding, pos -> onItemClickListener.accept(getItem(pos)),
                    (v, pos) -> onTalentIconClickListener.accept(v, getItem(pos), currentPriority));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var unit = getItem(position);
            AssetImageLoading.loadAssetImage(holder.binding.unitIcon, unit.getLatestFormIconPath(MyApplication.get(holder.binding.getRoot().getContext()).getUnitExplanationData()));
            final var talents = unit.getTalents(currentPriority);
            for (int i = 0; i < talents.size(); i++) {
                holder.icons[i].setVisibility(View.VISIBLE);
                AssetImageLoading.loadAssetImage(holder.icons[i], talents.get(i).getPathToIcon());
            }
            for (int i = talents.size(); i < Unit.MAX_NUM_OF_TALENTS; i++) {
                holder.icons[i].setVisibility(View.GONE);
            }
            holder.binding.unitName.setText(unit.getExplanation(MyApplication.get(holder.binding.getRoot().getContext()).getUnitExplanationData()).getName(Unit.Form.FIRST));
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ListItemUnitTpBinding binding;
            private final ImageView[] icons;

            public ViewHolder(ListItemUnitTpBinding binding,
                              IntConsumer onItemClickListener,
                              ObjIntConsumer<View> onTalentClick) {
                super(binding.getRoot());
                this.binding = binding;
                icons = new ImageView[]{binding.talentIcon1, binding.talentIcon2, binding.talentIcon3, binding.talentIcon4, binding.talentIcon5, binding.talentIcon6};
                for (int i = 0; i < icons.length; i++) {
                    icons[i].setTag(i);
                }
                binding.talentIcon1.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                binding.talentIcon2.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                binding.talentIcon3.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                binding.talentIcon4.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                binding.talentIcon5.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                binding.talentIcon6.setOnClickListener(v -> onTalentClick.accept(v, getAbsoluteAdapterPosition()));
                itemView.setOnClickListener(v -> onItemClickListener.accept(getAbsoluteAdapterPosition()));
            }
        }
    }
}
