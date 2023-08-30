package com.sixtyninefourtwenty.bcud.objects.favorites;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.json.JSONObject;

import java.util.Objects;

import lombok.SneakyThrows;

@Entity(tableName = "favorite_reasons",
        indices = {
                @Index(value = "unit_id")
        },
        foreignKeys = {
                @ForeignKey(
                        entity = FavoriteItem.class,
                        parentColumns = {"id"},
                        childColumns = {"unit_id"},
                        onDelete = ForeignKey.CASCADE
                )
        })
public final class FavoriteReason implements Parcelable {

    public FavoriteReason(int uid, int unitId, String reason) {
        this.uid = uid;
        this.unitId = unitId;
        this.reason = reason;
    }

    public int getUid() {
        return uid;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getReason() {
        return reason;
    }

    @PrimaryKey(autoGenerate = true)
    int uid;
    @ColumnInfo(name = "unit_id")
    int unitId;
    String reason;

    @Ignore
    public FavoriteReason(int unitId, String reason) {
        this(0, unitId, reason);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteReason that)) return false;
        return uid == that.uid && unitId == that.unitId && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, unitId, reason);
    }

    public static final JsonSerializer<FavoriteReason> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        public JSONObject toJson(FavoriteReason obj) {
            return new JSONObject()
                    .put("uid", obj.uid)
                    .put("unit_id", obj.unitId)
                    .put("reason", obj.reason);
        }

        @Override
        @SneakyThrows
        public FavoriteReason fromJson(JSONObject obj) {
            return new FavoriteReason(
                    obj.getInt("uid"),
                    obj.getInt("unit_id"),
                    obj.getString("reason")
            );
        }
    };

    private FavoriteReason(Parcel in) {
        uid = in.readInt();
        unitId = in.readInt();
        reason = in.readString();
    }

    public static final Creator<FavoriteReason> CREATOR = new Creator<>() {
        @Override
        public FavoriteReason createFromParcel(Parcel in) {
            return new FavoriteReason(in);
        }

        @Override
        public FavoriteReason[] newArray(int size) {
            return new FavoriteReason[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeInt(unitId);
        dest.writeString(reason);
    }
}
