package com.sixtyninefourtwenty.bcud.ui.fragments;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.util.concurrent.Futures;
import com.kennyc.view.MultiStateView;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.adapters.FavoriteReasonAdapter;
import com.sixtyninefourtwenty.bcud.databinding.FragmentFavoritesBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemFavoriteBinding;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.repository.UnitData;
import com.sixtyninefourtwenty.bcud.ui.activities.MainActivity;
import com.sixtyninefourtwenty.bcud.ui.dialogs.AddReasonDialog;
import com.sixtyninefourtwenty.bcud.ui.dialogs.EditReasonDialog;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.FavoritesBackupRestore;
import com.sixtyninefourtwenty.bcud.utils.MySwipeMenuListener;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingFragment;
import com.sixtyninefourtwenty.bcud.viewmodels.FavoritesDataViewModel;
import com.sixtyninefourtwenty.common.interfaces.BiIntConsumer;
import com.sixtyninefourtwenty.common.interfaces.TriConsumer;
import com.sixtyninefourtwenty.common.utils.FutureCallbackLambdas;
import com.sixtyninefourtwenty.common.utils.ImmutableListCollector;
import com.sixtyninefourtwenty.common.utils.MoreFutures;
import com.sixtyninefourtwenty.customactionmode.AbstractActionMode;
import com.sixtyninefourtwenty.javastuff.concurrent.FutureContainer;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;
import com.sixtyninefourtwenty.materialpopupmenu.builder.ItemBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.PopupMenuBuilder;
import com.sixtyninefourtwenty.materialpopupmenu.builder.SectionBuilder;
import com.sixtyninefourtwenty.stuff.Bundles;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;

import kotlin.collections.CollectionsKt;
import lombok.AllArgsConstructor;

public final class FavoritesFragment extends BaseViewBindingFragment<@NonNull FragmentFavoritesBinding> {
    private FavoritesAdapter adapter;
    private FavoritesDataViewModel favModel;
    private SelectionTracker<Long> selectionTracker;
    private FutureContainer futureContainer;

