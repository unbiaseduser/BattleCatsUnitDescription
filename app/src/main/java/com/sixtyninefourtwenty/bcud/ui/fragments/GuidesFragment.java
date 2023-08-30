package com.sixtyninefourtwenty.bcud.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.GuidesListAdapter;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class GuidesFragment extends BaseFragment<RecyclerView> {

    private final MenuProvider provider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_guides_top, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.contribute) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/guides/help.html");
                return true;
            }
            return false;
        }
    };

    @Override
    protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new RecyclerView(requireContext());
    }

    @Override
    protected void setup(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
        final var guides = MyApplication.get(requireContext()).getGuideData().getGuides();
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setHasFixedSize(true);
        view.setAdapter(new GuidesListAdapter(guides, pos -> {
            if (!guides.get(pos).getUrl().equals("https://thanksfeanor.pythonanywhere.com/guides/documents/advents.html")) {
                navigate(GuidesFragmentDirections.goToGuideDetail(guides.get(pos)));
            } else {
                navigate(GuidesFragmentDirections.goToGuideAdvent());
            }
        }));
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

}