package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.elderepictfpriority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.ListItemUnitEepBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;
import com.sixtyninefourtwenty.common.objects.ElderEpic;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public abstract class ElderEpicTabFragment extends BaseFragment<RecyclerView> {
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

        public UnitListEEPAdapter(List<Unit> list, Consumer<Unit> onIconClickListener, Consumer<Unit> onButtonClickListener) {
            super(Utils.UNIT_DIFFER);
            submitList(list);
            this.onIconClickListener = onIconClickListener;
            this.onButtonClickListener = onButtonClickListener;
        }

        private final Consumer<Unit> onIconClickListener;
        private final Consumer<Unit> onButtonClickListener;

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
            AssetImageLoading.loadAssetImage(holder.binding.unitIcon, getItem(position).getLatestFormIconPath(MyApplication.get(holder.binding.getRoot().getContext()).getUnitExplanationData()));
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
