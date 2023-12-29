package com.sixtyninefourtwenty.common.objects;

import static java.util.Objects.requireNonNull;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.core.os.ParcelCompat;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.repository.TalentSupplier;
import com.sixtyninefourtwenty.stuff.interfaces.JsonSerializer;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

@Value
@AllArgsConstructor
@NonNullTypesByDefault
public class TalentData implements Parcelable {

    int talentIndex;
    Talent.Priority priority;

    public Talent getTalent(TalentSupplier supplier) {
        return supplier.getTalentByIndex(talentIndex);
    }

    public static final JsonSerializer<TalentData> SERIALIZER = new JsonSerializer<>() {
        @NonNull
        @Override
        @SneakyThrows
        public JSONObject toJson(TalentData obj) {
            return new JSONObject()
                    .put("talent_index", obj.talentIndex)
                    .put("priority", obj.priority.name());
        }

        @Override
        @SneakyThrows
        public TalentData fromJson(@NonNull JSONObject obj) {
            return new TalentData(
                    obj.getInt("talent_index"),
                    Talent.Priority.valueOf(obj.getString("priority"))
            );
        }
    };

    private TalentData(Parcel in) {
        talentIndex = in.readInt();
        priority = requireNonNull(ParcelCompat.readSerializable(in, null, Talent.Priority.class));
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
        dest.writeInt(talentIndex);
        dest.writeSerializable(priority);
    }
}
