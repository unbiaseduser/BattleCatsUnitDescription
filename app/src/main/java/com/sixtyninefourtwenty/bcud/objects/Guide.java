package com.sixtyninefourtwenty.bcud.objects;

import static java.util.Objects.requireNonNull;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ScrollView;

import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.bcud.utils.HasAssetMarkdownFile;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.noties.markwon.Markwon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
@Immutable
public class Guide implements Parcelable, HasAssetMarkdownFile {

    String title;
    @Getter(AccessLevel.NONE)
    String fileToLoad;
    String url;

    private Guide(Parcel in) {
        title = requireNonNull(in.readString());
        fileToLoad = requireNonNull(in.readString());
        url = requireNonNull(in.readString());
    }

    public static final Creator<Guide> CREATOR = new Creator<>() {
        @Override
        public Guide createFromParcel(Parcel in) {
            return new Guide(in);
        }

        @Override
        public Guide[] newArray(int size) {
            return new Guide[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(fileToLoad);
        dest.writeString(url);
    }

    @Override
    public String getFilePath() {
        return fileToLoad;
    }

    @Override
    public Markwon createMarkwon(Context context, ScrollView sv) {
        return Utils.getMarkwonAllPlugins(context, sv);
    }
}
