package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.UnitHypermaxData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditUnitHypermaxDataDialog extends AbstractUnitHypermaxDataDialog {

    public static final String EDIT_UNIT_HYPERMAX_KEY = "edit_unit_hypermax";
    public static final String EXISTING_DATA_BUNDLE_KEY = "orig_data";
    public static final String NEW_DATA_BUNDLE_KEY = "new_data";

    public static void registerDataCallback(Fragment fragment, BiConsumer<UnitHypermaxData, UnitHypermaxData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_UNIT_HYPERMAX_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_DATA_BUNDLE_KEY, UnitHypermaxData.class),
                Bundles.getParcelableCompat(result, NEW_DATA_BUNDLE_KEY, UnitHypermaxData.class)
        ));
    }

    private final Lazy<EditUnitHypermaxDataDialogArgs> args = LazyKt.lazy(() -> EditUnitHypermaxDataDialogArgs.fromBundle(requireArguments()));

    @NonNull
    @Override
    protected UnitHypermaxData getExistingData() {
        return args.getValue().getExistingData();
    }

    @Override
    protected int getTitle() {
        return R.string.edit_hypermax_data;
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        if (input == getExistingData().getUnitId()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, int selectedUnitTypePosition, int selectedUnitPriorityPosition) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_UNIT_HYPERMAX_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_DATA_BUNDLE_KEY, getExistingData());
            bundle.putParcelable(NEW_DATA_BUNDLE_KEY, new UnitHypermaxData(
                    unitId,
                    Hypermax.Priority.values()[selectedUnitPriorityPosition],
                    Hypermax.UnitType.values()[selectedUnitTypePosition]
            ));
        }));
    }
}
