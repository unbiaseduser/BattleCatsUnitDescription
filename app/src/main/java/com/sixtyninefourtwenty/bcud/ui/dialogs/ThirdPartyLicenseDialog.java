package com.sixtyninefourtwenty.bcud.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.GenericTextPageBinding;
import com.sixtyninefourtwenty.bcud.enums.License;
import com.sixtyninefourtwenty.bcud.utils.AssetMarkdownFileParsing;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ThirdPartyLicenseDialog extends BaseViewBindingBottomSheetAlertDialogFragment<@NonNull GenericTextPageBinding> {

    private License license;

    @Override
    protected @NonNull GenericTextPageBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return GenericTextPageBinding.inflate(inflater, container, false);
    }

    @Override
    protected @NonNull View initDialogView(GenericTextPageBinding binding) {
        license = ThirdPartyLicenseDialogArgs.fromBundle(requireArguments()).getLicense();
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this, license.isLicenseLong())
                .setTitle(R.string.license_info)
                .setPositiveButton(DialogButtonProperties.ofOnlyText(R.string.got_it))
                .setNeutralButton(new DialogButtonProperties.Builder(R.string.website)
                        .setOnClickListener(() -> openWebsite(license.getLicenseUrl()))
                        .build())
                .getRootView();
    }

    @Override
    protected void setup(@NonNull GenericTextPageBinding binding, @Nullable Bundle savedInstanceState) {
        final var container = new LifecycleAwareFutureContainer(getViewLifecycleOwner());
        final var markwon = license.createMarkwon(requireContext(), binding.scroll);
        final var future = container.addAndReturn(AssetMarkdownFileParsing.parseFromObject(
                MyApplication.get(requireContext()).getThreadPool(),
                license,
                requireContext(),
                markwon
        ));
        MoreFutures.addIgnoreExceptionsCallback(future,
                spanned -> {
                    Utils.applyBetterLinkMovementMethod(binding.content, this);
                    markwon.setParsedMarkdown(binding.content, spanned);
                    binding.getRoot().setViewState(MultiStateView.ViewState.CONTENT);
                },
                ContextCompat.getMainExecutor(requireContext()));
    }

}
