package com.sixtyninefourtwenty.common.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;
import com.sixtyninefourtwenty.common.utils.Validations;

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
                if (Validations.isValidInfoString(text)) {
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
            return new UnitBaseData(
                    obj.getInt("unit_id"),
                    Type.valueOf(obj.getString("type")),
                    miscInfoTextObj != null ? miscInfoTextObj.optString(Info.USEFUL_TO_OWN.name(), Validations.NO_INFO) : null,
                    miscInfoTextObj != null ? miscInfoTextObj.optString(Info.USEFUL_TO_TF_OR_TALENT.name(), Validations.NO_INFO) : null,
                    miscInfoTextObj != null ? miscInfoTextObj.optString(Info.HYPERMAX_PRIORITY.name(), Validations.NO_INFO) : null
            );
        }
    };

    public UnitBaseData(int unitId,
                        Type type,
                        @Nullable String usefulToOwnBy,
                        @Nullable String usefulToTFOrTalentBy,
                        @Nullable String hypermaxPriority) {
        this.unitId = unitId;
        this.type = type;
        if (Validations.isValidInfoString(usefulToOwnBy)) {
            miscInfoTexts.put(Info.USEFUL_TO_OWN, usefulToOwnBy);
        }
        if (Validations.isValidInfoString(usefulToTFOrTalentBy)) {
            miscInfoTexts.put(Info.USEFUL_TO_TF_OR_TALENT, usefulToTFOrTalentBy);
        }
        if (Validations.isValidInfoString(hypermaxPriority)) {
            miscInfoTexts.put(Info.HYPERMAX_PRIORITY, hypermaxPriority);
        }
    }

    public String getTextForInfo(Info info) {
        return miscInfoTexts.getOrDefault(info, "");
    }

    public boolean hasInfo(Info info) {
        return !Validations.NO_INFO.equals(getTextForInfo(info));
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
        type = (Type) in.readSerializable();
        for (final var miscInfo : Info.values()) {
            final var text = in.readString();
            if (Validations.isValidInfoString(text)) {
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
