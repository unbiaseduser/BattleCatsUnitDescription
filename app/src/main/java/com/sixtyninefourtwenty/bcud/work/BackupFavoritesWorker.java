package com.sixtyninefourtwenty.bcud.work;

import static com.sixtyninefourtwenty.stuff.Versions.isDeviceOnOrOverSdk;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.utils.Constants;
import com.sixtyninefourtwenty.bcud.utils.FavoritesBackupRestore;

import java.io.IOException;
import java.util.List;

public final class BackupFavoritesWorker extends Worker {

    public BackupFavoritesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private static final String OUTPUT_URI_KEY = "output_uri";
    private static final String FAVORITE_ITEMS_JSON_KEY = "favorites_json";
    private static final String FAVORITE_REASONS_JSON_KEY = "favorite_reasons_json";

    public static Data createInputData(Uri outputUri, List<FavoriteItem> favoriteItems, List<FavoriteReason> favoriteReasons) {
        return new Data.Builder()
                .putString(OUTPUT_URI_KEY, outputUri.toString())
                .putString(FAVORITE_ITEMS_JSON_KEY, FavoriteItem.SERIALIZER.listToJson(favoriteItems).toString())
                .putString(FAVORITE_REASONS_JSON_KEY, FavoriteReason.SERIALIZER.listToJson(favoriteReasons).toString())
                .build();
    }

    @NonNull
    @Override
    public Result doWork() {
        final var data = getInputData();
        final var outputUri = Uri.parse(requireNonNull(data.getString(OUTPUT_URI_KEY)));
        final var favoritesJson = requireNonNull(data.getString(FAVORITE_ITEMS_JSON_KEY));
        final var favoriteReasonsJson = requireNonNull(data.getString(FAVORITE_REASONS_JSON_KEY));
        try {
            final var outputStream = getApplicationContext().getContentResolver().openOutputStream(outputUri);
            if (outputStream != null) {
                FavoritesBackupRestore.backupFavorites(outputStream, favoritesJson, favoriteReasonsJson);
                if (isDeviceOnOrOverSdk(Build.VERSION_CODES.TIRAMISU) && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    NotificationManagerCompat.from(getApplicationContext())
                            .notify(Constants.FAVORITES_BACKUP_NOTIFICATION_ID, new NotificationCompat.Builder(getApplicationContext(), Constants.FAVORITES_BACKUP_NOTIFICATION_CHANNEL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher_round)
                                    .setContentTitle(getApplicationContext().getString(R.string.favorites_backup))
                                    .setContentText(getApplicationContext().getString(R.string.export_success))
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .build());
                }
                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (IOException e) {
            return Result.failure();
        }
    }

    @Override
    public void onStopped() {
        final var outputUri = Uri.parse(requireNonNull(getInputData().getString(OUTPUT_URI_KEY)));
        final var file = requireNonNull(DocumentFile.fromSingleUri(getApplicationContext(), outputUri));
        if (file.exists()) {
            final var deleteSuccess = file.delete();
            if (!deleteSuccess) {
                Log.e(getClass().getSimpleName(), "Failed to delete probably corrupted backup file");

            }
        }
    }

    @NonNull
    @Override
    public ForegroundInfo getForegroundInfo() {
        return new ForegroundInfo(
                Constants.FAVORITES_BACKUP_NOTIFICATION_ID,
                new NotificationCompat.Builder(getApplicationContext(), Constants.FAVORITES_BACKUP_NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(getApplicationContext().getString(R.string.favorites_backup))
                        .setContentText(getApplicationContext().getString(R.string.backing_up_favorites))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setOngoing(true)
                        .setProgress(0, 0, true)
                        .build()
        );
    }
}
