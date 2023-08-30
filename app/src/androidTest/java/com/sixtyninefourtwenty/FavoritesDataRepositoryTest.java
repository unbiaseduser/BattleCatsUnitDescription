package com.sixtyninefourtwenty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoritesDatabase;
import com.sixtyninefourtwenty.bcud.repository.FavoritesDataRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
class FavoritesDataRepositoryTest {

    private FavoritesDataRepository repository;

    @BeforeEach
    void setup() {
        final var context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final var db = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), FavoritesDatabase.class).allowMainThreadQueries().build();
        repository = new FavoritesDataRepository(db);
    }

    @Test
    void addItemWithNoReasons() throws Exception {
        final var item = new FavoriteItem(24);
        repository.addFavorites(Collections.singleton(item));
        final var data = repository.getAllFavoritesAndReasonsSnapshot().get();
        assertTrue(data.containsKey(item));
        final var associatedReasons = data.get(item);
        assertNotNull(associatedReasons);
        assertTrue(associatedReasons.isEmpty());
    }

    @Test
    void addReasonWithoutItem() {
        final var reason = new FavoriteReason(24, "");
        assertThrows(SQLiteConstraintException.class, () -> repository.addFavoriteReasons(List.of(reason)));
    }

    @Test
    void addItemAndReason() throws Exception {
        final var item = new FavoriteItem(24);
        repository.addFavorites(List.of(item));
        final var reason = new FavoriteReason(1, 24, "");
        repository.addFavoriteReasons(List.of(reason));
        final var data = repository.getAllFavoritesAndReasonsSnapshot().get();
        final var foundReasons = data.get(item);
        assertNotNull(foundReasons);
        assertTrue(foundReasons.contains(reason));
        assertEquals(1, foundReasons.size());
    }

    @Test
    void addItemAndReasonAndDeleteItemThenReasonsAreDeletedToo() throws Exception {
        final var item = new FavoriteItem(24);
        repository.addFavorites(List.of(item));
        final var reason = new FavoriteReason(1, 24, "");
        repository.addFavoriteReasons(List.of(reason));
        final var reasonsAfterAddingItem = repository.getReasonsForFavoriteSnapshot(24).get();
        assertEquals(1, reasonsAfterAddingItem.size());
        repository.deleteFavorites(List.of(item));
        final var reasonsAfterDeleteItem = repository.getReasonsForFavoriteSnapshot(24).get();
        assertTrue(reasonsAfterDeleteItem.isEmpty());
    }

}
