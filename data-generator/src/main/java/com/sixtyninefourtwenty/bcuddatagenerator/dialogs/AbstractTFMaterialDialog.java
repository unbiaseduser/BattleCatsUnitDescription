package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sixtyninefourtwenty.bcuddatagenerator.MyApplication;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddTfMaterialBinding;
import com.sixtyninefourtwenty.common.objects.TFMaterial;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.misc.NoAutoDismissAlertDialogBuilder;
import com.sixtyninefourtwenty.common.utils.Formatting;
import com.sixtyninefourtwenty.common.utils.SpinnerAdapters;

import java.util.Arrays;

public abstract class AbstractTFMaterialDialog extends AppCompatDialogFragment {

    private DialogAddTfMaterialBinding binding;

    @StringRes
    protected abstract int getTitle();

    @Nullable
    protected abstract TFMaterialData getExistingMaterial();

    protected abstract boolean checkDuplicateMaterialIndex(int index);

    protected abstract void onValidInput(@NonNull TFMaterialData newData);

    @NonNull
    @Override
    public final Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddTfMaterialBinding.inflate(getLayoutInflater());
        final var existingMaterial = getExistingMaterial();
        binding.materialPicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), Arrays.stream(TFMaterial.values())
                .map(material -> material.getInfo(MyApplication.get(requireContext()).getMaterialInfo()).getName())
                .toArray(String[]::new)));
        if (existingMaterial != null) {
            binding.quantityInput.setText(Formatting.formatANumber(requireContext(), existingMaterial.getQuantity()));
        }
        return new NoAutoDismissAlertDialogBuilder(requireContext())
                .setTitle(getTitle())
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, false, (dialog, which) -> {
                    if (checkDuplicateMaterialIndex(binding.materialPicker.getSelectedItemPosition())) {
                        Toast.makeText(requireContext(), R.string.material_already_exists_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final var text = binding.quantityInput.getText();
                    if (text == null || text.toString().isBlank()) {
                        binding.quantityInputLayout.setError(getString(R.string.quantity_empty_error));
                        return;
                    }
                    final var quantity = Integer.parseInt(text.toString());
                    if (quantity == 0) {
                        binding.quantityInputLayout.setError(getString(R.string.quantity_zero_error));
                        return;
                    }
                    onValidInput(new TFMaterialData(
                            TFMaterial.values()[binding.materialPicker.getSelectedItemPosition()],
                            quantity
                    ));
                    dismiss();
                })
                .setNegativeButton(android.R.string.cancel, true, null)
                .create();
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
