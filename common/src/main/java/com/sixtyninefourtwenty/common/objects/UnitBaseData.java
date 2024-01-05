package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class UnitBaseData implements Parcelable {

    int unitId;
    Type type;
    @Getter(AccessLevel.NONE)
    Map<Info, String> miscInfoTexts = new EnumMap<>(Info.class);

    public static final JsonSerializer<UnitBaseData> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        public JSONObject toJson(UnitBaseData obj) {
            final var result = new JSONObject()
                    .put("unit_id", obj.unitId)
                    .put("type", obj.type.name());
            final var miscInfoTextObj = new JSONObject();
            for (final var miscInfo : Info.values()) {
                final var text = obj.getTextForInfo(miscInfo);
                if (text != null) {
                    miscInfoTextObj.put(miscInfo.name(), text);
                }
            }
            if (miscInfoTextObj.keys().hasNext()) {
                result.put("misc_info_text", miscInfoTextObj);
            }
            return result;
        }

        @Override
        @SneakyThrows
        public UnitBaseData fromJson(JSONObject obj) {
            final var miscInfoTextObj = obj.optJSONObject("misc_info_text");
            final var unitId = obj.getInt("unit_id");
            final var type = Type.valueOf(obj.getString("type"));
            if (miscInfoTextObj != null) {
                return new UnitBaseData(
                        unitId,
                        type,
                        miscInfoTextObj.has(Info.USEFUL_TO_OWN.name()) ? miscInfoTextObj.getString(Info.USEFUL_TO_OWN.name()) : null,
                        miscInfoTextObj.has(Info.USEFUL_TO_TF_OR_TALENT.name()) ? miscInfoTextObj.getString(Info.USEFUL_TO_TF_OR_TALENT.name()) : null,
                        miscInfoTextObj.has(Info.HYPERMAX_PRIORITY.name()) ? miscInfoTextObj.getString(Info.HYPERMAX_PRIORITY.name()) : null
                );
            } else {
                return new UnitBaseData(
                        unitId,
                        type,
                        null,
                        null,
                        null
                );
            }
        }
    };

    public UnitBaseData(int unitId,
                        Type type,
                        @Nullable String usefulToOwnBy,
                        @Nullable String usefulToTFOrTalentBy,
                        @Nullable String hypermaxPriority) {
        this.unitId = unitId;
        this.type = type;
        if (usefulToOwnBy != null) {
            miscInfoTexts.put(Info.USEFUL_TO_OWN, usefulToOwnBy);
        }
        if (usefulToTFOrTalentBy != null) {
            miscInfoTexts.put(Info.USEFUL_TO_TF_OR_TALENT, usefulToTFOrTalentBy);
        }
        if (hypermaxPriority != null) {
            miscInfoTexts.put(Info.HYPERMAX_PRIORITY, hypermaxPriority);
        }
    }

    @Nullable
    public String getTextForInfo(Info info) {
        return miscInfoTexts.get(info);
    }

    public boolean hasInfo(Info info) {
        return getTextForInfo(info) != null;
    }

    public static final Creator<UnitBaseData> CREATOR = new Creator<>() {
        @Override
        public UnitBaseData createFromParcel(Parcel in) {
            return new UnitBaseData(in);
        }

        @Override
        public UnitBaseData[] newArray(int size) {
            return new UnitBaseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private UnitBaseData(Parcel in) {
        unitId = in.readInt();
        type = (Type) requireNonNull(in.readSerializable());
        for (final var miscInfo : Info.values()) {
            final var text = in.readString();
            if (text != null) {
                miscInfoTexts.put(miscInfo, text);
            }
        }
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeSerializable(type);
        for (final var miscInfo : Info.values()) {
            dest.writeString(getTextForInfo(miscInfo));
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        STORY_LEGEND(R.string.story_legends),
        CF_SPECIAL(R.string.cf_specials),
        ADVENT_DROP(R.string.advent_drops),
        RARE(R.string.rares),
        SUPER_RARE(R.string.super_rares),
        UBER(R.string.ubers),
        LEGEND_RARE(R.string.legend_rares);

        @StringRes
        private final int text;
    }

    public enum Info {
        USEFUL_TO_OWN, USEFUL_TO_TF_OR_TALENT, HYPERMAX_PRIORITY
    }

}
