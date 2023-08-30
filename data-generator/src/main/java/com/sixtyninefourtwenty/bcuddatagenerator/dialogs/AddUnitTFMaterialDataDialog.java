package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.objects.UnitTFMaterialData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.List;
import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddUnitTFMaterialDataDialog extends AbstractUnitTFMaterialDataDialog {

    public static final String ADD_UNIT_TF_MATERIAL_KEY = "add_unit_tf_material";
    public static final String DATA_BUNDLE_KEY = "data";

    public static void registerDataCallback(Fragment fragment, Consumer<UnitTFMaterialData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_UNIT_TF_MATERIAL_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(Bundles.getParcelableCompat(result, DATA_BUNDLE_KEY, UnitTFMaterialData.class)));
    }

    private final Lazy<AddUnitTFMaterialDataDialogArgs> args = LazyKt.lazy(() -> AddUnitTFMaterialDataDialogArgs.fromBundle(requireArguments()));

    @Nullable
    @Override
    protected UnitTFMaterialData getExistingData() {
        return null;
    }

    @NonNull
    @Override
    protected NavDirections getShowEditTFMaterialDialogDirections(@NonNull TFMaterialData material, @NonNull int[] materialIndices) {
        return AddUnitTFMaterialDataDialogDirections.showEditTfMaterialDialog(material, materialIndices);
    }

    @NonNull
    @Override
    protected NavDirections getShowAddTFMaterialDialogDirections(@NonNull int[] materialIndices) {
        return AddUnitTFMaterialDataDialogDirections.showAddTfMaterialDialog(materialIndices);
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, @NonNull List<TFMaterialData> materials) {
        requireActivity().getSupportFragmentManager().setFragmentResult(ADD_UNIT_TF_MATERIAL_KEY, Bundles.createBundle(bundle -> bundle.putParcelable(DATA_BUNDLE_KEY, new UnitTFMaterialData(unitId, materials))));
    }

}
