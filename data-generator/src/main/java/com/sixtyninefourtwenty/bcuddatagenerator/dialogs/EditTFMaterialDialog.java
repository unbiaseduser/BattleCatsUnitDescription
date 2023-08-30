package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditTFMaterialDialog extends AbstractTFMaterialDialog {

    public static final String EDIT_TF_MATERIAL_KEY = "edit_tf_material";
    public static final String EXISTING_TF_MATERIAL_BUNDLE_KEY = "existing_tf_material";
    public static final String NEW_TF_MATERIAL_BUNDLE_KEY = "new_tf_material";

    public static void registerDataCallback(Fragment fragment, BiConsumer<TFMaterialData, TFMaterialData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_TF_MATERIAL_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_TF_MATERIAL_BUNDLE_KEY, TFMaterialData.class),
                Bundles.getParcelableCompat(result, NEW_TF_MATERIAL_BUNDLE_KEY, TFMaterialData.class)
        ));
    }

    private final Lazy<EditTFMaterialDialogArgs> args = LazyKt.lazy(() -> EditTFMaterialDialogArgs.fromBundle(requireArguments()));

    @Override
    protected int getTitle() {
        return R.string.edit_material;
    }

    @NonNull
    @Override
    protected TFMaterialData getExistingMaterial() {
        return args.getValue().getExistingMaterial();
    }

    @Override
    protected boolean checkDuplicateMaterialIndex(int index) {
        if (index == getExistingMaterial().getMaterial().ordinal()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingTfMaterialIndices(), index);
    }

    @Override
    protected void onValidInput(@NonNull TFMaterialData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_TF_MATERIAL_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_TF_MATERIAL_BUNDLE_KEY, getExistingMaterial());
            bundle.putParcelable(NEW_TF_MATERIAL_BUNDLE_KEY, newData);
        }));
    }

}
