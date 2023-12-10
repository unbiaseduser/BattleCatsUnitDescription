package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class TFMaterialData implements Parcelable {

    TFMaterial material;
    @IntRange(from = 1)
    int quantity;

    public static final JsonSerializer<TFMaterialData> SERIALIZER = new JsonSerializer<>() {
        @NonNull
        @Override
        @SneakyThrows
        public JSONObject toJson(@NonNull TFMaterialData obj) {
            return new JSONObject()
                    .put("material_index", obj.material.getIndex())
                    .put("quantity", obj.quantity);
        }

        @NonNull
        @Override
        @SneakyThrows
        public TFMaterialData fromJson(@NonNull JSONObject obj) {
            return new TFMaterialData(
                    TFMaterial.fromIndex(obj.getInt("material_index")),
                    obj.getInt("quantity")
            );
        }
    };

    private TFMaterialData(Parcel in) {
        material = (TFMaterial) requireNonNull(in.readSerializable());
        quantity = in.readInt();
    }

    public static final Creator<TFMaterialData> CREATOR = new Creator<>() {
        @Override
        public TFMaterialData createFromParcel(Parcel in) {
            return new TFMaterialData(in);
        }

        @Override
        public TFMaterialData[] newArray(int size) {
            return new TFMaterialData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(material);
        dest.writeInt(quantity);
    }
}
