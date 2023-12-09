package com.sixtyninefourtwenty.bcud.ui.fragments.advent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sixtyninefourtwenty.bcud.databinding.FragmentAdventDetailsBinding;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public final class AdventDetailsFragment extends BaseViewBindingFragment<@NonNull FragmentAdventDetailsBinding> {

    @Override
    protected @NonNull FragmentAdventDetailsBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAdventDetailsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull FragmentAdventDetailsBinding binding, @Nullable Bundle savedInstanceState) {
        final var stage = AdventDetailsFragmentArgs.fromBundle(getArguments()).getStage();
        final var markwon = stage.createMarkwon(requireContext(), binding.scrollOneStage);
        markwon.setMarkdown(binding.tvOneStage, stage.readFileContent(requireContext().getAssets()));
        new FastScrollerBuilder(binding.scrollOneStage).build();
        Utils.setupShowHideFabOnScrolls(binding.scrollOneStage, binding.webFab);
        binding.webFab.setOnClickListener(v -> openWebsite(stage.getWikiUrl()));
    }

}