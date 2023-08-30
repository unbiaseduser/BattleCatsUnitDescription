package com.sixtyninefourtwenty.bcud.objects.favorites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Map;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites LEFT JOIN favorite_reasons ON favorites.id = favorite_reasons.unit_id")
    LiveData<Map<FavoriteItem, List<FavoriteReason>>> getAllFavoritesAndReasons();

    @Query("SELECT * FROM favorites LEFT JOIN favorite_reasons ON favorites.id = favorite_reasons.unit_id")
    ListenableFuture<Map<FavoriteItem, List<FavoriteReason>>> getAllFavoritesAndReasonsSnapshot();

    @Query("select * from favorites where id = :id")
    ListenableFuture<FavoriteItem> findFavoriteByUnitId(int id);

    @Query("SELECT * FROM favorite_reasons WHERE unit_id = :id")
    LiveData<List<FavoriteReason>> getReasonsForFavorite(int id);

    @Query("SELECT * FROM favorite_reasons WHERE unit_id = :id")
    ListenableFuture<List<FavoriteReason>> getReasonsForFavoriteSnapshot(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorites(Iterable<FavoriteItem> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavoriteReasons(Iterable<FavoriteReason> reasons);

    @Delete
    void deleteFavorites(Iterable<FavoriteItem> items);

    @Delete
    void deleteFavoriteReasons(Iterable<FavoriteReason> reasons);

    @Update
    void updateFavoriteReasons(Iterable<FavoriteReason> reasons);

}
