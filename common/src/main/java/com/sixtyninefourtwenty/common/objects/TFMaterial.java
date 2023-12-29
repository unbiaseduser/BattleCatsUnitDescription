package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialInfoSupplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public class TFMaterial implements Parcelable {
    int index;
    String pathToIcon;

    public TFMaterial.Info getInfo(TFMaterialInfoSupplier supplier) {
        return supplier.getInfo(index);
    }

    private TFMaterial(Parcel in) {
        index = in.readInt();
        pathToIcon = requireNonNull(in.readString());
    }

    public static final Parcelable.Creator<TFMaterial> CREATOR = new Parcelable.Creator<>() {
        @Override
        public TFMaterial createFromParcel(Parcel in) {
            return new TFMaterial(in);
        }

        @Override
        public TFMaterial[] newArray(int size) {
            return new TFMaterial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(pathToIcon);
    }

    @Value
    public static class Info {
        String name;
        String explanation;
    }

}
