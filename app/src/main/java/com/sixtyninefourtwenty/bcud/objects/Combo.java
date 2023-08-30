package com.sixtyninefourtwenty.bcud.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboEffectDescSupplier;
import com.sixtyninefourtwenty.bcud.repository.helper.ComboNameSupplier;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@NonNullTypesByDefault
@Immutable
public class Combo implements Parcelable {

    public static final int MAX_NUM_OF_UNITS = 5;

    int index;
    Type type;
    Level level;
    ImmutableList<Pair<Unit, Unit.Form>> unitsAndForms;
    ImmutableList<Unit> units;

    public String getName(ComboNameSupplier supplier) {
        return supplier.getName(index);
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        ATTACK(0), DEFENSE(1),
        MOVE_SPEED(2), STRONG(14),
        MASSIVE(15), RESIST(16),
        KNOCKBACK(17), SLOW(18),
        FREEZE(19), WEAKEN(20),
        STRENGTHEN(21), CRITICAL(24),
        CANNON_START(3), CANNON_POWER(6),
        CANNON_RECHARGE(7), BASE_DEFENSE(10),
        STARTING_MONEY(5), WORKER_START_LEVEL(4),
        WORKER_MAX(9), RESEARCH(11),
        ACCOUNTING(12), STUDY(13),
        EVA_KILLER(23), WITCH_KILLER(22),
        //unused worker speed combo
        WORKER_SPEED(8);

        @IntRange(from = 0)
        private final int index;

        public String getEffectDesc(ComboEffectDescSupplier supplier, Level level) {
            return supplier.getDesc(index, level.getIndex());
        }

        public static final ImmutableList<Type> VALUES = ImmutableList.copyOf(values());
    }

    @Getter
    @AllArgsConstructor
    public enum Level {
        SM(0), M(1), L(2), XL(3);

        @IntRange(from = 0)
        private final int index;

        public String getEffectDesc(ComboEffectDescSupplier supplier, Type type) {
            return supplier.getDesc(type.getIndex(), index);
        }

        public static final ImmutableList<Level> VALUES = ImmutableList.copyOf(values());
    }

    private Combo(Parcel in) {
        Function<Parcel, ImmutableList<Pair<Unit, Unit.Form>>> readUnitList = parcel -> {
            var units = parcel.createTypedArrayList(Unit.CREATOR);
            var nums = parcel.createIntArray();
            var list = new ImmutableList.Builder<Pair<Unit, Unit.Form>>();
            for (int i = 0; i < units.size(); i++) {
                list.add(new Pair<>(units.get(i), Unit.Form.values()[nums[i]]));
            }
            return list.build();
        };

        index = in.readInt();
        type = (Type) in.readSerializable();
        level = (Level) in.readSerializable();
        unitsAndForms = readUnitList.apply(in);
        units = ImmutableList.copyOf(in.createTypedArrayList(Unit.CREATOR));
    }

    public static final Creator<Combo> CREATOR = new Creator<>() {
        @Override
        public Combo createFromParcel(Parcel in) {
            return new Combo(in);
        }

        @Override
        public Combo[] newArray(int size) {
            return new Combo[size];
        }
    };

    public Combo(int index, Type type, Level level, Unit first, @Nullable Unit second, @Nullable Unit third, @Nullable Unit fourth, @Nullable Unit fifth,
                 int formOfFirst, int formOfSecond, int formOfThird, int formOfFourth, int formOfFifth) {
        this.index = index;
        var forms = Unit.Form.values();
        this.type = type;
        this.level = level;
        var list = new ImmutableList.Builder<Pair<Unit, Unit.Form>>();
        list.add(new Pair<>(first, forms[formOfFirst]));
        if (second != null) {
            list.add(new Pair<>(second, forms[formOfSecond]));
        }
        if (third != null) {
            list.add(new Pair<>(third, forms[formOfThird]));
        }
        if (fourth != null) {
            list.add(new Pair<>(fourth, forms[formOfFourth]));
        }
        if (fifth != null) {
            list.add(new Pair<>(fifth, forms[formOfFifth]));
        }
        unitsAndForms = list.build();
        units = ImmutableList.copyOf(CollectionsKt.map(unitsAndForms, Pair::getFirst));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeSerializable(type);
        dest.writeSerializable(level);
        dest.writeTypedList(CollectionsKt.map(unitsAndForms, Pair::getFirst));
        final var nums = new int[unitsAndForms.size()];
        for (int i = 0; i < unitsAndForms.size(); i++) {
            nums[i] = unitsAndForms.get(i).getSecond().ordinal();
        }
        dest.writeIntArray(nums);
        dest.writeTypedList(units);
    }
}
