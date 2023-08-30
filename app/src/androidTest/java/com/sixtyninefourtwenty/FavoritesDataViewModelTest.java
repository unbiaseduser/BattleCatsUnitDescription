package com.sixtyninefourtwenty;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.util.concurrent.MoreExecutors;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoritesDatabase;
import com.sixtyninefourtwenty.bcud.repository.FavoritesDataRepository;
import com.sixtyninefourtwenty.bcud.viewmodels.FavoritesDataViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(InstantTaskExecutorExtension.class)
class FavoritesDataViewModelTest {

    private FavoritesDataViewModel viewModel;

    @BeforeEach
    void setup() {
        final var executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        final var context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final var db = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), FavoritesDatabase.class).allowMainThreadQueries().build();
        viewModel = new FavoritesDataViewModel(new FavoritesDataRepository(db), executorService);
    }

    @Test
    void addItemAndReasonsThenLiveDataContainsValue() throws Exception {
        final var item = new FavoriteItem(24);
        final var reason = new FavoriteReason(1,24, "Something");
        viewModel.addFavorite(item, List.of(reason)).get();
        final var resultMapRef = new AtomicReference<Map<FavoriteItem, List<FavoriteReason>>>();
        final Observer<Map<FavoriteItem, List<FavoriteReason>>> dataObserver = resultMapRef::set;
        viewModel.getAllFavoritesAndReasons().observeForever(dataObserver);
        await().until(() -> resultMapRef.get() != null);
        final var resultMap = resultMapRef.get();
        assertNotNull(resultMap);
        assertIterableEquals(List.of(reason), resultMap.get(item));
        viewModel.getAllFavoritesAndReasons().removeObserver(dataObserver);
    }



}
