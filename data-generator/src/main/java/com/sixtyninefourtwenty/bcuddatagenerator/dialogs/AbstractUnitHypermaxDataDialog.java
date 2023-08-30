package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddUnitHypermaxDataBinding;
import com.sixtyninefourtwenty.common.misc.NoAutoDismissAlertDialogBuilder;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.UnitHypermaxData;
import com.sixtyninefourtwenty.common.utils.Formatting;
import com.sixtyninefourtwenty.common.utils.SpinnerAdapters;

import java.util.Arrays;

public abstract class AbstractUnitHypermaxDataDialog extends AppCompatDialogFragment {

    private DialogAddUnitHypermaxDataBinding binding;

    @Nullable
    protected abstract UnitHypermaxData getExistingData();

    @StringRes
    protected abstract int getTitle();

    protected abstract boolean checkDuplicateUnitId(int input);

    protected abstract void onValidInput(int unitId, int selectedUnitTypePosition, int selectedUnitPriorityPosition);

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddUnitHypermaxDataBinding.inflate(getLayoutInflater());
        final var existingData = getExistingData();
        if (existingData != null) {
            binding.unitIdInput.setText(Formatting.formatANumber(requireContext(), existingData.getUnitId()));
        }
        binding.priorityPicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), Arrays.stream(Hypermax.Priority.values())
                .map(p -> getString(p.getText()))
                .toArray(String[]::new)));
        binding.unitTypePicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), Arrays.stream(Hypermax.UnitType.values())
                .map(p -> getString(p.getText()))
                .toArray(String[]::new)));
        return new NoAutoDismissAlertDialogBuilder(requireContext())
                .setTitle(getTitle())
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, false, (dialog, which) -> {
                    final var text = binding.unitIdInput.getText();
                    if (text == null || text.toString().isBlank()) {
                        binding.unitIdInputLayout.setError(getString(R.string.unit_id_empty_error));
                        return;
                    }
                    if (checkDuplicateUnitId(Integer.parseInt(text.toString()))) {
                        binding.unitIdInputLayout.setError(getString(R.string.unit_id_already_exists_error));
                        return;
                    }
                    onValidInput(Integer.parseInt(text.toString()), binding.unitTypePicker.getSelectedItemPosition(), binding.priorityPicker.getSelectedItemPosition());
                    dismiss();
                })
                .setNegativeButton(android.R.string.cancel, true, null)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
