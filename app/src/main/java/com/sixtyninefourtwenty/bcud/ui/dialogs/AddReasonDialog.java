package com.sixtyninefourtwenty.bcud.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.DialogAddEditReasonBinding;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.stuff.Bundles;
import com.sixtyninefourtwenty.stuff.Views;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class AddReasonDialog extends BaseViewBindingBottomSheetAlertDialogFragment<DialogAddEditReasonBinding> {
    public static final String CALLBACK_KEY = "add_reason_callback";

    @Override
    protected @NonNull DialogAddEditReasonBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DialogAddEditReasonBinding.inflate(inflater, container, false);
    }

    @Override
    protected @NonNull View initDialogView(@NonNull DialogAddEditReasonBinding binding) {
        final var args = AddReasonDialogArgs.fromBundle(requireArguments());
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this)
                .setTitle(R.string.reason)
                .setPositiveButton(new DialogButtonProperties.Builder(android.R.string.ok)
                        .setOnClickListener(() -> {
                            if (Views.isBlank(binding.reasonInput)) {
                                binding.reasonInputLayout.setError(getString(R.string.please_input_reason));
                            } else {
                                requireActivity().getSupportFragmentManager().setFragmentResult(
                                        CALLBACK_KEY,
                                        Bundles.createBundle(bundle -> bundle.putParcelable("reason", new FavoriteReason(args.getUnitId(), Views.getInput(binding.reasonInput))))
                                );
                                dismiss();
                            }
                        })
                        .disableDismissAfterClick()
                        .build())
                .getRootView();
    }

    @Override
    protected void setup(@NonNull DialogAddEditReasonBinding binding, @Nullable Bundle savedInstanceState) {

    }
}
