package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.elderepictfpriority;

import static java.util.Objects.requireNonNullElseGet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.ListItemUnitEepBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;
import com.sixtyninefourtwenty.common.objects.ElderEpic;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public abstract class ElderEpicTabFragment extends BaseFragment<@NonNull RecyclerView> {
    protected abstract ElderEpic getElderEpic();

    @Override
    protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new RecyclerView(requireContext());
    }

    @Override
    protected void setup(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
        final var elderEpicList = MyApplication.get(requireContext()).getUnitData().getElderEpicList(getElderEpic());
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setAdapter(new UnitListEEPAdapter(elderEpicList,
                unit -> getNavController().navigate(ElderEpicFragmentDirections.actionNavElderEpicToNavUdpUnitInfo(unit)),
                unit -> getNavController().navigate(ElderEpicFragmentDirections.showEepDialog(unit, getElderEpic()))));
    }

    private static final class UnitListEEPAdapter extends ListAdapter<Unit, UnitListEEPAdapter.ViewHolder> {

        public UnitListEEPAdapter(
                List<Unit> list,
                Consumer<Unit> onIconClickListener,
                Consumer<Unit> onButtonClickListener
        ) {
            this(list, onIconClickListener, onButtonClickListener, null);
        }

        public UnitListEEPAdapter(
                List<Unit> list,
                Consumer<Unit> onIconClickListener,
                Consumer<Unit> onButtonClickListener,
                @Nullable UnitExplanationSupplier unitExplanationSupplier
        ) {
            super(Utils.UNIT_DIFFER);
            submitList(list);
            this.onIconClickListener = onIconClickListener;
            this.onButtonClickListener = onButtonClickListener;
            this.unitExplanationSupplier = unitExplanationSupplier;
        }

        private final Consumer<Unit> onIconClickListener;
        private final Consumer<Unit> onButtonClickListener;
        @Nullable
        private final UnitExplanationSupplier unitExplanationSupplier;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemUnitEepBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding,
                    pos -> onIconClickListener.accept(getItem(pos)),
                    pos -> onButtonClickListener.accept(getItem(pos)));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var unit = getItem(position);
            final var context = holder.binding.getRoot().getContext();
            final var unitExplanationData = requireNonNullElseGet(
                    unitExplanationSupplier,
                    () -> MyApplication.get(context).getUnitExplanationData()
            );
            AssetImageLoading.loadAssetImage(holder.binding.unitIcon, unit.getLatestFormIconPath(unitExplanationData));
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ListItemUnitEepBinding binding;

            public ViewHolder(ListItemUnitEepBinding binding, IntConsumer onIconClickListener, IntConsumer onButtonClickListener) {
                super(binding.getRoot());
                this.binding = binding;
                binding.unitIcon.setOnClickListener(v -> onIconClickListener.accept(getAbsoluteAdapterPosition()));
                binding.reason.setOnClickListener(v -> onButtonClickListener.accept(getAbsoluteAdapterPosition()));
            }
        }
    }
}
