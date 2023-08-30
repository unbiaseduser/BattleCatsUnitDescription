package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddUnitBaseDataDialog extends AbstractUnitBaseDataDialog {

    public static final String ADD_UNIT_BASE_KEY = "add_unit_base";
    public static final String DATA_BUNDLE_KEY = "data";

    public static void registerDataCallback(Fragment fragment, Consumer<UnitBaseData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_UNIT_BASE_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                requireNonNull(Bundles.getParcelableCompat(result, DATA_BUNDLE_KEY, UnitBaseData.class))
        ));
    }

    private final Lazy<AddUnitBaseDataDialogArgs> args = LazyKt.lazy(() -> AddUnitBaseDataDialogArgs.fromBundle(requireArguments()));

    @Nullable
    @Override
    protected UnitBaseData getExistingData() {
        return null;
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(@NonNull UnitBaseData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(
                ADD_UNIT_BASE_KEY,
                Bundles.createBundle(bundle -> bundle.putParcelable(DATA_BUNDLE_KEY, newData))
        );
    }
}
