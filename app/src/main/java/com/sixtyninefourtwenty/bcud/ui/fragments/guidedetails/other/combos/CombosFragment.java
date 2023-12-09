package com.sixtyninefourtwenty.bcud.ui.fragments.guidedetails.other.combos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MenuProvider;

import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.ComboListAdapter;
import com.sixtyninefourtwenty.bcud.databinding.DialogSearchFilterCombosBinding;
import com.sixtyninefourtwenty.bcud.databinding.FragmentCombosBinding;
import com.sixtyninefourtwenty.bcud.objects.Combo;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.ComboDataViewModel;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.interfaces.MyTextWatcher;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public final class CombosFragment extends BaseViewBindingFragment<@NonNull FragmentCombosBinding> {
    private final MenuProvider provider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_combos, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.filter_combos) {
                navigate(CombosFragmentDirections.showSearchFilterCombosDialog());
                return true;
            }
            return false;
        }
    };

    @Override
    protected @NonNull FragmentCombosBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCombosBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull FragmentCombosBinding binding, @Nullable Bundle savedInstanceState) {
        final var model = ComboDataViewModel.get(requireActivity());
        final var adapter = new ComboListAdapter(unit -> navigate(CombosFragmentDirections.goToUnitInfoFromCombos(unit)));
        binding.list.setAdapter(adapter);
        model.getCurrentComboList().observe(getViewLifecycleOwner(), combos -> {
            binding.getRoot().setViewState(combos.isEmpty() ? MultiStateView.ViewState.EMPTY : MultiStateView.ViewState.CONTENT);
            adapter.submitList(combos);
        });
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

    public static final class SearchFilterCombosDialog extends BaseViewBindingBottomSheetAlertDialogFragment<@NonNull DialogSearchFilterCombosBinding> {

        private ComboDataViewModel model;

        @Override
        protected @NonNull DialogSearchFilterCombosBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
            return DialogSearchFilterCombosBinding.inflate(inflater, container, false);
        }

        @Override
        protected @NonNull View initDialogView(DialogSearchFilterCombosBinding binding) {
            return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this, true)
                    .setTitle(R.string.search_filter_combos)
                    .setPositiveButton(new DialogButtonProperties.Builder(R.string.search)
                            .setOnClickListener(() -> model.setCurrentComboList(model.filterCombos(MyApplication.get(requireContext()).getUnitExplanationData(), MyApplication.get(requireContext()).getComboNameData())))
                            .build())
                    .setNeutralButton(new DialogButtonProperties.Builder(R.string.reset)
                            .setOnClickListener(() -> model.resetSearchFilterCombosState())
                            .build())
                    .setNegativeButton(DialogButtonProperties.ofOnlyText(android.R.string.cancel))
                    .getRootView();
        }

        @Override
        protected void setup(@NonNull DialogSearchFilterCombosBinding binding, @Nullable Bundle savedInstanceState) {
            model = ComboDataViewModel.get(requireActivity());
            setupViews(model, binding);
        }

        private void setupViews(@NonNull ComboDataViewModel model, @NonNull DialogSearchFilterCombosBinding binding) {
            binding.byNamesExpander.setOnClickListener(v -> binding.expandByNames.toggle());
            binding.byTypesExpander.setOnClickListener(v -> binding.expandByTypes.toggle());
            binding.storyLegends.setTag(UnitBaseData.Type.STORY_LEGEND);
            binding.cfSpecials.setTag(UnitBaseData.Type.CF_SPECIAL);
            binding.adventDrops.setTag(UnitBaseData.Type.ADVENT_DROP);
            binding.rares.setTag(UnitBaseData.Type.RARE);
            binding.superRares.setTag(UnitBaseData.Type.SUPER_RARE);
            binding.ubers.setTag(UnitBaseData.Type.UBER);
            binding.legendRares.setTag(UnitBaseData.Type.LEGEND_RARE);
            final var unitTypeCheckboxes = List.of(binding.storyLegends, binding.cfSpecials, binding.adventDrops, binding.rares, binding.superRares, binding.ubers, binding.legendRares);
            for (final var cb : unitTypeCheckboxes) {
                final var type = (UnitBaseData.Type) cb.getTag();
                if (model.unitSearchScopeContainsType(type)) {
                    cb.setChecked(true);
                }
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        model.addTypeToUnitSearchScope(type);
                    } else {
                        model.removeTypeFromUnitSearchScope(type);
                    }
                });
            }

            model.getCurrentQueryByUnit().observe(this, input -> {
                if (input.isBlank()) {
                    if (binding.expandUnitTypesTable.getState() == ExpandableLayout.State.EXPANDED) {
                        binding.expandUnitTypesTable.collapse();
                    }
                } else {
                    if (binding.expandUnitTypesTable.getState() == ExpandableLayout.State.COLLAPSED) {
                        binding.expandUnitTypesTable.expand();
                    }
                }
            });

            binding.nameInput.setText(model.getCurrentQueryByName());
            binding.nameInput.addTextChangedListener((MyTextWatcher) (s, start, before, count) ->
                    model.setCurrentQueryByName(s.toString()));
            binding.unitInput.setText(model.getCurrentQueryByUnit().getValue());
            binding.unitInput.addTextChangedListener((MyTextWatcher) (s, start, before, count) ->
                    model.setCurrentQueryByUnit(s.toString()));
            binding.atk.setTag(Combo.Type.ATTACK);
            binding.def.setTag(Combo.Type.DEFENSE);
            binding.spd.setTag(Combo.Type.MOVE_SPEED);
            binding.strong.setTag(Combo.Type.STRONG);
            binding.massive.setTag(Combo.Type.MASSIVE);
            binding.resist.setTag(Combo.Type.RESIST);
            binding.kb.setTag(Combo.Type.KNOCKBACK);
            binding.slow.setTag(Combo.Type.SLOW);
            binding.freeze.setTag(Combo.Type.FREEZE);
            binding.weaken.setTag(Combo.Type.WEAKEN);
            binding.strengthen.setTag(Combo.Type.STRENGTHEN);
            binding.crit.setTag(Combo.Type.CRITICAL);
            binding.startCannon.setTag(Combo.Type.CANNON_START);
            binding.cannonAtk.setTag(Combo.Type.CANNON_POWER);
            binding.recharge.setTag(Combo.Type.CANNON_RECHARGE);
            binding.baseDef.setTag(Combo.Type.BASE_DEFENSE);
            binding.startMoney.setTag(Combo.Type.STARTING_MONEY);
            binding.startLevel.setTag(Combo.Type.WORKER_START_LEVEL);
            binding.wallet.setTag(Combo.Type.WORKER_MAX);
            binding.research.setTag(Combo.Type.RESEARCH);
            binding.accounting.setTag(Combo.Type.ACCOUNTING);
            binding.study.setTag(Combo.Type.STUDY);
            final var comboCheckboxes = List.of(binding.atk, binding.def, binding.spd, binding.strong, binding.massive, binding.resist, binding.kb, binding.slow, binding.freeze, binding.weaken, binding.strengthen, binding.crit,
                    binding.startCannon, binding.cannonAtk, binding.recharge, binding.baseDef, binding.startMoney, binding.startLevel, binding.wallet, binding.research, binding.accounting, binding.study);
            for (final var cb : comboCheckboxes) {
                final var comboType = (Combo.Type) cb.getTag();
                if (model.comboFiltersContainsType(comboType)) {
                    cb.setChecked(true);
                }
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked)
                        model.addTypeToComboFilters(comboType);
                    else
                        model.removeTypeFromComboFilters(comboType);
                });
            }
        }
    }

}
