package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.repository.TalentInfoSupplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public class Talent implements Parcelable {

    int index;
    String pathToIcon;

    public Talent.Info getInfo(TalentInfoSupplier supplier) {
        return supplier.getInfo(index);
    }

    private Talent(Parcel in) {
        index = in.readInt();
        pathToIcon = requireNonNull(in.readString());
    }

    public static final Parcelable.Creator<Talent> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Talent createFromParcel(Parcel in) {
            return new Talent(in);
        }

        @Override
        public Talent[] newArray(int size) {
            return new Talent[size];
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

    @Getter
    @AllArgsConstructor
    public enum Priority {
        TOP(R.string.top_priority), HIGH(R.string.high_priority), MID(R.string.medium_priority), LOW(R.string.low_priority), DONT(R.string.do_not_unlock);

        @StringRes
        private final int text;
    }

    public enum UnitType {
        NON_UBER, UBER
    }

    @Value
    public static class Info {
        String abilityName;
        String abilityExplanation;
    }

}
