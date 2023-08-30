package com.sixtyninefourtwenty.bcud.repository;

import androidx.lifecycle.LiveData;

import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoritesDao;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoritesDatabase;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.Delegate;

@NonNullTypesByDefault
public final class FavoritesDataRepository {

    @Delegate
    private final FavoritesDao dao;
    @Getter
    private final LiveData<Map<FavoriteItem, List<FavoriteReason>>> allFavoritesAndReasons;

    public FavoritesDataRepository(FavoritesDatabase db) {
        this(db.favoritesDao());
    }

    public FavoritesDataRepository(FavoritesDao dao) {
        this.dao = dao;
        allFavoritesAndReasons = dao.getAllFavoritesAndReasons();
    }

}
