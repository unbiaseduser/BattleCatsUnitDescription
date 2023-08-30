package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditTalentDialog extends AbstractTalentDialog {

    public static final String EDIT_TALENT_KEY = "edit_talent";
    public static final String EXISTING_TALENT_BUNDLE_KEY = "existing_talent";
    public static final String NEW_TALENT_BUNDLE_KEY = "new_talent";

    public static void registerDataCallback(Fragment fragment, BiConsumer<TalentData, TalentData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_TALENT_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_TALENT_BUNDLE_KEY, TalentData.class),
                Bundles.getParcelableCompat(result, NEW_TALENT_BUNDLE_KEY, TalentData.class)
        ));
    }

    private final Lazy<EditTalentDialogArgs> args = LazyKt.lazy(() -> EditTalentDialogArgs.fromBundle(requireArguments()));

    @Override
    protected int getTitle() {
        return R.string.edit_talent;
    }

    @NonNull
    @Override
    protected TalentData getExistingTalent() {
        return args.getValue().getExistingTalent();
    }

    @Override
    protected boolean checkDuplicateTalentIndex(int index) {
        if (index == getExistingTalent().getTalent().ordinal()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingTalentIndices(), index);
    }

    @Override
    protected void onValidInput(@NonNull TalentData newData) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_TALENT_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_TALENT_BUNDLE_KEY, getExistingTalent());
            bundle.putParcelable(NEW_TALENT_BUNDLE_KEY, newData);
        }));
    }
}
