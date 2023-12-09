package com.sixtyninefourtwenty.bcud.objects.favorites;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONObject;

import java.util.Objects;

import lombok.SneakyThrows;

@Entity(tableName = "favorites")
public final class FavoriteItem implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private final int unitId;

    public FavoriteItem(int unitId) {
        this.unitId = unitId;
    }

    public int getUnitId() {
        return unitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteItem that)) return false;
        return unitId == that.unitId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitId);
    }

    public static final JsonSerializer<FavoriteItem> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        @NonNull
        public JSONObject toJson(FavoriteItem obj) {
            return new JSONObject()
                    .put("unit_id", obj.unitId);
        }

        @Override
        @SneakyThrows
        public FavoriteItem fromJson(JSONObject obj) {
            return new FavoriteItem(obj.getInt("unit_id"));
        }
    };

    private FavoriteItem(Parcel in) {
        unitId = in.readInt();
    }

    public static final Creator<FavoriteItem> CREATOR = new Creator<>() {
        @Override
        public FavoriteItem createFromParcel(Parcel in) {
            return new FavoriteItem(in);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
    }
}
