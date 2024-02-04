package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.hypermaxpriority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.UnitListAdapterGrid;
import com.sixtyninefourtwenty.bcud.adapters.UnitListAdapterList;
import com.sixtyninefourtwenty.bcud.databinding.HpTpTabBinding;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.AppSettingsViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.SearchViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.UnitDataViewModel;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.materialpopupmenu.builder.ItemBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.PopupMenuBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.SectionBuilder;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class HypermaxPriorityTabFragment extends BaseViewBindingFragment<@NonNull HpTpTabBinding> {
    protected abstract Hypermax.UnitType getHypermaxPriorityType();

    @Override
    protected @NonNull HpTpTabBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return HpTpTabBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull HpTpTabBinding binding, @Nullable Bundle savedInstanceState) {
        final var searchModel = SearchViewModel.get(requireActivity());
        final var settingsModel = AppSettingsViewModel.get(requireActivity());
        final var unitModel = UnitDataViewModel.get(requireActivity());
        final var spn = binding.spinner;
        spn.setItemSelectedListener(position -> {
            switch (position) {
                case 0 -> unitModel.setHypermaxPriorityType(getHypermaxPriorityType(), Hypermax.Priority.MAX);
                case 1 -> unitModel.setHypermaxPriorityType(getHypermaxPriorityType(), Hypermax.Priority.HIGH);
                case 2 -> unitModel.setHypermaxPriorityType(getHypermaxPriorityType(), Hypermax.Priority.MID);
                case 3 -> unitModel.setHypermaxPriorityType(getHypermaxPriorityType(), Hypermax.Priority.LOW);
                case 4 -> unitModel.setHypermaxPriorityType(getHypermaxPriorityType(), Hypermax.Priority.MIN);
            }
        });
        spn.setItemStringArrayRes(R.array.hypermax_priorities);
        final var rcv = binding.list;
        rcv.setHasFixedSize(true);
        final var decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        final var adapterGridView = new UnitListAdapterGrid(unit -> getNavController().navigate(HypermaxPriorityFragmentDirections.actionNavHypermaxPriorityToNavUdpUnitInfo(unit)),
                (v, unit) -> new PopupMenuBuilder(requireContext(), v)
                        .addSection(new SectionBuilder()
                                .addItem(new ItemBuilder(R.string.add_to_favorites)
                                        .setIcon(R.drawable.star)
                                        .setOnSelectListener(() -> getNavController().navigate(HypermaxPriorityFragmentDirections.showAddEditFavoritesDialog(unit)))
                                        .build())
                                .build())
                        .build()
                        .show());
        final var adapterListView = new UnitListAdapterList(unit -> getNavController().navigate(HypermaxPriorityFragmentDirections.actionNavHypermaxPriorityToNavUdpUnitInfo(unit)),
                (v, unit) -> new PopupMenuBuilder(requireContext(), v)
                        .addSection(new SectionBuilder()
                                .addItem(new ItemBuilder(R.string.add_to_favorites)
                                        .setIcon(R.drawable.star)
                                        .setOnSelectListener(() -> getNavController().navigate(HypermaxPriorityFragmentDirections.showAddEditFavoritesDialog(unit)))
                                        .build())
                                .build())
                        .build()
                        .show());
        settingsModel.getListViewMode().observe(getViewLifecycleOwner(), mode -> {
            switch (mode) {
                case LIST -> {
                    rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
                    rcv.setAdapter(adapterListView);
                    rcv.addItemDecoration(decoration);
                }
                case GRID -> {
                    rcv.setLayoutManager(new GridLayoutManager(requireContext(), UnitListAdapterGrid.getGridSpans(requireContext())));
                    rcv.setAdapter(adapterGridView);
                    rcv.removeItemDecoration(decoration);
                }
            }
        });
        searchModel.getQuery().observe(getViewLifecycleOwner(), query -> unitModel.setHypermaxPriorityQuery(getHypermaxPriorityType(), query));

        unitModel.getHypermaxPriorityLiveData(getHypermaxPriorityType()).observe(getViewLifecycleOwner(), list -> {
            adapterListView.submitList(list);
            adapterGridView.submitList(list);
        });
    }

}
