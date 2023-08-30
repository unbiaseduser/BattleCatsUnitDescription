package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditUnitBaseDataDialog extends AbstractUnitBaseDataDialog {

    public static final String EDIT_UNIT_BASE_KEY = "edit_unit_base";
    public static final String EXISTING_DATA_BUNDLE_KEY = "orig_data";
    public static final String NEW_DATA_BUNDLE_KEY = "new_data";

    public static void registerDataCallback(Fragment fragment, BiConsumer<UnitBaseData, UnitBaseData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_UNIT_BASE_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_DATA_BUNDLE_KEY, UnitBaseData.class),
                Bundles.getParcelableCompat(result, NEW_DATA_BUNDLE_KEY, UnitBaseData.class)
        ));
    }

    private final Lazy<EditUnitBaseDataDialogArgs> args = LazyKt.lazy(() -> EditUnitBaseDataDialogArgs.fromBundle(requireArguments()));

    @NonNull
    @Override
    protected UnitBaseData getExistingData() {
        return args.getValue().getExistingData();
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        if (input == args.getValue().getExistingData().getUnitId()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(@NonNull UnitBaseData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_UNIT_BASE_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_DATA_BUNDLE_KEY, getExistingData());
            bundle.putParcelable(NEW_DATA_BUNDLE_KEY, newData);
        }));
    }
}
