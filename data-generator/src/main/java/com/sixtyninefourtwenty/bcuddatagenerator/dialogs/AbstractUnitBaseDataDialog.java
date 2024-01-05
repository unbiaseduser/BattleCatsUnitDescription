package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddUnitBaseDataBinding;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.SpinnerAdapters;
import com.sixtyninefourtwenty.stuff.Views;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractUnitBaseDataDialog extends BottomSheetDialogFragment {

    private DialogAddUnitBaseDataBinding binding;
    private List<TextInputLayout> inputFields;

    @Nullable
    protected abstract UnitBaseData getExistingData();

    protected abstract boolean checkDuplicateUnitId(int input);

    protected abstract void onValidInput(@NonNull UnitBaseData newData);

    private void setInputFieldError(CharSequence error, TextInputLayout field) {
        field.setError(error);
        inputFields.forEach(til -> {
            if (til != field) {
                til.setError(null);
            }
        });
    }

    private boolean validateInfoInputField(CharSequence error, TextInputLayout inputLayout, TextInputEditText editText) {
        if (Views.isBlank(editText)) {
            setInputFieldError(error, inputLayout);
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddUnitBaseDataBinding.inflate(inflater, container, false);
        inputFields = List.of(binding.unitIdInputLayout, binding.usefulToOwnInputLayout, binding.usefulToTfOrTalentInputLayout);
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this, true)
                .setPositiveButton(new DialogButtonProperties.Builder(android.R.string.ok)
                        .setOnClickListener(() -> {
                            final var unitIdInputText = binding.unitIdInput.getText();
                            if (unitIdInputText == null || unitIdInputText.toString().isBlank()) {
                                setInputFieldError(getString(R.string.unit_id_empty_error), binding.unitIdInputLayout);
                                return;
                            }
                            if (checkDuplicateUnitId(Integer.parseInt(unitIdInputText.toString()))) {
                                setInputFieldError(getString(R.string.unit_id_already_exists_error), binding.unitIdInputLayout);
                                return;
                            }
                            String usefulToOwnBy = null;
                            if (binding.hasUsefulToOwnText.isChecked()) {
                                if (!validateInfoInputField(getString(R.string.useful_to_own_text_empty_error), binding.usefulToOwnInputLayout, binding.usefulToOwnInput)) {
                                    return;
                                }
                                usefulToOwnBy = Views.getInput(binding.usefulToOwnInput);
                            }
                            String usefulToTfBy = null;
                            if (binding.hasUsefulToTfOrTalentText.isChecked()) {
                                if (!validateInfoInputField(getString(R.string.useful_to_true_form_or_talent_text_empty_error), binding.usefulToTfOrTalentInputLayout, binding.usefulToTfOrTalentInput)) {
                                    return;
                                }
                                usefulToTfBy = Views.getInput(binding.usefulToTfOrTalentInput);
                            }
                            String hypermaxPriority = null;
                            if (binding.hasHypermaxPriority.isChecked()) {
                                if (!validateInfoInputField(getString(R.string.hypermax_priority_text_empty_error), binding.hypermaxPriorityInputLayout, binding.hypermaxPriorityInput)) {
                                    return;
                                }
                                hypermaxPriority = Views.getInput(binding.hypermaxPriorityInput);
                            }
                            onValidInput(new UnitBaseData(Integer.parseInt(unitIdInputText.toString()),
                                    UnitBaseData.Type.values()[binding.unitTypePicker.getSelectedItemPosition()],
                                    usefulToOwnBy,
                                    usefulToTfBy,
                                    hypermaxPriority));
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
        binding.unitTypePicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), Arrays.stream(UnitBaseData.Type.values())
                .map(type -> getString(type.getText()))
                .toArray(String[]::new)));
        binding.hasUsefulToOwnText.setOnCheckedChangeListener((buttonView, isChecked) -> binding.usefulToOwnGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        binding.hasUsefulToTfOrTalentText.setOnCheckedChangeListener((buttonView, isChecked) -> binding.usefulToTfOrTalentGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        binding.hasHypermaxPriority.setOnCheckedChangeListener((buttonView, isChecked) -> binding.hypermaxPriorityGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        final var existingData = getExistingData();
        if (existingData != null) {
            binding.unitIdInput.setText(Integer.toString(existingData.getUnitId()));
            if (existingData.hasInfo(UnitBaseData.Info.USEFUL_TO_OWN)) {
                binding.hasUsefulToOwnText.setChecked(true);
                binding.usefulToOwnInput.setText(existingData.getTextForInfo(UnitBaseData.Info.USEFUL_TO_OWN));
            }
            if (existingData.hasInfo(UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT)) {
                binding.hasUsefulToTfOrTalentText.setChecked(true);
                binding.usefulToTfOrTalentInput.setText(existingData.getTextForInfo(UnitBaseData.Info.USEFUL_TO_TF_OR_TALENT));
            }
            if (existingData.hasInfo(UnitBaseData.Info.HYPERMAX_PRIORITY)) {
                binding.hasHypermaxPriority.setChecked(true);
                binding.hypermaxPriorityInput.setText(existingData.getTextForInfo(UnitBaseData.Info.HYPERMAX_PRIORITY));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
