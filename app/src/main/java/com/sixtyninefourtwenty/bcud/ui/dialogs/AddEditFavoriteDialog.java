package com.sixtyninefourtwenty.bcud.ui.dialogs;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.FavoriteReasonAdapter;
import com.sixtyninefourtwenty.bcud.databinding.DialogAddEditFavoriteBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingBottomSheetAlertDialogFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.FavoritesDataViewModel;
import com.sixtyninefourtwenty.bottomsheetalertdialog.BottomSheetAlertDialogFragmentViewBuilder;
import com.sixtyninefourtwenty.bottomsheetalertdialog.DialogButtonProperties;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.materialpopupmenu.builder.ItemBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.PopupMenuBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.SectionBuilder;
import com.sixtyninefourtwenty.stuff.Bundles;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class AddEditFavoriteDialog extends BaseViewBindingBottomSheetAlertDialogFragment<DialogAddEditFavoriteBinding> {
    private boolean isUpdate = false;
    private Unit unit;
    private final FavoriteReasonAdapter adapter = new FavoriteReasonAdapter((adapter, v, reason) -> new PopupMenuBuilder(requireContext(), v)
            .addSection(new SectionBuilder()
                    .addItem(new ItemBuilder(R.string.edit)
                            .setIcon(R.drawable.edit)
                            .setOnSelectListener(() -> navigate(AddEditFavoriteDialogDirections.showEditReasonDialog(reason)))
                            .build())
                    .addItem(new ItemBuilder(R.string.delete)
                            .setIcon(R.drawable.delete)
                            .setOnSelectListener(() -> new MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(R.string.delete)
                                    .setMessage(R.string.confirm_delete)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        updateList(list -> list.remove(reason));
                                        showToast(R.string.delete_success);
                                    })
                                    .show())
                            .build())
                    .build())
            .build()
            .show());

    @Override
    protected @NonNull DialogAddEditFavoriteBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return DialogAddEditFavoriteBinding.inflate(inflater, container, false);
    }

    @NonNull
    @Override
    protected View initDialogView(DialogAddEditFavoriteBinding binding) {
        return new BottomSheetAlertDialogFragmentViewBuilder(binding.getRoot(), this)
                .setPositiveButton(new DialogButtonProperties.Builder(android.R.string.ok)
                        .setOnClickListener(() -> {
                            if (binding.getRoot().getViewState() == MultiStateView.ViewState.CONTENT) {
                                MoreFutures.addIgnoreExceptionsCallback(FavoritesDataViewModel.get(this).addFavorite(new FavoriteItem(unit.getId()), adapter.getCurrentList()),
                                        ignored -> {
                                            showToast(isUpdate ? R.string.update_favorite_success : R.string.add_favorite_success);
                                            dismiss();
                                        }, ContextCompat.getMainExecutor(requireContext()));
                            }
                        })
                        .disableDismissAfterClick()
                        .build())
                .setNegativeButton(new DialogButtonProperties.Builder(android.R.string.cancel)
                        .build())
                .getRootView();
    }

    @Override
    protected void setup(@NonNull DialogAddEditFavoriteBinding binding, @Nullable Bundle savedInstanceState) {
        final var model = FavoritesDataViewModel.get(this);
        unit = AddEditFavoriteDialogArgs.fromBundle(requireArguments()).getUnit();
        AssetImageLoading.loadAssetImage(binding.unitIconAlt, unit.getLatestFormIconPath(MyApplication.get(requireContext()).getUnitExplanationData()));
        binding.unitNameAlt.setText(unit.getExplanation(MyApplication.get(requireContext()).getUnitExplanationData()).getName(Unit.Form.FIRST));
        binding.reasonsList.setAdapter(adapter);
        binding.addReason.setOnClickListener(v -> navigate(AddEditFavoriteDialogDirections.showAddReasonDialog(unit.getId())));
        MoreFutures.addIgnoreExceptionsCallback(model.findFavoriteByUnitId(unit.getId()), result -> {
            if (result != null) {
                isUpdate = true;
                MoreFutures.addIgnoreExceptionsCallback(model.getReasonsForFavoriteSnapshot(result.getUnitId()), adapter::submitList, ContextCompat.getMainExecutor(requireContext()));
            }
            binding.getRoot().setViewState(MultiStateView.ViewState.CONTENT);
        }, ContextCompat.getMainExecutor(requireContext()));
        requireActivity().getSupportFragmentManager().setFragmentResultListener(AddReasonDialog.CALLBACK_KEY, this, (requestKey, result) -> {
            final var reason = requireNonNull(Bundles.getParcelableCompat(result, "reason", FavoriteReason.class));
            updateList(list -> list.add(reason));
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(EditReasonDialog.CALLBACK_KEY, this, (requestKey, result) -> {
            final var reason = requireNonNull(Bundles.getParcelableCompat(result, "reason", FavoriteReason.class));
            updateList(list -> {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUid() == reason.getUid()) {
                        list.set(i, reason);
                        break;
                    }
                }
            });
        });
    }

    private void updateList(Consumer<List<FavoriteReason>> mutations) {
        final var list = new ArrayList<>(adapter.getCurrentList());
        mutations.accept(list);
        adapter.submitList(list);
    }

}
