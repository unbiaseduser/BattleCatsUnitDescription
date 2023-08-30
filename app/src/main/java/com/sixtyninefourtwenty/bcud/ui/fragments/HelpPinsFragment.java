package com.sixtyninefourtwenty.bcud.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.adapters.GuidesListAdapter;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class HelpPinsFragment extends BaseFragment<RecyclerView> {

    @Override
    protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new RecyclerView(requireContext());
    }

    @Override
    protected void setup(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
        final var helpPins = MyApplication.get(requireContext()).getGuideData().getHelpPins();
        final var adapter = new GuidesListAdapter(helpPins, pos -> {
            switch (pos) {
                case 0 -> getNavController().navigate(HelpPinsFragmentDirections.goToTalentPriority());
                case 1 -> getNavController().navigate(HelpPinsFragmentDirections.goToHypermaxPriority());
                case 2 -> getNavController().navigate(HelpPinsFragmentDirections.goToXpCost());
                case 3 -> getNavController().navigate(HelpPinsFragmentDirections.goToElderEpic());
            }
        });
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setHasFixedSize(true);
        view.setAdapter(adapter);
    }

}