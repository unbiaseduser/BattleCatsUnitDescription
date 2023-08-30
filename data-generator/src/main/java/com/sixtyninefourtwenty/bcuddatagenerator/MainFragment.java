package com.sixtyninefourtwenty.bcuddatagenerator;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sixtyninefourtwenty.bcuddatagenerator.databinding.FragmentMainBinding;
import com.sixtyninefourtwenty.bcuddatagenerator.databinding.ListItemNavigationItemBinding;
import com.sixtyninefourtwenty.bcuddatagenerator.objects.NavigationItem;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.navigationItems.setLayoutManager(new GridLayoutManager(requireContext(), NavigationItemsAdapter.getGridSpan(requireContext())));
        final var adapter = new NavigationItemsAdapter(List.of(
                new NavigationItem(R.string.unit_base_data, MainFragmentDirections.actionMainFragmentToBaseDataFragment()),
                new NavigationItem(R.string.tf_material_navigation_item, MainFragmentDirections.mainToTfMaterialData()),
                new NavigationItem(R.string.talents, MainFragmentDirections.actionMainFragmentToTalentDataFragment()),
                new NavigationItem(R.string.hypermax_priority, MainFragmentDirections.actionMainFragmentToHypermaxDataFragment()),
                new NavigationItem(R.string.elder_epic_tf_priority, MainFragmentDirections.actionMainFragmentToEEPDataFragment())
        ), NavHostFragment.findNavController(this)::navigate);
        binding.navigationItems.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static final class NavigationItemsAdapter extends ListAdapter<NavigationItem, NavigationItemsAdapter.ViewHolder> {

        public static int getGridSpan(Context context) {
            return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        }

        private static final DiffUtil.ItemCallback<NavigationItem> NAVIGATION_ITEM_DIFFER = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull NavigationItem oldItem, @NonNull NavigationItem newItem) {
                return oldItem.getTitle() == newItem.getTitle();
            }

            @Override
            public boolean areContentsTheSame(@NonNull NavigationItem oldItem, @NonNull NavigationItem newItem) {
                return oldItem.getTitle() == newItem.getTitle();
            }
        };

        private final Consumer<NavDirections> onItemClick;

        public NavigationItemsAdapter(List<NavigationItem> navigationItems, Consumer<NavDirections> onItemClick) {
            super(NAVIGATION_ITEM_DIFFER);
            submitList(navigationItems);
            this.onItemClick = onItemClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemNavigationItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding, pos -> onItemClick.accept(getItem(pos).getDirections()));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var item = getItem(position);
            holder.binding.title.setText(item.getTitle());
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ListItemNavigationItemBinding binding;

            public ViewHolder(ListItemNavigationItemBinding binding, IntConsumer onItemClick) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnClickListener(v -> onItemClick.accept(getAdapterPosition()));
            }
        }
    }

}