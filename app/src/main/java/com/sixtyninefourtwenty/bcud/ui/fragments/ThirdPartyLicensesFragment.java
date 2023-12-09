package com.sixtyninefourtwenty.bcud.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.collect.ImmutableList;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.DialogLibWebsitesBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemThirdPartyLibBinding;
import com.sixtyninefourtwenty.bcud.databinding.ListItemThirdPartyLibWebsiteBinding;
import com.sixtyninefourtwenty.bcud.enums.License;
import com.sixtyninefourtwenty.bcud.objects.ThirdPartyLibInfo;
import com.sixtyninefourtwenty.bcud.utils.Stuff;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseFragment;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingDialogFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class ThirdPartyLicensesFragment extends BaseFragment<@NonNull RecyclerView> {

    @Override
    protected @NonNull RecyclerView initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return new RecyclerView(requireContext());
    }

    @Override
    protected void setup(@NonNull RecyclerView view, @Nullable Bundle savedInstanceState) {
        view.setLayoutManager(new LinearLayoutManager(requireContext()));
        view.setHasFixedSize(true);
        view.setAdapter(new ThirdPartyLibInfoAdapter(Stuff.LIB_INFOS,
                dev -> openWebsite(dev.getWebsite()),
                websites -> {
                    if (websites.size() == 1) {
                        openWebsite(websites.get(0));
                    } else {
                        navigate(ThirdPartyLicensesFragmentDirections.showLibWebsitesDialog(websites.toArray(new String[0])));
                    }
                },
                license -> navigate(ThirdPartyLicensesFragmentDirections.showThirdPartyLicenseDialog(license))));
    }

    public static final class LibWebsitesDialog extends BaseViewBindingDialogFragment<@NonNull DialogLibWebsitesBinding> {

        @Override
        protected @NonNull DialogLibWebsitesBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogLibWebsitesBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogLibWebsitesBinding binding, @Nullable Bundle savedInstanceState) {
            final var websites = ThirdPartyLicensesFragment$LibWebsitesDialogArgs.fromBundle(requireArguments()).getWebsites();
            binding.pickWebsite.setText(getString(R.string.pick_website_notice, websites.length));
            binding.websites.setAdapter(new WebsitesAdapter(websites, this::openWebsite));
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(binding.getRoot())
                    .setTitle(R.string.pick_website)
                    .create();
        }

        private static final class WebsitesAdapter extends ListAdapter<String, WebsitesAdapter.ViewHolder> {

            private static final DiffUtil.ItemCallback<String> WEBSITE_DIFFER = new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            };

            private final Consumer<String> onWebsiteClick;

            public WebsitesAdapter(String[] websites, Consumer<String> onWebsiteClick) {
                super(WEBSITE_DIFFER);
                submitList(List.of(websites));
                this.onWebsiteClick = onWebsiteClick;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final var binding = ListItemThirdPartyLibWebsiteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ViewHolder(binding, pos -> onWebsiteClick.accept(getItem(pos)));
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.binding.website.setText(getItem(position));
            }

            private static final class ViewHolder extends RecyclerView.ViewHolder {
                private final ListItemThirdPartyLibWebsiteBinding binding;

                public ViewHolder(ListItemThirdPartyLibWebsiteBinding binding, IntConsumer onWebsiteClick) {
                    super(binding.getRoot());
                    this.binding = binding;
                    itemView.setOnClickListener(v -> onWebsiteClick.accept(getAbsoluteAdapterPosition()));
                }
            }
        }
    }

    private static final class ThirdPartyLibInfoAdapter extends ListAdapter<ThirdPartyLibInfo, ThirdPartyLibInfoAdapter.ViewHolder> {

        private static final DiffUtil.ItemCallback<ThirdPartyLibInfo> LIB_DIFFER = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull ThirdPartyLibInfo oldItem, @NonNull ThirdPartyLibInfo newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ThirdPartyLibInfo oldItem, @NonNull ThirdPartyLibInfo newItem) {
                return oldItem.equals(newItem);
            }
        };

        private final Consumer<ThirdPartyLibInfo.Dev> onDevClick;
        private final Consumer<ImmutableList<String>> onWebsitesClick;
        private final Consumer<License> onLicenseClick;

        public ThirdPartyLibInfoAdapter(List<ThirdPartyLibInfo> infos, Consumer<ThirdPartyLibInfo.Dev> onDevClick, Consumer<ImmutableList<String>> onWebsitesClick, Consumer<License> onLicenseClick) {
            super(LIB_DIFFER);
            submitList(infos);
            this.onDevClick = onDevClick;
            this.onWebsitesClick = onWebsitesClick;
            this.onLicenseClick = onLicenseClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final var binding = ListItemThirdPartyLibBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding,
                    pos -> onDevClick.accept(getItem(pos).getDev()),
                    pos -> onWebsitesClick.accept(getItem(pos).getWebsites()),
                    pos -> onLicenseClick.accept(getItem(pos).getLicense()));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final var lib = getItem(position);
            final var websites = lib.getWebsites();
            holder.binding.name.setText(lib.getName());
            if (websites.size() == 1) {
                holder.binding.website.setText(websites.get(0));
            } else {
                holder.binding.website.setText(holder.itemView.getContext().getString(R.string.num_websites, websites.size()));
            }
            holder.binding.license.setText(lib.getLicense().getLicenseName());
            holder.binding.dev.setText(lib.getDev().getName());
        }

        private static final class ViewHolder extends RecyclerView.ViewHolder {
            private final ListItemThirdPartyLibBinding binding;

            public ViewHolder(ListItemThirdPartyLibBinding binding, IntConsumer onDevClick, IntConsumer onWebsiteClick, IntConsumer onLicenseClick) {
                super(binding.getRoot());
                this.binding = binding;
                binding.dev.setOnClickListener(v -> onDevClick.accept(getAbsoluteAdapterPosition()));
                binding.website.setOnClickListener(v -> onWebsiteClick.accept(getAbsoluteAdapterPosition()));
                binding.license.setOnClickListener(v -> onLicenseClick.accept(getAbsoluteAdapterPosition()));
            }
        }
    }

}
