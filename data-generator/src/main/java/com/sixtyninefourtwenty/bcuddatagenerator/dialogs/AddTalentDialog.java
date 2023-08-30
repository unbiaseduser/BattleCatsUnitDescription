package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddTalentDialog extends AbstractTalentDialog {

    public static final String ADD_TALENT_KEY = "add_talent";
    public static final String ADD_TALENT_BUNDLE_KEY = "talent";

    public static void registerDataCallback(Fragment fragment, Consumer<TalentData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_TALENT_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                requireNonNull(Bundles.getParcelableCompat(result, ADD_TALENT_BUNDLE_KEY, TalentData.class))
        ));
    }

    private final Lazy<AddTalentDialogArgs> args = LazyKt.lazy(() -> AddTalentDialogArgs.fromBundle(requireArguments()));

    @Override
    protected int getTitle() {
        return R.string.add_talent;
    }

    @Nullable
    @Override
    protected TalentData getExistingTalent() {
        return null;
    }

    @Override
    protected boolean checkDuplicateTalentIndex(int index) {
        return MoreArrays.contains(args.getValue().getExistingTalentIndices(), index);
    }

    @Override
    protected void onValidInput(@NonNull TalentData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(
                ADD_TALENT_KEY,
                Bundles.createBundle(bundle -> bundle.putParcelable(ADD_TALENT_BUNDLE_KEY, newData))
        );
    }
}
