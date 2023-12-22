package com.sixtyninefourtwenty.bcud.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.DialogAboutDevBinding;
import com.sixtyninefourtwenty.bcud.enums.License;
import com.sixtyninefourtwenty.bcud.utils.AssetImageLoading;
import com.sixtyninefourtwenty.bcud.utils.Utils;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingDialogFragment;
import com.sixtyninefourtwenty.materialaboutlibrary.ConvenienceBuilder;
import com.sixtyninefourtwenty.materialaboutlibrary.MaterialAboutFragment;
import com.sixtyninefourtwenty.materialaboutlibrary.items.ActionItem;
import com.sixtyninefourtwenty.materialaboutlibrary.model.AboutCard;
import com.sixtyninefourtwenty.materialaboutlibrary.model.AboutList;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class AboutFragment extends MaterialAboutFragment {

    @Override
    protected boolean shouldAnimate() {
        return false;
    }

    @Override
    protected AboutList getMaterialAboutList() {
        final var nav = NavHostFragment.findNavController(this);
        return new AboutList.Builder()
                .addCard(new AboutCard.Builder()
                        .setTitle(R.string.app_info)
                        .addItem(ConvenienceBuilder.createAppTitleItem(requireContext(), getString(R.string.nav_header_subtitle)))
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.gavel)
                                .setTitle(R.string.app_license)
                                .setSubtext(R.string.gpl_3)
                                .setOnClickAction(() -> Utils.handleOpenWebsite(this, License.GPL3.getLicenseUrl()))
                                .build())
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.description)
                                .setTitle(R.string.third_party_licenses)
                                .setSubtext(R.string.third_party_licenses_subtext)
                                .setOnClickAction(() -> nav.navigate(AboutFragmentDirections.actionNavAboutToNavThirdPartyLicenses()))
                                .build())
                        .build())
                .addCard(new AboutCard.Builder()
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.person)
                                .setTitle(getString(R.string.about_dev))
                                .setOnClickAction(() -> new AboutDevDialog().show(getChildFragmentManager(), null))
                                .build())
                        .build())
                .addCard(new AboutCard.Builder()
                        .setTitle(R.string.udp)
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.gavel)
                                .setTitle(R.string.license)
                                .setSubtext(R.string.cc_by_nc_sa_4)
                                .setOnClickAction(() -> Utils.handleOpenWebsite(this, "https://creativecommons.org/licenses/by-nc-sa/4.0/"))
                                .build())
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.description)
                                .setTitle(R.string.udp_credits)
                                .setOnClickAction(() -> Utils.handleOpenWebsite(this, "https://thanksfeanor.pythonanywhere.com/UDP/credits"))
                                .build())
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.chat)
                                .setTitle(R.string.udp_feedback)
                                .setOnClickAction(() -> Utils.handleOpenWebsite(this, "https://thanksfeanor.pythonanywhere.com/Feedback"))
                                .build())
                        .addItem(new ActionItem.Builder()
                                .setIcon(R.drawable.attach_money)
                                .setTitle(R.string.udp_donations)
                                .setOnClickAction(() -> Utils.handleOpenWebsite(this, "https://thanksfeanor.pythonanywhere.com/Donations"))
                                .build())
                        .build())
                .build();
    }

    public static final class AboutDevDialog extends BaseViewBindingDialogFragment<DialogAboutDevBinding> {

        @Override
        protected @NonNull DialogAboutDevBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogAboutDevBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogAboutDevBinding binding, @Nullable Bundle savedInstanceState) {
            AssetImageLoading.loadAssetImage(binding.icon, "img/lilmohawkcat.png");
            binding.github.setOnClickListener(v -> openWebsite("https://github.com/unbiaseduser"));
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(binding.getRoot())
                    .setPositiveButton(R.string.got_it, null)
                    .create();
        }

    }

}
