package com.sixtyninefourtwenty.bcud.ui.fragments.helppinsdetails.xpcost;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.core.view.MenuProvider;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.FragmentXpCostBinding;
import com.sixtyninefourtwenty.bcud.utils.Stuff;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.commonmark.ext.gfm.tables.TableBlock;

import io.noties.markwon.Markwon;
import io.noties.markwon.recycler.MarkwonAdapter;
import io.noties.markwon.recycler.table.TableEntry;
import io.noties.markwon.recycler.table.TableEntryPlugin;

public final class XPCostFragment extends BaseViewBindingFragment<@NonNull FragmentXpCostBinding> {
    private final MenuProvider provider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_guide_other, menu);
            menu.findItem(R.id.go_to_combos).setVisible(false).setEnabled(false);
            menu.findItem(R.id.text_search).setVisible(false).setEnabled(false);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.guide_other_go_to_website) {
                openWebsite("https://thanksfeanor.pythonanywhere.com/xpcurves");
                return true;
            }
            return false;
        }
    };

    @Override
    protected @NonNull FragmentXpCostBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentXpCostBinding.inflate(inflater, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void setup(@NonNull FragmentXpCostBinding binding, @Nullable Bundle savedInstanceState) {
        final var adapter = MarkwonAdapter.builderTextViewIsRoot(R.layout.md_entry)
                .include(TableBlock.class, TableEntry.create(builder -> builder
                        .textLayoutIsRoot(R.layout.md_table_cell)
                        .tableLayout(R.layout.md_table_block, R.id.md_table)))
                .build();
        final var markwon = Markwon.builder(requireContext())
                .usePlugin(TableEntryPlugin.create(requireContext()))
                .build();
        binding.picker.setItemSelectedListener((position) -> {
            switch (position) {
                case 0 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/rare_curve.md", requireContext()));
                case 1 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/super_rare_curve.md", requireContext()));
                case 2 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/uber_curve.md", requireContext()));
                case 3 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/bahamut_curve.md", requireContext()));
                case 4 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_cat_curve.md", requireContext()));
                case 5 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_tank_curve.md", requireContext()));
                case 6 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_axe_curve.md", requireContext()));
                case 7 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_gross_curve.md", requireContext()));
                case 8 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_cow_curve.md", requireContext()));
                case 9 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_bird_curve.md", requireContext()));
                case 10 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_fish_curve.md", requireContext()));
                case 11 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_lizard_curve.md", requireContext()));
                case 12 -> adapter.setMarkdown(markwon, Stuff.getText("text/xp_curves/crazed_titan_curve.md", requireContext()));
            }
            adapter.notifyDataSetChanged();
        });
        binding.picker.setItemStringArrayRes(R.array.xp_curves);
        binding.stuff.setAdapter(adapter);
        Utils.applyBetterLinkMovementMethod(binding.opening, this);
        markwon.setMarkdown(binding.opening, Stuff.getText("text/xp_curves/xp_curve_opening.md", requireContext()));
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
    }

}
