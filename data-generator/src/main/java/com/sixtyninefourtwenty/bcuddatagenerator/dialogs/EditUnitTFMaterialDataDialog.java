package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.UnitTFMaterialData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.List;
import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditUnitTFMaterialDataDialog extends AbstractUnitTFMaterialDataDialog {

    public static final String EDIT_UNIT_TF_MATERIAL_KEY = "edit_unit_tf_material";
    public static final String EXISTING_DATA_BUNDLE_KEY = "orig_data";
    public static final String NEW_DATA_BUNDLE_KEY = "new_data";

    public static void registerDataCallback(Fragment fragment, BiConsumer<UnitTFMaterialData, UnitTFMaterialData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_UNIT_TF_MATERIAL_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_DATA_BUNDLE_KEY, UnitTFMaterialData.class),
                Bundles.getParcelableCompat(result, NEW_DATA_BUNDLE_KEY, UnitTFMaterialData.class)
        ));
    }

    private final Lazy<EditUnitTFMaterialDataDialogArgs> args = LazyKt.lazy(() -> EditUnitTFMaterialDataDialogArgs.fromBundle(requireArguments()));

    @NonNull
    @Override
    protected UnitTFMaterialData getExistingData() {
        return args.getValue().getExistingData();
    }

    @NonNull
    @Override
    protected NavDirections getShowEditTFMaterialDialogDirections(@NonNull TFMaterialData material, @NonNull int[] materialIndices) {
        return EditUnitTFMaterialDataDialogDirections.showEditTfMaterialDialog(material, materialIndices);
    }

    @NonNull
    @Override
    protected NavDirections getShowAddTFMaterialDialogDirections(@NonNull int[] materialIndices) {
        return EditUnitTFMaterialDataDialogDirections.showAddTfMaterialDialog(materialIndices);
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        if (input == getExistingData().getUnitId()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, @NonNull List<TFMaterialData> materials) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_UNIT_TF_MATERIAL_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_DATA_BUNDLE_KEY, getExistingData());
            bundle.putParcelable(NEW_DATA_BUNDLE_KEY, new UnitTFMaterialData(unitId, materials));
        }));
    }

}
