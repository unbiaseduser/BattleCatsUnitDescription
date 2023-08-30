package com.sixtyninefourtwenty.bcud.objects.favorites;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

@Database(entities = {FavoriteItem.class, FavoriteReason.class}, version = 1)
@NonNullTypesByDefault
public abstract class FavoritesDatabase extends RoomDatabase {
    public abstract FavoritesDao favoritesDao();
}
