package com.sixtyninefourtwenty;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.repository.UnitData;
import com.sixtyninefourtwenty.bcud.repository.helper.UnitExplanationSupplier;
import com.sixtyninefourtwenty.bcud.viewmodels.UnitDataViewModel;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(InstantTaskExecutorExtension.class)
class UnitDataViewModelTest {

    private static UnitData unitData;
    private static UnitExplanationSupplier explanationSupplier;
    private UnitDataViewModel viewModel;

    @BeforeAll
    static void setupAll() {
        final var app = MyApplication.get(InstrumentationRegistry.getInstrumentation().getTargetContext());
        unitData = app.getUnitData();
        explanationSupplier = app.getUnitExplanationData();
    }

    @BeforeEach
    void setup() {
        viewModel = new UnitDataViewModel(
                new SavedStateHandle(),
                unitData,
                explanationSupplier
        );
    }

    @Test
    void testInitialState() {
        final var ld = viewModel.getUnitDescLiveData(UnitBaseData.Type.STORY_LEGEND);
        final Observer<ImmutableList<Unit>> emptyObserver = list -> {};
        ld.observeForever(emptyObserver);
        await().until(() -> ld.getValue() != null);
        final var allStoryLegendUnits = ld.getValue();
        assertNotNull(allStoryLegendUnits);
        assertIterableEquals(unitData.getMainList(UnitBaseData.Type.STORY_LEGEND), allStoryLegendUnits);
        ld.removeObserver(emptyObserver);
    }

    @Test
    void testUnitListAfterSearch() {
        viewModel.setUnitDescQuery(UnitBaseData.Type.STORY_LEGEND, "baha");
        final var ld = viewModel.getUnitDescLiveData(UnitBaseData.Type.STORY_LEGEND);
        final Observer<ImmutableList<Unit>> emptyObserver = list -> {};
        ld.observeForever(emptyObserver);
        await().until(() -> ld.getValue() != null);
        final var storyLegendUnitsAfterSearch = ld.getValue();
        assertNotNull(storyLegendUnitsAfterSearch);
        assertTrue(storyLegendUnitsAfterSearch.stream().anyMatch(unit -> unit.getId() == 25));
        assertTrue(storyLegendUnitsAfterSearch.stream().noneMatch(unit -> unit.getId() == 24));
        ld.removeObserver(emptyObserver);
    }

}
