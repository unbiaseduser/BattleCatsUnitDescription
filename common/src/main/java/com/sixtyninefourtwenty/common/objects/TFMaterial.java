package com.sixtyninefourtwenty.common.objects;

import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.objects.repository.TFMaterialInfoSupplier;

import java.util.NoSuchElementException;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@NonNullTypesByDefault
public enum TFMaterial {
    XP(6, "img/material/xp.png"),
    PURPLE_SEED(30, "img/material/purple_seed.png"),
    RED_SEED(31, "img/material/red_seed.png"),
    BLUE_SEED(32, "img/material/blue_seed.png"),
    GREEN_SEED(33, "img/material/green_seed.png"),
    YELLOW_SEED(34, "img/material/yellow_seed.png"),
    PURPLE(35, "img/material/purple.png"),
    RED(36, "img/material/red.png"),
    BLUE(37, "img/material/blue.png"),
    GREEN(38, "img/material/green.png"),
    YELLOW(39, "img/material/yellow.png"),
    EPIC(40, "img/material/epic.png"),
    ELDER_SEED(41, "img/material/elder_seed.png"),
    ELDER(42, "img/material/elder.png"),
    EPIC_SEED(43, "img/material/epic_seed.png"),
    GOLD(44, "img/material/gold.png"),
    AKU_SEED(160, "img/material/aku_seed.png"),
    AKU(161, "img/material/aku.png"),
    GOLD_SEED(164, "img/material/gold_seed.png");

    public static final List<TFMaterial> VALUES = List.of(values());

    public static TFMaterial fromIndex(int index) {
        return VALUES.find(m -> m.index == index).getOrElseThrow(() -> new NoSuchElementException("No material has this index: " + index));
    }

    private final int index;
    private final String pathToIcon;

    public Info getInfo(TFMaterialInfoSupplier supplier) {
        return supplier.getInfo(index);
    }

    @Value
    public static class Info {
        String name;
        String explanation;
    }

}
