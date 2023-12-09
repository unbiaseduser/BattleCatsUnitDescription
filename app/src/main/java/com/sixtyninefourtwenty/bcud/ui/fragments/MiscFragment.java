package com.sixtyninefourtwenty.bcud.ui.fragments;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.FragmentMiscBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemQuoteBinding;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.utils.UniqueIntRandom;
import com.sixtyninefourtwenty.materialpopupmenu.builder.ItemBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.PopupMenuBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.SectionBuilder;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;

public final class MiscFragment extends BaseViewBindingFragment<@NonNull FragmentMiscBinding> {

    private final UniqueIntRandom intRandom = new UniqueIntRandom();
    private ImmutableList<String> ponosQuotes;

    @Override
    protected @NonNull FragmentMiscBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentMiscBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull FragmentMiscBinding binding, @Nullable Bundle savedInstanceState) {
        ponosQuotes = MyApplication.get(requireContext()).getPonosQuoteData().getQuotes();
        refreshQuote(binding);
        binding.refreshPonosQuote.setOnClickListener(v -> refreshQuote(binding));
        binding.allQuotes.setOnClickListener(v -> getNavController().navigate(MiscFragmentDirections.showPonosQuotesDialog()));

        binding.goToFavoriteUnits.setOnClickListener(v -> navigate(MiscFragmentDirections.actionNavMiscToNavFavorites()));

        binding.randomUnit.setOnClickListener(v -> {
            final var allUnits = MyApplication.get(requireContext()).getUnitData().getAllUnits();
            final var unit = allUnits.get(ThreadLocalRandom.current().nextInt(allUnits.size()));
            navigate(MiscFragmentDirections.actionNavMiscToNavUdpUnitInfo(unit));
        });
    }

    private void refreshQuote(FragmentMiscBinding binding) {
        binding.ponosQuote.setText(ponosQuotes.get(intRandom.nextInt(ponosQuotes.size())));
    }

    public static final class PonosQuotesDialog extends BaseBottomSheetAlertDialogFragment<@NonNull RecyclerView> {

        @NonNull
        @Override
        protected RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
            return new RecyclerView(requireContext());
        }

        @NonNull
        @Override
        protected View initDialogView(@NonNull RecyclerView view) {
            return new BottomSheetAlertDialogFragmentViewBuilder(view, this, true)
                    .setTitle(R.string.ponos_quotes)
                    .setPositiveButton(DialogButtonProperties.ofOnlyText(R.string.got_it))
                    .getRootView();
        }

        @Override
        protected void setup(RecyclerView view, @Nullable Bundle savedInstanceState) {
            final var ponosQuotes = MyApplication.get(requireContext()).getPonosQuoteData().getQuotes();
            view.setLayoutManager(new LinearLayoutManager(requireContext()));
            view.setHasFixedSize(true);
            view.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            view.setAdapter(new PonosQuotesAdapter(ponosQuotes, (v, quote) -> new PopupMenuBuilder(requireContext(), v)
                    .addSection(new SectionBuilder()
                            .addItem(new ItemBuilder(R.string.copy)
                                    .setIcon(R.drawable.content_copy)
                                    .setOnSelectListener(() -> {
                                        getClipboardManager().setPrimaryClip(ClipData.newPlainText("quote", quote));
                                        showToast(R.string.copied_to_clipboard);
                                    })
                                    .build())
                            .addItem(new ItemBuilder(R.string.share)
                                    .setIcon(R.drawable.share)
                                    .setOnSelectListener(() -> startActivity(Intent.createChooser(
                                            new Intent(Intent.ACTION_SEND)
                                                    .putExtra(Intent.EXTRA_TEXT, quote)
                                                    .setType("text/plain")
                                            , null)))
                                    .build())
                            .build())
                    .build()
                    .show()));
        }

    }

    private static final class PonosQuotesAdapter extends RecyclerView.Adapter<PonosQuotesAdapter.ViewHolder> {
        private final List<String> ponosQuotes;
        private final BiConsumer<View, String> onQuoteClickListener;

        public PonosQuotesAdapter(List<String> ponosQuotes, BiConsumer<View, String> onQuoteClickListener) {
            this.ponosQuotes = ponosQuotes;
            this.onQuoteClickListener = onQuoteClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemQuoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding, (view, pos) -> onQuoteClickListener.accept(view, ponosQuotes.get(pos)));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.quoteDisplay.setText(ponosQuotes.get(position));
        }

        @Override
        public int getItemCount() {
            return ponosQuotes.size();
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView quoteDisplay;

            public ViewHolder(ListItemQuoteBinding binding, ObjIntConsumer<View> onQuoteClickListener) {
                super(binding.getRoot());
                binding.buttonMoreQuote.setOnClickListener(v -> onQuoteClickListener.accept(v, getAbsoluteAdapterPosition()));
                quoteDisplay = binding.quote;
            }
        }
    }
}