    private final ActivityResultLauncher<String> importFavorites = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            try {
                final var inputStream = requireContext().getContentResolver().openInputStream(result);
                if (inputStream != null) {
                    final var future = futureContainer.addAndReturn(
                            FavoritesBackupRestore.restoreFavorites(
                                    MyApplication.get(requireContext()).getThreadPool(),
                                    inputStream
                            )
                    );
                    final var f = Futures.transformAsync(future,
                            out -> favModel.addFavorites(out.getItems(), out.getReasons()),
                            ContextCompat.getMainExecutor(requireContext()));
                    Futures.addCallback(f, new FutureCallbackLambdas<>(
                            __ -> showSnackbar(R.string.import_success),
                            t -> new MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(R.string.import_failed)
                                    .setMessage(Arrays.toString(t.getStackTrace()))
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show()
                    ), ContextCompat.getMainExecutor(requireContext()));
                } else {
                    showSnackbar(R.string.cannot_read_file);
                }
            } catch (IOException e) {
                showSnackbar(R.string.cannot_read_file);
            }
        }
    });

    private final ActivityResultLauncher<String> exportFavorites = registerForActivityResult(new ActivityResultContracts.CreateDocument("application/zip"), result -> {
        if (result != null) {
            final var currentList = adapter.getCurrentList();
            final var favorites = currentList.stream()
                    .map(Map.Entry::getKey)
                    .collect(new ImmutableListCollector<>());
            final var favoriteReasons = currentList.stream()
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .collect(new ImmutableListCollector<>());
            FavoritesBackupRestore.backupFavorites(WorkManager.getInstance(requireContext()), result, favorites, favoriteReasons).observe(getViewLifecycleOwner(), workInfo -> {
                switch (workInfo.getState()) {
                    case SUCCEEDED -> showSnackbar(R.string.export_success);
                    case FAILED -> showSnackbar(R.string.export_failed);
                }
            });
        }
    });

    private final MenuProvider menu = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_favorites, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            final int id = menuItem.getItemId();
            if (id == R.id.import_fav) {
                importFavorites.launch("application/zip");
                return true;
            } else if (id == R.id.export_fav) {
                final var list = adapter.getCurrentList();
                if (list.isEmpty()) {
                    showSnackbar(R.string.nothing_to_export);
                } else {
                    exportFavorites.launch("favorites_backup_" + System.currentTimeMillis());
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected @NonNull FragmentFavoritesBinding initBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentFavoritesBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setup(@NonNull FragmentFavoritesBinding binding, @Nullable Bundle savedInstanceState) {
        futureContainer = new LifecycleAwareFutureContainer(getViewLifecycleOwner());
        final var unitData = MyApplication.get(requireContext()).getUnitData();
        favModel = FavoritesDataViewModel.get(this);
        adapter = new FavoritesAdapter(unitData, (item, id) -> {
            if (id == R.id.open_info) {
                navigate(FavoritesFragmentDirections.goToUnitInfoFromFavorites(unitData.getUnitById(item.getUnitId())));
            } else if (id == R.id.delete) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.confirmation)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> MoreFutures.addIgnoreExceptionsCallback(favModel.deleteFavoritesAndReasons(List.of(item)),
                                ignored -> Toast.makeText(requireContext(), R.string.delete_success, Toast.LENGTH_SHORT).show(),
                                ContextCompat.getMainExecutor(requireContext())))
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else if (id == R.id.edit) {
                navigate(FavoritesFragmentDirections.showAddEditFavoritesDialog(unitData.getUnitById(item.getUnitId())));
            }
        }, (adapter, v, reason) -> new PopupMenuBuilder(requireContext(), v)
                .addSection(new SectionBuilder()
                        .addItem(new ItemBuilder(R.string.edit)
                                .setIcon(R.drawable.edit)
                                .setOnSelectListener(() -> navigate(FavoritesFragmentDirections.showEditReasonDialog(reason)))
                                .build())
                        .addItem(new ItemBuilder(R.string.delete)
                                .setIcon(R.drawable.delete)
                                .setOnSelectListener(() -> new MaterialAlertDialogBuilder(requireContext())
                                        .setTitle(R.string.delete)
                                        .setMessage(R.string.confirm_delete)
                                        .setPositiveButton(android.R.string.ok, (dialog, which) -> MoreFutures.addIgnoreExceptionsCallback(favModel.deleteFavoriteReasons(List.of(reason)),
                                                ignored -> showSnackbar(R.string.delete_success),
                                                ContextCompat.getMainExecutor(requireContext())))
                                        .show())
                                .build())
                        .build())
                .build()
                .show());
        binding.list.setAdapter(adapter);
        selectionTracker = new SelectionTracker.Builder<>("selected_favorites",
                binding.list,
                adapter.new KeyProvider(),
                new FavoritesAdapter.DetailsLookup(binding.list),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();
        selectionTracker.onRestoreInstanceState(savedInstanceState);
        adapter.setSelectionTracker(selectionTracker);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<>() {
            @Override
            public void onSelectionChanged() {
                final var actionMode = ((MainActivity) requireActivity()).getActionMode();
                if (!selectionTracker.getSelection().isEmpty()) {
                    actionMode.start(new AbstractActionMode.Callback() {
                        @Override
                        public boolean onMenuItemClicked(@NonNull AbstractActionMode abstractActionMode, @NonNull MenuItem menuItem) {
                            return false;
                        }

                        @Override
                        public void onActionModeFinished(@NonNull AbstractActionMode mode) {
                            selectionTracker.clearSelection();
                        }
                    });
                    actionMode.setTitle(Integer.toString(selectionTracker.getSelection().size()));
                } else {
                    actionMode.finish();
                }
            }
        });
        favModel.getAllFavoritesAndReasons().observe(getViewLifecycleOwner(), map -> {
            final var list = new ArrayList<>(map.entrySet());
            adapter.submitList(list);
            binding.getRoot().setViewState(list.isEmpty() ? MultiStateView.ViewState.EMPTY : MultiStateView.ViewState.CONTENT);
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(EditReasonDialog.CALLBACK_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            final var reason = requireNonNull(Bundles.getParcelableCompat(result, "reason", FavoriteReason.class));
            favModel.updateFavoriteReasons(List.of(reason));
        });
        requireActivity().getSupportFragmentManager().setFragmentResultListener(AddReasonDialog.CALLBACK_KEY, getViewLifecycleOwner(), (requestKey, result) -> {
            final var reason = requireNonNull(Bundles.getParcelableCompat(result, "reason", FavoriteReason.class));
            favModel.addFavoriteReasons(List.of(reason));
        });
        requireActivity().addMenuProvider(menu, getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectionTracker != null) {
            selectionTracker.onSaveInstanceState(outState);
        }
    }

    private static final class FavoritesAdapter extends ListAdapter<Map.Entry<FavoriteItem, List<FavoriteReason>>, FavoritesAdapter.ViewHolder> {
        private final UnitData unitData;
        private final ObjIntConsumer<FavoriteItem> onSwipeActionClick;
        private final RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        private final TriConsumer<FavoriteReasonAdapter, View, FavoriteReason> onReasonLongClick;
        private SelectionTracker<Long> selectionTracker;

        public void setSelectionTracker(@NonNull SelectionTracker<Long> selectionTracker) {
            this.selectionTracker = selectionTracker;
        }

        private static final DiffUtil.ItemCallback<Map.Entry<FavoriteItem, List<FavoriteReason>>> FAV_DIFFER = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(Map.@NonNull Entry<FavoriteItem, List<FavoriteReason>> oldItem, Map.@NonNull Entry<FavoriteItem, List<FavoriteReason>> newItem) {
                return oldItem.getKey().getUnitId() == newItem.getKey().getUnitId();
            }

            @Override
            public boolean areContentsTheSame(Map.@NonNull Entry<FavoriteItem, List<FavoriteReason>> oldItem, Map.@NonNull Entry<FavoriteItem, List<FavoriteReason>> newItem) {
                return oldItem.getKey().equals(newItem.getKey()) && oldItem.getValue().equals(newItem.getValue());
            }
        };

        public FavoritesAdapter(UnitData unitData,
                                ObjIntConsumer<FavoriteItem> onSwipeActionClick,
                                TriConsumer<FavoriteReasonAdapter, View, FavoriteReason> onReasonLongClick) {
            super(FAV_DIFFER);
            this.unitData = unitData;
            this.onSwipeActionClick = onSwipeActionClick;
            this.onReasonLongClick = onReasonLongClick;
        }

        @Override
        public FavoritesFragment.FavoritesAdapter.@NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding, (id, pos) -> onSwipeActionClick.accept(getItem(pos).getKey(), id), pool);
        }

        @Override
        public void onBindViewHolder(FavoritesFragment.FavoritesAdapter.@NonNull ViewHolder holder, int position) {
            final var item = getItem(position);
            final var reasons = item.getValue();
            final var unit = unitData.getUnitById(item.getKey().getUnitId());
            AssetImageLoading.loadAssetImage(holder.binding.unitIcon, unit.getLatestFormIconPath(MyApplication.get(holder.binding.getRoot().getContext()).getUnitExplanationData()));
            holder.binding.unitName.setText(unit.getExplanation(MyApplication.get(holder.binding.getRoot().getContext()).getUnitExplanationData()).getName(Unit.Form.SECOND));

            if (selectionTracker.isSelected((long) item.getKey().getUnitId())) {
                holder.binding.cb.setVisibility(View.VISIBLE);
            } else {
                holder.binding.cb.setVisibility(View.GONE);
            }

            final var existingLayoutMan = holder.binding.reasonsList.getLayoutManager();
            if (existingLayoutMan == null) {
                final var layoutMan = new LinearLayoutManager(holder.itemView.getContext());
                layoutMan.setInitialPrefetchItemCount(reasons.size());
                holder.binding.reasonsList.setLayoutManager(layoutMan);
            } else if (existingLayoutMan instanceof LinearLayoutManager llm) {
                llm.setInitialPrefetchItemCount(reasons.size());
            }

            final var existingAdapter = holder.binding.reasonsList.getAdapter();
            if (existingAdapter == null) {
                final var adapter = new FavoriteReasonAdapter(onReasonLongClick);
                adapter.submitList(reasons);
                holder.binding.reasonsList.setAdapter(adapter);
            } else if (existingAdapter instanceof FavoriteReasonAdapter fra) {
                fra.submitList(reasons);
            }
        }

        @AllArgsConstructor
        public static final class DetailsLookup extends ItemDetailsLookup<Long> {

            private final RecyclerView recyclerView;

            @Nullable
            @Override
            public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
                final var view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    return ((ViewHolder) recyclerView.getChildViewHolder(view)).getItemDetails();
                }
                return null;
            }
        }

        public final class KeyProvider extends ItemKeyProvider<Long> {

            public KeyProvider() {
                super(ItemKeyProvider.SCOPE_CACHED);
            }

            @NonNull
            @Override
            public Long getKey(int position) {
                return (long) getItem(position).getKey().getUnitId();
            }

            @Override
            public int getPosition(@NonNull Long key) {
                return CollectionsKt.indexOfFirst(getCurrentList(), item -> item.getKey().getUnitId() == key);
            }
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ListItemFavoriteBinding binding;

            public ViewHolder(ListItemFavoriteBinding binding,
                              BiIntConsumer onSwipeActionClick,
                              RecyclerView.RecycledViewPool pool) {
                super(binding.getRoot());
                this.binding = binding;
                binding.ll.setOnClickListener(v -> binding.expandReason.toggle());
                binding.sal.setMenuListener((MySwipeMenuListener) (view, swipeAction) -> {
                    final var id = swipeAction.getActionId();
                    if (id == R.id.close) binding.sal.close();
                    else onSwipeActionClick.accept(id, getAbsoluteAdapterPosition());
                });
                binding.reasonsList.setRecycledViewPool(pool);
            }

            public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
                return new ItemDetailsLookup.ItemDetails<>() {
                    @Override
                    public int getPosition() {
                        return getAbsoluteAdapterPosition();
                    }

                    @NonNull
                    @Override
                    public Long getSelectionKey() {
                        final var adapter = requireNonNull((FavoritesAdapter) getBindingAdapter());
                        return (long) adapter.getCurrentList().get(getAbsoluteAdapterPosition()).getKey().getUnitId();
                    }
                };
            }
        }
    }
}
