package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class UnitEEPriorityData implements Parcelable {
    int unitId;
    ElderEpic elderEpic;
    String text;

    public static final JsonSerializer<UnitEEPriorityData> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        @NonNull
        public JSONObject toJson(UnitEEPriorityData obj) {
            return new JSONObject()
                    .put("unit_id", obj.unitId)
                    .put("elder_epic", obj.elderEpic.name())
                    .put("text", obj.text);
        }

        @Override
        @SneakyThrows
        public UnitEEPriorityData fromJson(JSONObject obj) {
            return new UnitEEPriorityData(
                    obj.getInt("unit_id"),
                    ElderEpic.valueOf(obj.getString("elder_epic")),
                    obj.getString("text")
            );
        }
    };

    private UnitEEPriorityData(Parcel in) {
        unitId = in.readInt();
        elderEpic = (ElderEpic) requireNonNull(in.readSerializable());
        text = requireNonNull(in.readString());
    }

    public static final Creator<UnitEEPriorityData> CREATOR = new Creator<>() {
        @Override
        public UnitEEPriorityData createFromParcel(Parcel in) {
            return new UnitEEPriorityData(in);
        }

        @Override
        public UnitEEPriorityData[] newArray(int size) {
            return new UnitEEPriorityData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeSerializable(elderEpic);
        dest.writeString(text);
    }
}
