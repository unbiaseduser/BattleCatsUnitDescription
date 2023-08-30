package com.sixtyninefourtwenty.bcud;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import java.util.concurrent.ThreadLocalRandom;

public final class PonosQuotesWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final var ponosQuotes = MyApplication.get(context).getPonosQuoteData().getQuotes();
        for (int appWidgetId : appWidgetIds) {
            final var remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_quotes);
            remoteViews.setTextViewText(R.id.quote_widget, ponosQuotes.get(ThreadLocalRandom.current().nextInt(ponosQuotes.size())));
            final var intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    .setClass(context, PonosQuotesWidgetProvider.class);
            final var pendingIntent = PendingIntent.getBroadcast(context, 69, intent, (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0) | PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refresh_quote_widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}