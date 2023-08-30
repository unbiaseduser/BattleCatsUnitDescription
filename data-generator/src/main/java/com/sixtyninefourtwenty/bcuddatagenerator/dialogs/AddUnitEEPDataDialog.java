package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.common.objects.ElderEpic;
import com.sixtyninefourtwenty.common.objects.UnitEEPriorityData;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddUnitEEPDataDialog extends AbstractUnitEEPDataDialog {

    public static final String ADD_UNIT_EEP_KEY = "add_unit_eep";
    public static final String DATA_BUNDLE_KEY = "data";

    public static void registerDataCallback(Fragment fragment, Consumer<UnitEEPriorityData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_UNIT_EEP_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, DATA_BUNDLE_KEY, UnitEEPriorityData.class)
        ));
    }

    private final Lazy<AddUnitEEPDataDialogArgs> args = LazyKt.lazy(() -> AddUnitEEPDataDialogArgs.fromBundle(requireArguments()));

    @Nullable
    @Override
    protected UnitEEPriorityData getExistingData() {
        return null;
    }

    @Override
    protected boolean checkDuplicateUnitIdAndElderEpic(int input, @NonNull ElderEpic elderEpic) {
        for (final var data : args.getValue().getExistingDatas()) {
            if (data.getUnitId() == input && data.getElderEpic() == elderEpic) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onValidInput(@NonNull UnitEEPriorityData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(ADD_UNIT_EEP_KEY, Bundles.createBundle(bundle -> bundle.putParcelable(DATA_BUNDLE_KEY, newData)));
    }
}
