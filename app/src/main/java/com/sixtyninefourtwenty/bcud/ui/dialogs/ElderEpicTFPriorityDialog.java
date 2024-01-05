package com.sixtyninefourtwenty.bcud.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.TFMaterialAdapter;
import com.sixtyninefourtwenty.bcud.databinding.DialogEepBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.BalloonFactory;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ElderEpicTFPriorityDialog extends BaseViewBindingBottomSheetAlertDialogFragment<@NonNull DialogEepBinding> {

    @Override
    protected @NonNull DialogEepBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DialogEepBinding.inflate(inflater, container, false);
    }

    @Override
    protected @NonNull View initDialogView(@NonNull DialogEepBinding binding) {
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this)
                .setTitle(R.string.reason)
                .setPositiveButton(DialogButtonProperties.ofOnlyText(R.string.got_it))
                .getRootView();
    }

    @Override
    protected void setup(@NonNull DialogEepBinding binding, @Nullable Bundle savedInstanceState) {
        final var args = ElderEpicTFPriorityDialogArgs.fromBundle(requireArguments());
        final var unit = args.getUnit();
        final var elderEpic = args.getElderEpic();
        final var reason = unit.getEEPriorityReasoning(MyApplication.get(requireContext()).getEEPReasoningData(), args.getElderEpic());
        final var finalReason = reason != null ? reason : "This unit does not have a reason for " + elderEpic + ". If you're not a developer, please file a bug report.";
        binding.reason.setText(finalReason);
        final var tfMaterials = unit.getTfMaterialData();
        binding.tfMaterialsList.setLayoutManager(new GridLayoutManager(requireContext(), Unit.MAX_NUM_OF_TF_MATERIALS));
        binding.tfMaterialsList.setAdapter(new TFMaterialAdapter(tfMaterials, (v, material) -> BalloonFactory.createWithUsualSettings(requireContext())
                .setText(material.getMaterial(MyApplication.get(requireContext()).getMaterialData()).getInfo(MyApplication.get(requireContext()).getMaterialInfo()).getName())
                .build()
                .showAlignTop(v)));
    }

}
