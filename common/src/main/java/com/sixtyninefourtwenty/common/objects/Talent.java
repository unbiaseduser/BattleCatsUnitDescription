package com.sixtyninefourtwenty.common.objects;

import androidx.annotation.StringRes;

import com.sixtyninefourtwenty.common.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.repository.TalentInfoSupplier;

import java.util.NoSuchElementException;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public enum Talent {
    TARGET_RED(57,"img/talent_icons/target_red.png"),
    TARGET_FLOATING(58,"img/talent_icons/target_float.png"),
    TARGET_BLACK(59,"img/talent_icons/target_black.png"),
    TARGET_ANGEL(61,"img/talent_icons/target_angel.png"),
    TARGET_ALIEN(62,"img/talent_icons/target_alien.png"),
    TARGET_METAL(60,"img/talent_icons/target_metal.png"),
    TARGET_ZOMBIE(63,"img/talent_icons/target_zombie.png"),
    TARGET_AKU(76,"img/talent_icons/target_aku.png"),
    TARGET_RELIC(64,"img/talent_icons/target_relic.png"),
    WEAKEN(10,"img/talent_icons/weaken.png"),
    FREEZE(12,"img/talent_icons/freeze.png"),
    SLOW(13,"img/talent_icons/slow.png"),
    RESIST(19,"img/talent_icons/resist.png"),
    MASSIVE(21,"img/talent_icons/massive_dmg.png"),
    KB(22,"img/talent_icons/kb.png"),
    CURSE(70,"img/talent_icons/curse.png"),
    DODGE(67,"img/talent_icons/dodge.png"),
    STRENGTHEN(11,"img/talent_icons/strengthen.png"),
    SURVIVE(14,"img/talent_icons/survive.png"),
    CRITICAL(16,"img/talent_icons/crit.png"),
    ZKILL(34,"img/talent_icons/zkill.png"),
    SOULSTRIKE(78,"img/talent_icons/soulstrike.png"),
    BARRIER_BREAK(36,"img/talent_icons/barrier_break.png"),
    SHIELD_PIERCE(75,"img/talent_icons/shield_pierce.png"),
    SAVAGE_BLOW(66,"img/talent_icons/svg_blow.png"),
    DOUBLE_BOUNTY(20,"img/talent_icons/x2cash.png"),
    WAVE(23,"img/talent_icons/wave.png"),
    SURGE(68,"img/talent_icons/surge.png"),
    WEAKEN_IMMUNE(28,"img/talent_icons/weaken_immune.png"),
    FREEZE_IMMUNE(29,"img/talent_icons/freeze_immune.png"),
    SLOW_IMMUNE(30,"img/talent_icons/slow_immune.png"),
    KB_IMMUNE(31,"img/talent_icons/kb_immune.png"),
    WAVE_IMMUNE(25,"img/talent_icons/wave_immune.png"),
    SURGE_IMMUNE(71,"img/talent_icons/surge_immune.png"),
    WARP_IMMUNE(37,"img/talent_icons/warp_immune.png"),
    CURSE_IMMUNE(40,"img/talent_icons/curse_immune.png"),
    TOXIC_IMMUNE(69,"img/talent_icons/toxic_immune.png"),
    WEAKEN_RESIST(42,"img/talent_icons/weaken_resist.png"),
    FREEZE_RESIST(43,"img/talent_icons/freeze_resist.png"),
    SLOW_RESIST(44,"img/talent_icons/slow_resist.png"),
    KB_RESIST(45,"img/talent_icons/kb_resist.png"),
    WAVE_RESIST(46,"img/talent_icons/wave_resist.png"),
    SURGE_RESIST(74,"img/talent_icons/surge_resist.png"),
    CURSE_RESIST(52,"img/talent_icons/curse_resist.png"),
    TOXIC_RESIST(73,"img/talent_icons/toxic_resist.png"),
    HP_UP(54,"img/talent_icons/hp_up.png"),
    ATK_UP(53,"img/talent_icons/atk_up.png"),
    SPEED_UP(50,"img/talent_icons/speed_up.png"),
    COST_DOWN(48,"img/talent_icons/cost_down.png"),
    RECHARGE(49,"img/talent_icons/recharge.png");

    public static final List<Talent> VALUES = List.of(values());

    public static Talent fromIndex(int index) {
        return VALUES.find(t -> t.index == index).getOrElseThrow(() -> new NoSuchElementException("No talent has this index: " + index));
    }

    private final int index;
    private final String pathToIcon;

    public Info getInfo(TalentInfoSupplier supplier) {
        return supplier.getInfo(index);
    }

    @AllArgsConstructor
    public enum Priority {
        TOP(R.string.top_priority), HIGH(R.string.high_priority), MID(R.string.medium_priority), LOW(R.string.low_priority), DONT(R.string.do_not_unlock);

        @Getter
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
