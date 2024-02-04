package com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.UnitListAdapterGrid;
import com.sixtyninefourtwenty.bcud.adapters.UnitListAdapterList;
import com.sixtyninefourtwenty.bcud.databinding.GenericSearchableListBinding;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.AppSettingsViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.SearchViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.UnitDataViewModel;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.materialpopupmenu.builder.ItemBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.PopupMenuBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.SectionBuilder;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public abstract class UnitDescTabFragment extends BaseViewBindingFragment<@NonNull GenericSearchableListBinding> {
    protected abstract UnitBaseData.Type getUnitType();

    @Override
    protected @NonNull GenericSearchableListBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return GenericSearchableListBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull GenericSearchableListBinding binding, @Nullable Bundle savedInstanceState) {
        final var searchModel = SearchViewModel.get(requireActivity());
        final var settingsModel = AppSettingsViewModel.get(requireActivity());
        final var unitModel = UnitDataViewModel.get(requireActivity());
        final var rcv = binding.list;
        rcv.setHasFixedSize(true);
        new FastScrollerBuilder(rcv).build();
        final var decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        final var adapterGridView = new UnitListAdapterGrid(unit -> getNavController().navigate(com.sixtyninefourtwenty.bcud.ui.fragments.UnitDescFragmentDirections.goToUnitInfo(unit)),
                (v, unit) -> new PopupMenuBuilder(requireContext(), v)
                        .addSection(new SectionBuilder()
                                .addItem(new ItemBuilder(R.string.add_to_favorites)
                                        .setIcon(R.drawable.star)
                                        .setOnSelectListener(() -> getNavController().navigate(com.sixtyninefourtwenty.bcud.ui.fragments.UnitDescFragmentDirections.showAddEditFavoritesDialog(unit)))
                                        .build())
                                .build())
                        .build()
                        .show());
        final var adapterListView = new UnitListAdapterList(unit -> getNavController().navigate(com.sixtyninefourtwenty.bcud.ui.fragments.UnitDescFragmentDirections.goToUnitInfo(unit)),
                (v, unit) -> new PopupMenuBuilder(requireContext(), v)
                        .addSection(new SectionBuilder()
                                .addItem(new ItemBuilder(R.string.add_to_favorites)
                                        .setIcon(R.drawable.star)
                                        .setOnSelectListener(() -> getNavController().navigate(com.sixtyninefourtwenty.bcud.ui.fragments.UnitDescFragmentDirections.showAddEditFavoritesDialog(unit)))
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
        searchModel.getQuery().observe(getViewLifecycleOwner(), query -> unitModel.setUnitDescQuery(getUnitType(), query));
        unitModel.getUnitDescLiveData(getUnitType()).observe(getViewLifecycleOwner(), list -> {
            adapterListView.submitList(list);
            adapterGridView.submitList(list);
            binding.getRoot().setViewState(list.isEmpty() ? MultiStateView.ViewState.EMPTY : MultiStateView.ViewState.CONTENT);
        });
    }

}
