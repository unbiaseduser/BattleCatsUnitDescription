package com.sixtyninefourtwenty.bcud.ui.fragments.unitdesc;

import static com.sixtyninefourtwenty.stuff.listeners.ShortOnLongClickListener.shorten;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.ComboListAdapter;
import com.sixtyninefourtwenty.bcud.adapters.TFMaterialAdapter;
import com.sixtyninefourtwenty.bcud.databinding.FragmentUdpUnitInfoBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.ui.activities.PhotoEditorActivity;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.BalloonFactory;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.ComboDataViewModel;
import com.sixtyninefourtwenty.bcud.viewmodels.UnitDataViewModel;
import com.sixtyninefourtwenty.common.utils.Validations;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Setter;

public final class UnitDescUnitInfoFragment extends BaseViewBindingFragment<@NonNull FragmentUdpUnitInfoBinding> {
    private ComboListAdapter comboListAdapter;
    private TFMaterialAdapter tfMaterialAdapter;
    private ComboDataViewModel comboDataViewModel;

    @Override
    protected @NonNull FragmentUdpUnitInfoBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentUdpUnitInfoBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull FragmentUdpUnitInfoBinding binding, @Nullable Bundle savedInstanceState) {
        final var unit = UnitDescUnitInfoFragmentArgs.fromBundle(requireArguments()).getUnit();
        binding.descExpander.setOnClickListener(v -> binding.expandDesc.toggle());
        binding.tfCostsExpander.setOnClickListener(v -> binding.expandTfMats.toggle());
        binding.talentsTableExpander.setOnClickListener(v -> binding.expandTalents.toggle());
        binding.comboListExpander.setOnClickListener(v -> binding.expandComboList.toggle());

        final var unitDataViewModel = UnitDataViewModel.get(requireActivity());
        comboDataViewModel = ComboDataViewModel.get(requireActivity());
        comboListAdapter = new ComboListAdapter(unitDataViewModel::setCurrentUnitToDisplay);
        binding.tfMaterialsList.setLayoutManager(new GridLayoutManager(requireContext(), Unit.MAX_NUM_OF_TF_MATERIALS));
        tfMaterialAdapter = new TFMaterialAdapter((v, material) -> BalloonFactory.createWithUsualSettings(requireContext())
                .setText(material.getMaterial(MyApplication.get(requireContext()).getMaterialData()).getInfo(MyApplication.get(requireContext()).getMaterialInfo()).getName())
                .build()
                .showAlignTop(v));
        binding.tfMaterialsList.setNestedScrollingEnabled(false);
        binding.tfMaterialsList.setAdapter(tfMaterialAdapter);
        binding.comboList.setAdapter(comboListAdapter);
        final var menu = new UDPUnitInfoFragmentMenu(unit);
        unitDataViewModel.setCurrentUnitToDisplay(unit);
        unitDataViewModel.getCurrentUnitToDisplay().observe(getViewLifecycleOwner(), u -> setup(u, menu, binding));

        requireActivity().addMenuProvider(menu, getViewLifecycleOwner());
    }

    @SuppressLint("SetTextI18n")
    private void setup(Unit unit, UDPUnitInfoFragmentMenu menu, FragmentUdpUnitInfoBinding binding) {
        final var app = MyApplication.get(requireContext());
        final var unitExplanationData = app.getUnitExplanationData();
        menu.setUnit(unit);
        setToolbarTitle(unit.getExplanation(unitExplanationData).getFirstFormName());
        AssetImageLoading.loadAssetImage(binding.iconFirstForm, unit.getIconPathForForm(Unit.Form.FIRST, MyApplication.get(requireContext()).getUnitExplanationData()));
        setupClicks(binding.iconFirstForm, unit.getExplanation(unitExplanationData).getFirstFormName());
        AssetImageLoading.loadAssetImage(binding.iconSecondForm, unit.getIconPathForForm(Unit.Form.SECOND, MyApplication.get(requireContext()).getUnitExplanationData()));
        setupClicks(binding.iconSecondForm, unit.getExplanation(unitExplanationData).getSecondFormName());
        if (unit.hasTF(unitExplanationData)) {
            binding.iconThirdForm.setVisibility(View.VISIBLE);
            AssetImageLoading.loadAssetImage(binding.iconThirdForm, unit.getIconPathForForm(Unit.Form.TRUE, MyApplication.get(requireContext()).getUnitExplanationData()));
            setupClicks(binding.iconThirdForm, unit.getExplanation(unitExplanationData).getTrueFormName());
        } else {
            binding.iconThirdForm.setVisibility(View.GONE);
        }
        binding.unitDesc.setText(unit.getDesc(requireContext()));

        final var descPageTexts = unit.getDescPageTexts(app.getDescPageTextData());
        final var usefulToOwnByText = descPageTexts.getUsefulToOwnBy();
        binding.usefulToOwnBy2.setText(usefulToOwnByText);
        binding.usefulToOwnCard.setVisibility(!Validations.isValidInfoString(usefulToOwnByText) ? View.GONE : View.VISIBLE);
        final var usefulToTfOrTalentText = descPageTexts.getUsefulToTFOrTalentBy();
        binding.usefulToTfBy.setText(unit.isCfSpecial() ? R.string.useful_to_unlock_talents_by : R.string.useful_to_tf_by);
        binding.usefulToTfBy2.setText(usefulToTfOrTalentText);
        binding.usefulToTfCard.setVisibility(!Validations.isValidInfoString(usefulToTfOrTalentText) ? View.GONE : View.VISIBLE);
        final var hypermaxPriorityText = descPageTexts.getHypermaxPriority();
        binding.hp2.setText(hypermaxPriorityText);
        binding.hpCard.setVisibility(!Validations.isValidInfoString(hypermaxPriorityText) ? View.GONE : View.VISIBLE);

        final var tfMaterials = unit.getTfMaterialData();
        tfMaterialAdapter.submitList(tfMaterials);
        if (unit.hasTF(unitExplanationData) && !tfMaterials.isEmpty()) {
            binding.tfCostsCard.setVisibility(View.VISIBLE);
        } else {
            binding.tfCostsCard.setVisibility(View.GONE);
        }

        final var talentHolders = List.of(binding.firstTalent, binding.secondTalent, binding.thirdTalent, binding.fourthTalent, binding.fifthTalent, binding.sixthTalent);
        final var talentPrioHolders = List.of(binding.firstTalentPrio, binding.secondTalentPrio, binding.thirdTalentPrio, binding.fourthTalentPrio, binding.fifthTalentPrio, binding.sixthTalentPrio);
        final var talents = unit.getTalentData();
        if (!talents.isEmpty()) {
            binding.talentsCard.setVisibility(View.VISIBLE);
            for (int i = 0; i < talents.size(); i++) {
                final var talentHolder = talentHolders.get(i);
                final var talentPrioHolder = talentPrioHolders.get(i);
                talentHolder.setVisibility(View.VISIBLE);
                talentPrioHolder.setVisibility(View.VISIBLE);
                talentHolder.setText(talents.get(i).getTalent(app.getTalentData()).getInfo(app.getTalentInfo()).getAbilityName());
                talentPrioHolder.setText(talents.get(i).getPriority().getText());
            }
            for (int i = talents.size(); i < Unit.MAX_NUM_OF_TALENTS; i++) {
                talentHolders.get(i).setVisibility(View.GONE);
                talentPrioHolders.get(i).setVisibility(View.GONE);
            }
        } else {
            binding.talentsCard.setVisibility(View.GONE);
        }

        final var combosContainingUnit = comboDataViewModel.findCombosContainingUnit(unit);
        if (!combosContainingUnit.isEmpty()) {
            binding.combosCard.setVisibility(View.VISIBLE);
            comboListAdapter.submitList(combosContainingUnit);
        } else {
            binding.combosCard.setVisibility(View.GONE);
        }

    }

    private void setupClicks(ImageView view, String tooltip) {
        view.setOnClickListener(BalloonFactory.createWithUsualSettings(requireContext())
                .setText(tooltip)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build()::showAlignBottom);
        view.setOnLongClickListener(shorten(v -> startActivity(new Intent(requireContext(), PhotoEditorActivity.class)
                .putExtra("img_to_edit", Utils.drawableToBitmap(view.getDrawable())))));
    }

    @AllArgsConstructor
    private final class UDPUnitInfoFragmentMenu implements MenuProvider {
        @Setter
        @NonNull
        private Unit unit;
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_unit_info, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.go_to_website_unit_info) {
                openWebsite(unit.getUdpUrl());
                return true;
            }
            return false;
        }
    }

}