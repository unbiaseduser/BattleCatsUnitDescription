package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.hypermaxpriority;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.databinding.GenericTextPageBinding;
import com.sixtyninefourtwenty.bcud.utils.AssetMarkdownFileParsing;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.builders.MarkwonBuilderWrapper;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class HypermaxPriorityTabTextsFragment extends BaseViewBindingFragment<GenericTextPageBinding> {

    @Override
    protected @NonNull GenericTextPageBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return GenericTextPageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull GenericTextPageBinding binding, @Nullable Bundle savedInstanceState) {
        final var markwon = new MarkwonBuilderWrapper(requireContext()).images().html().build();
        final var container = new LifecycleAwareFutureContainer(getViewLifecycleOwner());
        final var future = container.addAndReturn(AssetMarkdownFileParsing.parseArbitraryFile(
                MyApplication.get(requireContext()).getThreadPool(),
                requireContext(),
                "text/help_pins/hypermax_priority_intro.md",
                markwon
        ));
        MoreFutures.addIgnoreExceptionsCallback(future,
                spanned -> {
                    markwon.setParsedMarkdown(binding.content, spanned);
                    Utils.applyBetterLinkMovementMethod(binding.content, this);
                    binding.getRoot().setViewState(MultiStateView.ViewState.CONTENT);
                },
                ContextCompat.getMainExecutor(requireContext()));
    }

}