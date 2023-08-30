package com.sixtyninefourtwenty.common.objects;

import android.os.Parcel;
import android.os.Parcelable;

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
public class TalentData implements Parcelable {

    Talent talent;
    Talent.Priority priority;

    public static final JsonSerializer<TalentData> SERIALIZER = new JsonSerializer<>() {
        @Override
        @SneakyThrows
        public JSONObject toJson(TalentData obj) {
            return new JSONObject()
                    .put("talent_index", obj.talent.getIndex())
                    .put("priority", obj.priority.name());
        }

        @Override
        @SneakyThrows
        public TalentData fromJson(JSONObject obj) {
            return new TalentData(
                    Talent.fromIndex(obj.getInt("talent_index")),
                    Talent.Priority.valueOf(obj.getString("priority"))
            );
        }
    };

    private TalentData(Parcel in) {
        talent = (Talent) in.readSerializable();
        priority = (Talent.Priority) in.readSerializable();
    }

    public static final Creator<TalentData> CREATOR = new Creator<>() {
        @Override
        public TalentData createFromParcel(Parcel in) {
            return new TalentData(in);
        }

        @Override
        public TalentData[] newArray(int size) {
            return new TalentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(talent);
        dest.writeSerializable(priority);
    }
}
