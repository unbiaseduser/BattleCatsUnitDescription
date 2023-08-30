package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.TFMaterialData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddTFMaterialDialog extends AbstractTFMaterialDialog {

    public static final String ADD_TF_MATERIAL_KEY = "add_tf_material";
    public static final String ADD_TF_MATERIAL_BUNDLE_KEY = "tf_material";

    public static void registerDataCallback(Fragment fragment, Consumer<TFMaterialData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_TF_MATERIAL_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                requireNonNull(Bundles.getParcelableCompat(result, ADD_TF_MATERIAL_BUNDLE_KEY, TFMaterialData.class))
        ));
    }

    private final Lazy<AddTFMaterialDialogArgs> args = LazyKt.lazy(() -> AddTFMaterialDialogArgs.fromBundle(requireArguments()));

    @Override
    protected int getTitle() {
        return R.string.add_material;
    }

    @Nullable
    @Override
    protected TFMaterialData getExistingMaterial() {
        return null;
    }

    @Override
    protected boolean checkDuplicateMaterialIndex(int index) {
        return MoreArrays.contains(args.getValue().getExistingTfMaterialIndices(), index);
    }

    @Override
    protected void onValidInput(@NonNull TFMaterialData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(
                ADD_TF_MATERIAL_KEY,
                Bundles.createBundle(bundle -> bundle.putParcelable(ADD_TF_MATERIAL_BUNDLE_KEY, newData))
        );
    }

}
