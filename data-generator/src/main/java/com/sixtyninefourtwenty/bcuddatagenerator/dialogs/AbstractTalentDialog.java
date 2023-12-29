package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sixtyninefourtwenty.bcuddatagenerator.MyApplication;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.DialogAddTalentBinding;
import com.sixtyninefourtwenty.common.R;
import com.sixtyninefourtwenty.common.misc.NoAutoDismissAlertDialogBuilder;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.utils.SpinnerAdapters;

public abstract class AbstractTalentDialog extends AppCompatDialogFragment {

    private DialogAddTalentBinding binding;

    @StringRes
    protected abstract int getTitle();

    @Nullable
    protected abstract TalentData getExistingTalent();

    protected abstract boolean checkDuplicateTalentIndex(int index);

    protected abstract void onValidInput(@NonNull TalentData newData);

    @NonNull
    @Override
    public final Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogAddTalentBinding.inflate(getLayoutInflater());
        final var app = MyApplication.get(requireContext());
        final var talents = app.getTalentData().getTalents();
        binding.talentPicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), talents.stream()
                .map(talent -> talent.getInfo(app.getTalentInfo()).getAbilityName())
                .toArray(String[]::new)));
        binding.talentPriorityPicker.setAdapter(SpinnerAdapters.createWithAndroidResources(requireContext(), getResources().getStringArray(R.array.talent_priorities)));
        return new NoAutoDismissAlertDialogBuilder(requireContext())
                .setTitle(getTitle())
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, false, (dialog, which) -> {
                    if (checkDuplicateTalentIndex(binding.talentPicker.getSelectedItemPosition())) {
                        Toast.makeText(requireContext(), com.sixtyninefourtwenty.bcuddatagenerator.R.string.talent_already_exists_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    onValidInput(new TalentData(
                            talents.get(binding.talentPicker.getSelectedItemPosition()).getIndex(),
                            Talent.Priority.values()[binding.talentPriorityPicker.getSelectedItemPosition()]
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
