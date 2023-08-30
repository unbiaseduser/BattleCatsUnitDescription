package com.sixtyninefourtwenty.common.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class UnitHypermaxData implements Parcelable {

    int unitId;
    Hypermax.Priority priority;
    Hypermax.UnitType type;

    public static final JsonSerializer<UnitHypermaxData> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        public JSONObject toJson(UnitHypermaxData obj) {
            return new JSONObject()
                    .put("unit_id", obj.unitId)
                    .put("priority", obj.priority.name())
                    .put("type", obj.type.name());
        }

        @Override
        @SneakyThrows
        public UnitHypermaxData fromJson(JSONObject obj) {
            return new UnitHypermaxData(
                    obj.getInt("unit_id"),
                    Hypermax.Priority.valueOf(obj.getString("priority")),
                    Hypermax.UnitType.valueOf(obj.getString("type"))
            );
        }
    };

    private UnitHypermaxData(Parcel in) {
        unitId = in.readInt();
        priority = (Hypermax.Priority) in.readSerializable();
        type = (Hypermax.UnitType) in.readSerializable();
    }

    public static final Creator<UnitHypermaxData> CREATOR = new Creator<>() {
        @Override
        public UnitHypermaxData createFromParcel(Parcel in) {
            return new UnitHypermaxData(in);
        }

        @Override
        public UnitHypermaxData[] newArray(int size) {
            return new UnitHypermaxData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeSerializable(priority);
        dest.writeSerializable(type);
    }
}
