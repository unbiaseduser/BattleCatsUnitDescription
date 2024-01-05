package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddUnitEepDataBinding;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.UnitEEPriorityData;
import com.sixtyninefourtwenty.common.utils.Formatting;
import com.sixtyninefourtwenty.stuff.Views;

public abstract class AbstractUnitEEPDataDialog extends BottomSheetDialogFragment {

    private DialogAddUnitEepDataBinding binding;

    @Nullable
    protected abstract UnitEEPriorityData getExistingData();

    protected abstract boolean checkDuplicateUnitIdAndElderEpic(int input, @NonNull ElderEpic elderEpic);

    protected abstract void onValidInput(@NonNull UnitEEPriorityData newData);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddUnitEepDataBinding.inflate(inflater, container, false);
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this, true)
                .setPositiveButton(new DialogButtonProperties.Builder(android.R.string.ok)
                        .setOnClickListener(() -> {
                            final var text = binding.unitIdInput.getText();
                            if (text == null || text.toString().isBlank()) {
                                binding.unitIdInputLayout.setError(getString(R.string.unit_id_empty_error));
                                binding.elderOrEpicInputLayout.setError(null);
                                return;
                            }
                            var elderEpic = ElderEpic.ELDER;
                            if (binding.elderOrEpicToggle.getCheckedButtonId() == binding.epic.getId()) {
                                elderEpic = ElderEpic.EPIC;
                            }
                            if (checkDuplicateUnitIdAndElderEpic(Integer.parseInt(text.toString()), elderEpic)) {
                                binding.unitIdInputLayout.setError(getString(R.string.unit_id_already_exists_error));
                                binding.elderOrEpicInputLayout.setError(null);
                                return;
                            }
                            if (Views.isBlank(binding.elderOrEpicInput)) {
                                binding.unitIdInputLayout.setError(null);
                                binding.elderOrEpicInputLayout.setError(getString(R.string.elder_epic_priority_text_empty_error));
                                return;
                            }
                            onValidInput(new UnitEEPriorityData(
                                    Integer.parseInt(text.toString()),
                                    elderEpic,
                                    Views.getInput(binding.elderOrEpicInput)
                            ));
                            dismiss();
                        })
                        .disableDismissAfterClick()
                        .build())
                .setNegativeButton(DialogButtonProperties.ofOnlyText(android.R.string.cancel))
                .getRootView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final var existingData = getExistingData();
        if (existingData != null) {
            binding.unitIdInput.setText(Formatting.formatANumber(requireContext(), existingData.getUnitId()));
            switch (existingData.getElderEpic()) {
                case ELDER -> binding.elderOrEpicToggle.check(binding.elder.getId());
                case EPIC -> binding.elderOrEpicToggle.check(binding.epic.getId());
            }
            binding.elderOrEpicInput.setText(existingData.getText());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
