package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sixtyninefourtwenty.bcuddatagenerator.MyApplication;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.bcuddatagenerator.adapters.TFMaterialAdapter;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddUnitTfMaterialDataBinding;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.UnitTFMaterialData;
import com.sixtyninefourtwenty.common.utils.RecyclerViews;
import com.sixtyninefourtwenty.stuff.ListAdapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractUnitTFMaterialDataDialog extends BottomSheetDialogFragment {

    private DialogAddUnitTfMaterialDataBinding binding;
    private final ItemTouchHelper dragDropMaterial = RecyclerViews.createVerticalDragDropTouchHelper((oldPos, newPos) -> ListAdapters.modifyList((TFMaterialAdapter) binding.materialsToAdd.getAdapter(), list -> Collections.swap(list, oldPos, newPos)));
    private final TFMaterialAdapter adapter = new TFMaterialAdapter(
            (tfMaterial, a) -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete)
                    .setMessage(getString(R.string.delete_item_confirmation, tfMaterial.getMaterial(MyApplication.get(requireContext()).getMaterialData()).getInfo(MyApplication.get(requireContext()).getMaterialInfo()).getName()))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ListAdapters.removeElement(a, tfMaterial))
                    .setNegativeButton(android.R.string.cancel, null)
                    .show(),
            (tfMaterial, a) -> {
                final var materialData = MyApplication.get(requireContext()).getMaterialData();
                NavHostFragment.findNavController(this).navigate(getShowEditTFMaterialDialogDirections(tfMaterial, a.getCurrentList().stream().mapToInt(data -> materialData.getMaterials().indexOf(data.getMaterial(materialData))).toArray()));
            },
            dragDropMaterial::startDrag
    );

    @Nullable
    protected abstract UnitTFMaterialData getExistingData();

    @NonNull
    protected abstract NavDirections getShowEditTFMaterialDialogDirections(@NonNull TFMaterialData material, @NonNull int[] materialIndices);

    @NonNull
    protected abstract NavDirections getShowAddTFMaterialDialogDirections(int[] materialIndices);

    protected abstract boolean checkDuplicateUnitId(int input);

    protected abstract void onValidInput(int unitId, @NonNull List<TFMaterialData> materials);

    @NonNull
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddUnitTfMaterialDataBinding.inflate(inflater, container, false);
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this, true)
                .setPositiveButton(new DialogButtonProperties.Builder(android.R.string.ok)
                        .setOnClickListener(() -> {
                            if (adapter.getCurrentList().isEmpty()) {
                                Toast.makeText(requireContext(), R.string.no_materials_error, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final var text = binding.unitIdInput.getText();
                            if (text == null || text.toString().isBlank()) {
                                binding.unitIdInputLayout.setError(getString(R.string.unit_id_empty_error));
                                return;
                            }
                            if (checkDuplicateUnitId(Integer.parseInt(text.toString()))) {
                                binding.unitIdInputLayout.setError(getString(R.string.unit_id_already_exists_error));
                                return;
                            }
                            onValidInput(Integer.parseInt(text.toString()), adapter.getCurrentList());
                            dismiss();
                        })
                        .disableDismissAfterClick()
                        .build())
                .setNegativeButton(DialogButtonProperties.ofOnlyText(android.R.string.cancel))
                .getRootView();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dragDropMaterial.attachToRecyclerView(binding.materialsToAdd);
        final var existingData = getExistingData();
        if (existingData != null) {
            binding.unitIdInput.setText(Integer.toString(existingData.getUnitId()));
            adapter.submitList(existingData.getMaterials());
        }
        if (savedInstanceState != null) {
            adapter.submitList(savedInstanceState.getParcelableArrayList("saved_list"));
        }
        binding.materialsToAdd.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.materialsToAdd.setAdapter(adapter);
        binding.addMaterial.setOnClickListener(v -> {
            if (adapter.getItemCount() == 6) {
                Toast.makeText(requireContext(), R.string.max_materials_error, Toast.LENGTH_SHORT).show();
            } else {
                final var materialData = MyApplication.get(requireContext()).getMaterialData();
                NavHostFragment.findNavController(this).navigate(getShowAddTFMaterialDialogDirections(adapter.getCurrentList().stream().mapToInt(data -> materialData.getMaterials().indexOf(data.getMaterial(materialData))).toArray()));
            }
        });

        EditTFMaterialDialog.registerDataCallback(this, (existingMaterial, newMaterial) -> ListAdapters.replaceElement(adapter, existingMaterial, newMaterial));

        AddTFMaterialDialog.registerDataCallback(this, material -> ListAdapters.addElement(adapter, material));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("saved_list", new ArrayList<>(adapter.getCurrentList()));
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
