package com.sixtyninefourtwenty.bcud.objects;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ScrollView;

import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.bcud.utils.HasAssetMarkdownFile;
import com.sixtyninefourtwenty.bcud.utils.builders.MarkwonBuilderWrapper;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.noties.markwon.Markwon;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
@Immutable
public class AdventStage implements Parcelable, HasAssetMarkdownFile {

    int bossId;
    String img;
    String name;
    String filePath;
    String wikiUrl;

    private AdventStage(Parcel in) {
        bossId = in.readInt();
        img = in.readString();
        name = in.readString();
        filePath = in.readString();
        wikiUrl = in.readString();
    }

    public static final Creator<AdventStage> CREATOR = new Creator<>() {
        @Override
        public AdventStage createFromParcel(Parcel in) {
            return new AdventStage(in);
        }

        @Override
        public AdventStage[] newArray(int size) {
            return new AdventStage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(bossId);
        dest.writeString(img);
        dest.writeString(name);
        dest.writeString(filePath);
        dest.writeString(wikiUrl);
    }

    @Override
    public Markwon createMarkwon(Context context, ScrollView sv) {
        return new MarkwonBuilderWrapper(context)
                .images()
                .html()
                .build();
    }
}
