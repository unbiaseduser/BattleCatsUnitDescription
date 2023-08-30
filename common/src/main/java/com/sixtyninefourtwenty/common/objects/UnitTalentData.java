package com.sixtyninefourtwenty.common.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.json.JSONObject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class UnitTalentData implements Parcelable {

    int unitId;
    Talent.UnitType unitType;
    @With
    List<TalentData> talents;

    public static final JsonSerializer<UnitTalentData> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        public JSONObject toJson(UnitTalentData obj) {
            return new JSONObject()
                    .put("unit_id", obj.unitId)
                    .put("unit_type", obj.unitType.name())
                    .put("talents", TalentData.SERIALIZER.listToJson(obj.talents));
        }

        @Override
        @SneakyThrows
        public UnitTalentData fromJson(JSONObject obj) {
            return new UnitTalentData(
                    obj.getInt("unit_id"),
                    Talent.UnitType.valueOf(obj.getString("unit_type")),
                    TalentData.SERIALIZER.listFromJson(obj.getJSONArray("talents"))
            );
        }
    };

    private UnitTalentData(Parcel in) {
        unitId = in.readInt();
        unitType = (Talent.UnitType) in.readSerializable();
        talents = in.createTypedArrayList(TalentData.CREATOR);
    }

    public static final Creator<UnitTalentData> CREATOR = new Creator<>() {
        @Override
        public UnitTalentData createFromParcel(Parcel in) {
            return new UnitTalentData(in);
        }

        @Override
        public UnitTalentData[] newArray(int size) {
            return new UnitTalentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeSerializable(unitType);
        dest.writeTypedList(talents);
    }
}
