package com.sixtyninefourtwenty.bcud.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
@Immutable
public class AdventBoss implements Parcelable {

    @Getter(AccessLevel.NONE)
    int bossId;
    String name;
    String info;
    String wikiUrl;
    ImmutableList<AdventStage> stages;

    public String getIconPath() {
        return "img/advent_boss/edi_" + bossId + ".png";
    }

    private AdventBoss(Parcel in) {
        bossId = in.readInt();
        name = in.readString();
        info = in.readString();
        wikiUrl = in.readString();
        stages = ImmutableList.copyOf(in.createTypedArrayList(AdventStage.CREATOR));
    }

    public static final Creator<AdventBoss> CREATOR = new Creator<>() {
        @Override
        public AdventBoss createFromParcel(Parcel in) {
            return new AdventBoss(in);
        }

        @Override
        public AdventBoss[] newArray(int size) {
            return new AdventBoss[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(bossId);
        dest.writeString(name);
        dest.writeString(info);
        dest.writeString(wikiUrl);
        dest.writeTypedList(stages);
    }

}
