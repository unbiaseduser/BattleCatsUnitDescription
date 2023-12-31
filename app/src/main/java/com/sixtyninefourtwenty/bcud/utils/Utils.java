package com.sixtyninefourtwenty.bcud.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.graphics.drawable.DrawableKt;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sixtyninefourtwenty.bcud.MobileNavigationDirections;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.markwonplugins.AnchorHeadingPlugin;
import com.sixtyninefourtwenty.bcud.markwonplugins.TableOfContentsPlugin;
import com.sixtyninefourtwenty.bcud.objects.Unit;
import com.sixtyninefourtwenty.bcud.utils.fragments.MiscHelper;
import com.sixtyninefourtwenty.bcud.utils.fragments.NavigationHelper;
import com.sixtyninefourtwenty.bcud.utils.fragments.OnScreenMsgHelper;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.stuff.Contexts;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.coil.CoilImagesPlugin;
import io.noties.markwon.image.file.FileSchemeHandler;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

@NonNullTypesByDefault
public final class Utils {

    public static final DiffUtil.ItemCallback<Unit> UNIT_DIFFER = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Unit oldItem, @NonNull Unit newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Unit oldItem, @NonNull Unit newItem) {
            return oldItem.equals(newItem);
        }
    };

    public static void openWebsite(Context context, String url) {
        new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(new CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(MaterialColors.getColor(context, com.google.android.material.R.attr.colorPrimary, Color.GRAY))
                        .build())
                .build()
                .launchUrl(context, Uri.parse(url));
    }

    public static void setupShowHideFabOnScrolls(ScrollView sv, FloatingActionButton fab) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sv.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            });
        }
    }

    @SuppressWarnings("unused")
    public static <T extends Throwable> T sneakyThrow(Throwable t) throws T {
        @SuppressWarnings("unchecked") final var throwable = (T) t;
        throw throwable;
    }

    public static Markwon getMarkwonAllPlugins(Context context, ScrollView sv) {
        final var anchor = new AnchorHeadingPlugin((view, top) -> sv.smoothScrollTo(0, top));
        return Markwon.builder(context)
                .usePlugin(ImagesPlugin.create(plugin -> plugin.addSchemeHandler(FileSchemeHandler.createWithAssets(context))))
                .usePlugin(CoilImagesPlugin.create(context))
                .usePlugin(HtmlPlugin.create())
                .usePlugin(TablePlugin.create(context))
                .usePlugins(AppPreferences.get(context).getUseToc() ? List.of(anchor, new TableOfContentsPlugin(context)) : List.of(anchor))
                .build();
    }

    public static boolean isDeviceInLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static <T extends Fragment & OnScreenMsgHelper & NavigationHelper & MiscHelper> void applyBetterLinkMovementMethod(TextView tv, T fragment) {
        tv.setMovementMethod(BetterLinkMovementMethod.newInstance().setOnLinkClickListener((t, url) -> {
            if (url.startsWith("http")) {
                fragment.openWebsite(url);
                return true;
            }
            if (url.startsWith("app")) {
                final var guides = Contexts.asApplication(fragment.requireContext(), MyApplication.class).getGuideData().getGuides();
                final var nav = fragment.getNavController();
                switch (url) {
                    case "app://sixty.nine/progress_outline" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(0)));
                    case "app://sixty.nine/early_sol_itf" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(1)));
                    case "app://sixty.nine/cotc" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(2)));
                    case "app://sixty.nine/advents" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideAdvent());
                    case "app://sixty.nine/legend_quest" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(4)));
                    case "app://sixty.nine/legend_quest_improv" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(5)));
                    case "app://sixty.nine/terminology" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(6)));
                    case "app://sixty.nine/everything_atk" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(7)));
                    case "app://sixty.nine/atk_timing_condensed" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(8)));
                    case "app://sixty.nine/leveling_stat" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(9)));
                    case "app://sixty.nine/gamatoto" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(10)));
                    case "app://sixty.nine/cannons" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(11)));
                    case "app://sixty.nine/combos" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(12)));
                    case "app://sixty.nine/account_recovery" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavGuideDetail(guides.get(13)));
                    case "app://sixty.nine/talentpriority" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavTalentPriority());
                    case "app://sixty.nine/hypermaxpriority" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavHypermaxPriority());
                    case "app://sixty.nine/xpcurves" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavXpCost());
                    case "app://sixty.nine/elderepicfruitpriority" ->
                            nav.navigate(MobileNavigationDirections.actionGlobalNavElderEpic());
                    default -> {
                        fragment.showToast("Unexpected link: " + url);
                        Log.w("Link", "Unexpected link: " + url);
                    }
                }
                return true;
            }
            return false;
        }));
    }

    public static void handleOpenWebsite(Fragment fragment, String url) {
        final var context = fragment.requireContext();
        openWebsite(context, url);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        return DrawableKt.toBitmap(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), null);
    }

    private Utils() {
        throw new UnsupportedOperationException("What are you doing here?");
    }
}
