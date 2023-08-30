package com.sixtyninefourtwenty.bcuddatagenerator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.FragmentBaseDataBinding;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.ListItemUnitBaseDataBinding;
import com.sixtyninefourtwenty.bcuddatagenerator.dialogs.AddUnitBaseDataDialog;
import com.sixtyninefourtwenty.bcuddatagenerator.dialogs.EditUnitBaseDataDialog;
import com.sixtyninefourtwenty.bcuddatagenerator.utils.Utils;
import com.sixtyninefourtwenty.common.objects.UnitBaseData;
import com.sixtyninefourtwenty.common.utils.Misc;
import com.sixtyninefourtwenty.conflictresolver.ListImportConflictResolver;
import com.sixtyninefourtwenty.stuff.ListAdapters;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

public final class BaseDataFragment extends Fragment {

    private FragmentBaseDataBinding binding;

    private final UnitBaseDataAdapter adapter = new UnitBaseDataAdapter(
            (data, a) -> new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete)
                    .setMessage(getString(R.string.delete_unit_confirmation, data.getUnitId()))
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> ListAdapters.removeElement(a, data))
                    .setNegativeButton(android.R.string.cancel, null)
                    .show(),
            (data, a) -> NavHostFragment.findNavController(this).navigate(BaseDataFragmentDirections.actionBaseDataFragmentToEditUnitBaseDataDialog(a.getCurrentList().stream().mapToInt(UnitBaseData::getUnitId).toArray(), data))
    );

    private final OnBackPressedCallback askExitConfirmation = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (!adapter.getCurrentList().isEmpty()) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.confirm_exit)
                        .setMessage(getString(R.string.items_present_exit_notice, getString(R.string.talents_lowercase)))
                        .setPositiveButton(R.string.exit, (dialog, which) -> NavHostFragment.findNavController(BaseDataFragment.this).popBackStack())
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                NavHostFragment.findNavController(BaseDataFragment.this).popBackStack();
            }
        }
    };

    private final ActivityResultLauncher<String> importJson = Utils.createImportTextLauncher(this, jsonString -> {
        try {
            new ListImportConflictResolver<>(adapter.getCurrentList(),
                    UnitBaseData.SERIALIZER.listFromJson(new JSONArray(jsonString)),
                    (first, second) -> first.getUnitId() == second.getUnitId(),
                    resolution -> new MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.conflict_resolution_for_unit_id, resolution.getExistingElement().getUnitId()))
                            .setItems(new String[]{getString(R.string.keep), getString(R.string.replace)}, (dialog, which) -> {
                                switch (which) {
                                    case 0 -> resolution.getImportedList().remove(resolution.getImportedElement());
                                    case 1 -> resolution.getExistingList().set(resolution.getExistingList().indexOf(resolution.getExistingElement()), resolution.getImportedElement());
                                }
                                resolution.retry();
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show(),
                    list -> adapter.submitList(Misc.cast(list))
            ).resolve();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    });

    private final ActivityResultLauncher<String> exportJson = Utils.createExportTextLauncher(this,
            () -> UnitBaseData.SERIALIZER.listToJson(adapter.getCurrentList()).toString());

    private final MenuProvider provider = new MenuProvider() {

        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.menu_import_export, menu);
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            final var id = menuItem.getItemId();
            if (id == R.id.impor) {
                importJson.launch("application/json");
                return true;
            } else if (id == R.id.export) {
                if (adapter.getCurrentList().isEmpty()) {
                    Toast.makeText(requireContext(), R.string.nothing_to_export, Toast.LENGTH_SHORT).show();
                    return true;
                }
                exportJson.launch("unit_base_data");
                return true;
            } else if (id == android.R.id.home) {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                return true;
            }
            return false;
        }
    };

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBaseDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), askExitConfirmation);
        requireActivity().addMenuProvider(provider, getViewLifecycleOwner());
        if (savedInstanceState != null) {
            adapter.submitList(savedInstanceState.getParcelableArrayList("saved_list"));
        }
        binding.add.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(BaseDataFragmentDirections.actionBaseDataFragmentToAddUnitBaseDataDialog(adapter.getCurrentList().stream().mapToInt(UnitBaseData::getUnitId).toArray())));
        binding.dataList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.dataList.setAdapter(adapter);

        AddUnitBaseDataDialog.registerDataCallback(this, newData -> ListAdapters.addElement(adapter, newData));

        EditUnitBaseDataDialog.registerDataCallback(this, (existingData, newData) -> ListAdapters.replaceElement(adapter, existingData, newData));

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("saved_list", new ArrayList<>(adapter.getCurrentList()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static final class UnitBaseDataAdapter extends ListAdapter<UnitBaseData, UnitBaseDataAdapter.ViewHolder> {

        private static final DiffUtil.ItemCallback<UnitBaseData> DATA_DIFFER = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull UnitBaseData oldItem, @NonNull UnitBaseData newItem) {
                return oldItem.getUnitId() == newItem.getUnitId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull UnitBaseData oldItem, @NonNull UnitBaseData newItem) {
                return oldItem.equals(newItem);
            }
        };

        private final BiConsumer<UnitBaseData, UnitBaseDataAdapter> onDeleteClick;
        private final BiConsumer<UnitBaseData, UnitBaseDataAdapter> onEditClick;

        public UnitBaseDataAdapter(BiConsumer<UnitBaseData, UnitBaseDataAdapter> onDeleteClick,
                                   BiConsumer<UnitBaseData, UnitBaseDataAdapter> onEditClick) {
            super(DATA_DIFFER);
            this.onDeleteClick = onDeleteClick;
            this.onEditClick = onEditClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemUnitBaseDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding,
                    pos -> onDeleteClick.accept(getItem(pos), this),
                    pos -> onEditClick.accept(getItem(pos), this));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var item = getItem(position);
            holder.binding.unitId.setText(holder.binding.getRoot().getContext().getString(R.string.unit_id_num, item.getUnitId()));
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
             private final ListItemUnitBaseDataBinding binding;

             public ViewHolder(ListItemUnitBaseDataBinding binding, IntConsumer onDeleteClick, IntConsumer onEditClick) {
                 super(binding.getRoot());
                 this.binding = binding;
                 binding.deleteButton.setOnClickListener(v -> onDeleteClick.accept(getAdapterPosition()));
                 binding.editButton.setOnClickListener(v -> onEditClick.accept(getAdapterPosition()));
             }
        }
    }
}
