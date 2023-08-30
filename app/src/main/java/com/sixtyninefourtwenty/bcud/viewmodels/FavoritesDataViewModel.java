package com.sixtyninefourtwenty.bcud.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.repository.FavoritesDataRepository;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.javastuff.concurrent.FutureContainer;
import com.sixtyninefourtwenty.javastuff.concurrent.ViewModelFutureContainer;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;

@NonNullTypesByDefault
public final class FavoritesDataViewModel extends ViewModel {

    private final ListeningExecutorService insertUpdateDeleteExecutor;
    private final FavoritesDataRepository repository;
    private final FutureContainer container = new ViewModelFutureContainer(this);

    public FavoritesDataViewModel(FavoritesDataRepository repository, ListeningExecutorService insertUpdateDeleteExecutor) {
        this.repository = repository;
        this.insertUpdateDeleteExecutor = insertUpdateDeleteExecutor;
    }

    public LiveData<Map<FavoriteItem, List<FavoriteReason>>> getAllFavoritesAndReasons() {
        return repository.getAllFavoritesAndReasons();
    }

    private ListenableFuture<?> createFutureAndReturn(Runnable r) {
        return container.addAndReturn(insertUpdateDeleteExecutor.submit(r));
    }

    public ListenableFuture<@Nullable FavoriteItem> findFavoriteByUnitId(int id) {
        return container.addAndReturn(repository.findFavoriteByUnitId(id));
    }

    public ListenableFuture<List<FavoriteReason>> getReasonsForFavoriteSnapshot(int id) {
        return container.addAndReturn(repository.getReasonsForFavoriteSnapshot(id));
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> addFavorite(FavoriteItem item, Iterable<FavoriteReason> reasons) {
        return createFutureAndReturn(() -> {
            repository.addFavorites(List.of(item));
            repository.addFavoriteReasons(reasons);
        });
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> addFavorites(Iterable<FavoriteItem> items, Iterable<FavoriteReason> reasons) {
        return createFutureAndReturn(() -> {
            repository.addFavorites(items);
            repository.addFavoriteReasons(reasons);
        });
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> addFavoriteReasons(Iterable<FavoriteReason> reasons) {
        return createFutureAndReturn(() -> repository.addFavoriteReasons(reasons));
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> updateFavoriteReasons(Iterable<FavoriteReason> reasons) {
        return createFutureAndReturn(() -> repository.updateFavoriteReasons(reasons));
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> deleteFavoriteReasons(Iterable<FavoriteReason> reasons) {
        return createFutureAndReturn(() -> repository.deleteFavoriteReasons(reasons));
    }

    @CanIgnoreReturnValue
    public ListenableFuture<?> deleteFavoritesAndReasons(Iterable<FavoriteItem> items) {
        return createFutureAndReturn(() -> repository.deleteFavorites(items));
    }

    public static final ViewModelInitializer<FavoritesDataViewModel> INITIALIZER = new ViewModelInitializer<>(FavoritesDataViewModel.class, creationExtras -> {
        final var application = creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
        assert application != null;
        return new FavoritesDataViewModel(((MyApplication) application).getFavoritesDataRepository(), ((MyApplication) application).getThreadPool());
    });

    public static FavoritesDataViewModel get(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, ViewModelProvider.Factory.from(INITIALIZER)).get(FavoritesDataViewModel.class);
    }

}
