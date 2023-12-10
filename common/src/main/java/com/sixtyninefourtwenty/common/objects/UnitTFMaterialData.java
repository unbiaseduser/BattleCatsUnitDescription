package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.json.JSONObject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class UnitTFMaterialData implements Parcelable {

    int unitId;
    @With
    List<TFMaterialData> materials;

    public static final JsonSerializer<UnitTFMaterialData> SERIALIZER = new JsonSerializer<>() {
        @NonNull
        @Override
        @SneakyThrows
        public JSONObject toJson(@NonNull UnitTFMaterialData obj) {
            return new JSONObject()
                    .put("unit_id", obj.unitId)
                    .put("materials", TFMaterialData.SERIALIZER.listToJson(obj.materials));
        }

        @Override
        @SneakyThrows
        public UnitTFMaterialData fromJson(@NonNull JSONObject obj) {
            return new UnitTFMaterialData(
                    obj.getInt("unit_id"),
                    TFMaterialData.SERIALIZER.listFromJson(obj.getJSONArray("materials"))
            );
        }
    };

    private UnitTFMaterialData(Parcel in) {
        unitId = in.readInt();
        materials = requireNonNull(in.createTypedArrayList(TFMaterialData.CREATOR));
    }

    public static final Creator<UnitTFMaterialData> CREATOR = new Creator<>() {
        @Override
        public UnitTFMaterialData createFromParcel(Parcel in) {
            return new UnitTFMaterialData(in);
        }

        @Override
        public UnitTFMaterialData[] newArray(int size) {
            return new UnitTFMaterialData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeTypedList(materials);
    }
}
