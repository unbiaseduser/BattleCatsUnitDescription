package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

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
import com.sixtyninefourtwenty.bcuddatagenerator.adapters.TalentDataAdapter;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddUnitTalentDataBinding;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitTalentData;
import com.sixtyninefourtwenty.common.utils.Formatting;
import com.sixtyninefourtwenty.common.utils.RecyclerViews;
import com.sixtyninefourtwenty.common.utils.SpinnerAdapters;
import com.sixtyninefourtwenty.stuff.ListAdapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractUnitTalentDataDialog extends BottomSheetDialogFragment {

    private DialogAddUnitTalentDataBinding binding;

    private final ItemTouchHelper dragDropTalents = RecyclerViews.createVerticalDragDropTouchHelper((oldPos, newPos) ->
            ListAdapters.modifyList((TalentDataAdapter) binding.talentsToAdd.getAdapter(), list -> Collections.swap(list, oldPos, newPos))
    );

    private final TalentDataAdapter adapter = new TalentDataAdapter(
            (talent, a) -> {
                final var talentData = MyApplication.get(requireContext()).getTalentData();
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.delete)
                        .setMessage(getString(R.string.delete_item_confirmation, talent.getTalent(talentData).getInfo(MyApplication.get(requireContext()).getTalentInfo()).getAbilityName()))
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> ListAdapters.removeElement(a, talent))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            },
            (talent, a) -> {
                final var talentData = MyApplication.get(requireContext()).getTalentData();
                NavHostFragment.findNavController(this).navigate(getShowEditTalentDialogDirections(talent, a.getCurrentList().stream().mapToInt(t -> talentData.getTalents().indexOf(t.getTalent(talentData))).toArray()));
            },
            dragDropTalents::startDrag
    );

    @Nullable
    protected abstract UnitTalentData getExistingData();

    @NonNull
    protected abstract NavDirections getShowEditTalentDialogDirections(@NonNull TalentData talent, @NonNull int[] talentIndices);

    @NonNull
    protected abstract NavDirections getShowAddTalentDialogDirections(@NonNull int[] talentIndices);

    protected abstract boolean checkDuplicateUnitId(int input);

    protected abstract void onValidInput(int unitId, int selectedUnitTypePosition, @NonNull List<TalentData> talents);

    @NonNull
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddUnitTalentDataBinding.inflate(inflater, container, false);
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
                            onValidInput(Integer.parseInt(text.toString()), binding.unitTypePicker.getSelectedItemPosition(), adapter.getCurrentList());
                            dismiss();
                        })
                        .disableDismissAfterClick()
                        .build())
                .setNegativeButton(DialogButtonProperties.ofOnlyText(android.R.string.cancel))
                .getRootView();
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dragDropTalents.attachToRecyclerView(binding.talentsToAdd);
        final var existingData = getExistingData();
        if (existingData != null) {
            binding.unitIdInput.setText(Formatting.formatANumber(requireContext(), existingData.getUnitId()));
            adapter.submitList(existingData.getTalents());
        }
        if (savedInstanceState != null) {
            adapter.submitList(savedInstanceState.getParcelableArrayList("saved_list"));
        }
        binding.unitTypePicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), com.sixtyninefourtwenty.common.R.array.talent_priority_unit_types));
        binding.talentsToAdd.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.talentsToAdd.setAdapter(adapter);
        binding.addTalent.setOnClickListener(v -> {
            if (adapter.getItemCount() == 6) {
                Toast.makeText(requireContext(), R.string.max_talents_error, Toast.LENGTH_SHORT).show();
            } else {
                final var talentData = MyApplication.get(requireContext()).getTalentData();
                NavHostFragment.findNavController(this).navigate(getShowAddTalentDialogDirections(adapter.getCurrentList().stream().mapToInt(d -> talentData.getTalents().indexOf(d.getTalent(talentData))).toArray()));
            }
        });

        EditTalentDialog.registerDataCallback(this, (existingTalent, newTalent) -> ListAdapters.replaceElement(adapter, existingTalent, newTalent));

        AddTalentDialog.registerDataCallback(this, talent -> ListAdapters.addElement(adapter, talent));

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
