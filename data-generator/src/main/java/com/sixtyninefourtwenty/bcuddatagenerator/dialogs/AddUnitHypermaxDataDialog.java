package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.Hypermax;
import com.sixtyninefourtwenty.common.objects.UnitHypermaxData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddUnitHypermaxDataDialog extends AbstractUnitHypermaxDataDialog {

    public static final String ADD_UNIT_HYPERMAX_KEY = "add_unit_hypermax";
    public static final String DATA_BUNDLE_KEY = "data";

    public static void registerDataCallback(Fragment fragment, Consumer<UnitHypermaxData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_UNIT_HYPERMAX_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, DATA_BUNDLE_KEY, UnitHypermaxData.class)
        ));
    }

    private final Lazy<AddUnitHypermaxDataDialogArgs> args = LazyKt.lazy(() -> AddUnitHypermaxDataDialogArgs.fromBundle(requireArguments()));

    @Nullable
    @Override
    protected UnitHypermaxData getExistingData() {
        return null;
    }

    @Override
    protected int getTitle() {
        return R.string.add_hypermax_data;
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, int selectedUnitTypePosition, int selectedUnitPriorityPosition) {
        requireActivity().getSupportFragmentManager().setFragmentResult(ADD_UNIT_HYPERMAX_KEY, Bundles.createBundle(bundle -> bundle.putParcelable(DATA_BUNDLE_KEY, new UnitHypermaxData(unitId,
                Hypermax.Priority.values()[selectedUnitPriorityPosition],
                Hypermax.UnitType.values()[selectedUnitTypePosition]))));
    }
}
