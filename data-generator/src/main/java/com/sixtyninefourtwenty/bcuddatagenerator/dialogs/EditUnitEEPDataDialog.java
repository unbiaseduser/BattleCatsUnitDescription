package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.UnitEEPriorityData;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditUnitEEPDataDialog extends AbstractUnitEEPDataDialog {

    public static final String EDIT_UNIT_EEP_KEY = "edit_unit_eep";
    public static final String EXISTING_DATA_BUNDLE_KEY = "orig_data";
    public static final String NEW_DATA_BUNDLE_KEY = "new_data";

    public static void registerDataCallback(Fragment fragment, BiConsumer<UnitEEPriorityData, UnitEEPriorityData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_UNIT_EEP_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_DATA_BUNDLE_KEY, UnitEEPriorityData.class),
                Bundles.getParcelableCompat(result, NEW_DATA_BUNDLE_KEY, UnitEEPriorityData.class)
        ));
    }

    private final Lazy<EditUnitEEPDataDialogArgs> args = LazyKt.lazy(() -> EditUnitEEPDataDialogArgs.fromBundle(requireArguments()));

    @NonNull
    @Override
    protected UnitEEPriorityData getExistingData() {
        return args.getValue().getExistingData();
    }

    @Override
    protected boolean checkDuplicateUnitIdAndElderEpic(int input, @NonNull ElderEpic elderEpic) {
        if (input == getExistingData().getUnitId() && elderEpic == getExistingData().getElderEpic()) {
            return false;
        }
        for (final var data : args.getValue().getExistingDatas()) {
            if (data.getUnitId() == input && data.getElderEpic() == elderEpic) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onValidInput(@NonNull UnitEEPriorityData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_UNIT_EEP_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_DATA_BUNDLE_KEY, getExistingData());
            bundle.putParcelable(NEW_DATA_BUNDLE_KEY, newData);
        }));
    }
}
