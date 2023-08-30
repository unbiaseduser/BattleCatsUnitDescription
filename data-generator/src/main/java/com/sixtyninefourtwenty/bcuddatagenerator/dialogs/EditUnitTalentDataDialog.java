package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitTalentData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.List;
import java.util.function.BiConsumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class EditUnitTalentDataDialog extends AbstractUnitTalentDataDialog {

    public static final String EDIT_UNIT_TALENT_KEY = "edit_unit_talent";
    public static final String EXISTING_DATA_BUNDLE_KEY = "orig_data";
    public static final String NEW_DATA_BUNDLE_KEY = "new_data";

    public static void registerDataCallback(Fragment fragment, BiConsumer<UnitTalentData, UnitTalentData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(EDIT_UNIT_TALENT_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, EXISTING_DATA_BUNDLE_KEY, UnitTalentData.class),
                Bundles.getParcelableCompat(result, NEW_DATA_BUNDLE_KEY, UnitTalentData.class)
        ));
    }

    private final Lazy<EditUnitTalentDataDialogArgs> args = LazyKt.lazy(() -> EditUnitTalentDataDialogArgs.fromBundle(requireArguments()));

    @NonNull
    @Override
    protected UnitTalentData getExistingData() {
        return args.getValue().getExistingData();
    }

    @NonNull
    @Override
    protected NavDirections getShowEditTalentDialogDirections(@NonNull TalentData talent, @NonNull int[] talentIndices) {
        return EditUnitTalentDataDialogDirections.actionEditUnitTalentDataDialogToEditTalentDialog(talent, talentIndices);
    }

    @NonNull
    @Override
    protected NavDirections getShowAddTalentDialogDirections(@NonNull int[] talentIndices) {
        return EditUnitTalentDataDialogDirections.actionEditUnitTalentDataDialogToAddTalentDialog(talentIndices);
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        if (input == getExistingData().getUnitId()) {
            return false;
        }
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, int selectedUnitTypePosition, @NonNull List<TalentData> talents) {
        requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_UNIT_TALENT_KEY, Bundles.createBundle(bundle -> {
            bundle.putParcelable(EXISTING_DATA_BUNDLE_KEY, getExistingData());
            bundle.putParcelable(NEW_DATA_BUNDLE_KEY, new UnitTalentData(unitId, Talent.UnitType.values()[selectedUnitTypePosition], talents));
        }));
    }
}
