package com.sixtyninefourtwenty.bcuddatagenerator.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.sixtyninefourtwenty.common.objects.Talent;
import com.sixtyninefourtwenty.common.objects.TalentData;
import com.sixtyninefourtwenty.common.objects.UnitTalentData;
import com.sixtyninefourtwenty.common.utils.MoreArrays;
import com.sixtyninefourtwenty.stuff.Bundles;

import java.util.List;
import java.util.function.Consumer;

import kotlin.Lazy;
import kotlin.LazyKt;

public final class AddUnitTalentDataDialog extends AbstractUnitTalentDataDialog {

    public static final String ADD_UNIT_TALENT_KEY = "add_unit_talent";
    public static final String DATA_BUNDLE_KEY = "data";

    public static void registerDataCallback(Fragment fragment, Consumer<UnitTalentData> callback) {
        fragment.requireActivity().getSupportFragmentManager().setFragmentResultListener(ADD_UNIT_TALENT_KEY, fragment.getViewLifecycleOwner(), (requestKey, result) -> callback.accept(
                Bundles.getParcelableCompat(result, DATA_BUNDLE_KEY, UnitTalentData.class)
        ));
    }

    private final Lazy<AddUnitTalentDataDialogArgs> args = LazyKt.lazy(() -> AddUnitTalentDataDialogArgs.fromBundle(requireArguments()));

    @Nullable
    @Override
    protected UnitTalentData getExistingData() {
        return null;
    }

    @NonNull
    @Override
    protected NavDirections getShowEditTalentDialogDirections(@NonNull TalentData talent, @NonNull int[] talentIndices) {
        return AddUnitTalentDataDialogDirections.actionAddUnitTalentDataDialogToEditTalentDialog(talent, talentIndices);
    }

    @NonNull
    @Override
    protected NavDirections getShowAddTalentDialogDirections(@NonNull int[] talentIndices) {
        return AddUnitTalentDataDialogDirections.actionAddUnitTalentDataDialogToAddTalentDialog(talentIndices);
    }

    @Override
    protected boolean checkDuplicateUnitId(int input) {
        return MoreArrays.contains(args.getValue().getExistingUnitIds(), input);
    }

    @Override
    protected void onValidInput(int unitId, int selectedUnitTypePosition, @NonNull List<TalentData> talents) {
        requireActivity().getSupportFragmentManager().setFragmentResult(ADD_UNIT_TALENT_KEY, Bundles.createBundle(bundle -> bundle.putParcelable(DATA_BUNDLE_KEY, new UnitTalentData(unitId, Talent.UnitType.values()[selectedUnitTypePosition], talents))));
    }
}
