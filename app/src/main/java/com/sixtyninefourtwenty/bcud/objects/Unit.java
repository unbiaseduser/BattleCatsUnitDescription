package com.sixtyninefourtwenty.bcud.objects;

import static java.util.Objects.requireNonNull;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitDescPageTextsSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitEEPriorityReasoningSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.bcud.utils.Stuff;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import kotlin.collections.CollectionsKt;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
public class Unit implements Parcelable {

    @AllArgsConstructor
    @Getter
    public enum Form {
        FIRST("_f00.png"),
        SECOND("_c00.png"),
        TRUE("_s00.png");

        private final String iconFileNameEnding;
    }

    @Value
    public static class DescPageTexts {
        String usefulToOwnBy;
        String usefulToTFOrTalentBy;
        String hypermaxPriority;

        public static DescPageTexts EMPTY = new DescPageTexts("", "", "");
    }

    @Value
    public static class Explanation {
        String firstFormName;
        String secondFormName;
        String trueFormName;
        String firstFormDescription;
        String secondFormDescription;
        String trueFormDescription;

        public String getName(Form form) {
            return switch (form) {
                case FIRST -> firstFormName;
                case SECOND -> secondFormName;
                case TRUE -> trueFormName;
            };
        }

        @SuppressWarnings("unused")
        public String getDescription(Form form) {
            return switch (form) {
                case FIRST -> firstFormDescription;
                case SECOND -> secondFormDescription;
                case TRUE -> trueFormDescription;
            };
        }

        public boolean hasTf() {
            return !trueFormName.isEmpty();
        }

    }

    public static final int MAX_NUM_OF_TF_MATERIALS = 6;
    public static final int MAX_NUM_OF_TALENTS = 6;

    @IntRange(from = 0)
    int id;

    UnitBaseData.Type type;

    ImmutableList<TFMaterialData> tfMaterialData;

    ImmutableList<TalentData> talentData;

    @Getter(AccessLevel.NONE)
    Map<Talent.Priority, ImmutableList<Talent>> talentMap = new EnumMap<>(Talent.Priority.class);

    public static final Creator<Unit> CREATOR = new Creator<>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    public DescPageTexts getDescPageTexts(UnitDescPageTextsSupplier supplier) {
        return supplier.getDescPageTexts(id);
    }

    public Explanation getExplanation(UnitExplanationSupplier supplier) {
        return supplier.getExplanation(id);
    }

    // this method is used in ElderEpicTFPriorityDialog by a unit got thru safe args, but android
    // studio doesn't recognize the generated args class
    @SuppressWarnings("unused")
    public String getEEPriorityReasoning(UnitEEPriorityReasoningSupplier supplier, ElderEpic elderEpic) {
        return supplier.getPriorityReasoningForUnitWithId(id, elderEpic);
    }

    public boolean isCfSpecial() {
        return type == UnitBaseData.Type.CF_SPECIAL;
    }

    public boolean hasTF(UnitExplanationSupplier supplier) {
        return getExplanation(supplier).hasTf();
    }

    public String getDesc(Context context) {
        return Stuff.getText("text/unit_desc/" + id + ".txt", context);
    }

    public ImmutableList<Talent> getTalents(Talent.Priority priority) {
        return requireNonNull(talentMap.getOrDefault(priority, ImmutableList.of()));
    }

    public String getUdpUrl() {
        String unitId;
        if (id < 100 && id > 9) {
            unitId = "0" + id;
        } else if (id <= 9) {
            unitId = "00" + id;
        } else {
            unitId = Integer.toString(id);
        }
        return String.format("https://thanksfeanor.pythonanywhere.com/UDP/%s", unitId);
    }

    public Unit(int id, UnitBaseData.Type type,
                ImmutableList<TFMaterialData> tfMaterialData,
                ImmutableList<TalentData> talentData) {
        this.id = id;
        this.type = type;
        this.tfMaterialData = tfMaterialData;
        this.talentData = talentData;
        for (final var priority : Talent.Priority.values()) {
            final var talentsForPriority = CollectionsKt.filter(talentData, data -> data.getPriority() == priority);
            if (!talentsForPriority.isEmpty()) {
                talentMap.put(priority, ImmutableList.copyOf(CollectionsKt.map(talentsForPriority, TalentData::getTalent)));
            }
        }
    }

    public String getLatestFormIconPath(UnitExplanationSupplier supplier) {
        final var path = new StringBuilder("img/unit_icons/uni");
        if (id < 100 && id > 9) {
            path.append("0").append(id);
        } else if (id <= 9) {
            path.append("00").append(id);
        } else path.append(id);
        path.append(hasTF(supplier) ? Form.TRUE.getIconFileNameEnding() : Form.SECOND.getIconFileNameEnding());
        return path.toString();
    }

    public String getIconPathForForm(Form form, UnitExplanationSupplier supplier) {
        if (!hasTF(supplier) && form == Form.TRUE) {
            throw new IllegalArgumentException("Unit does not have true form");
        }
        final var path = new StringBuilder("img/unit_icons/uni");
        if (id < 100 && id > 9) {
            path.append("0").append(id);
        } else if (id <= 9) {
            path.append("00").append(id);
        } else path.append(id);
        path.append(form.getIconFileNameEnding());
        return path.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Unit(Parcel in) {

        BiConsumer<Parcel, Map<Talent.Priority, ImmutableList<Talent>>> readTalentMapping = (parcel, mapping) -> {
            for (final var priority : Talent.Priority.values()) {
                final var list = parcel.createStringArrayList();
                if (list != null && !list.isEmpty()) {
                    mapping.put(priority, ImmutableList.copyOf(CollectionsKt.map(list, Talent::valueOf)));
                }
            }
        };

        id = in.readInt();
        type = requireNonNull((UnitBaseData.Type) in.readSerializable());
        tfMaterialData = ImmutableList.copyOf(requireNonNull(in.createTypedArrayList(TFMaterialData.CREATOR)));
        talentData = ImmutableList.copyOf(requireNonNull(in.createTypedArrayList(TalentData.CREATOR)));
        readTalentMapping.accept(in, talentMap);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        Consumer<Parcel> writeTalentMapping = parcel -> {
            for (final var priority : Talent.Priority.values()) {
                final var list = requireNonNull(talentMap.getOrDefault(priority, ImmutableList.of()));
                parcel.writeStringList(CollectionsKt.map(list, Object::toString));
            }
        };

        dest.writeInt(id);
        dest.writeSerializable(type);
        dest.writeTypedList(tfMaterialData);
        dest.writeTypedList(talentData);
        writeTalentMapping.accept(dest);
    }
}