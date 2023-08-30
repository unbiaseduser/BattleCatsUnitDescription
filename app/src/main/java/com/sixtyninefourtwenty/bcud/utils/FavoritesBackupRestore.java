package com.sixtyninefourtwenty.bcud.utils;

import android.net.Uri;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.work.BackupFavoritesWorker;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.Value;
import lombok.experimental.StandardException;

@NonNullTypesByDefault
public final class FavoritesBackupRestore {

    private FavoritesBackupRestore() {}

    /**
     * Represents the result of a successful restore operation.
     */
    @Value
    public static class Output {
        List<FavoriteItem> items;
        List<FavoriteReason> reasons;
    }

    @StandardException
    public static final class RestoreException extends RuntimeException { }

    public static final String FAVORITE_ITEMS_FILE_NAME = "favorites";
    public static final String FAVORITE_REASONS_FILE_NAME = "favorite_reasons";

    public static LiveData<WorkInfo> backupFavorites(WorkManager workManager,
                                                     Uri outputUri,
                                                     List<FavoriteItem> items,
                                                     List<FavoriteReason> reasons) {
        final var request = new OneTimeWorkRequest.Builder(BackupFavoritesWorker.class)
                .setInputData(BackupFavoritesWorker.createInputData(outputUri, items, reasons))
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build();
        workManager.enqueue(request);
        return workManager.getWorkInfoByIdLiveData(request.getId());
    }

    @WorkerThread
    public static void backupFavorites(OutputStream outputStream,
                                       String favoritesJson,
                                       String favoriteReasonsJson) throws IOException {
        try (final var zip = new ZipOutputStream(outputStream)) {
            final var favoritesFileEntry = new ZipEntry(FAVORITE_ITEMS_FILE_NAME);
            final var favoriteReasonsFileEntry = new ZipEntry(FAVORITE_REASONS_FILE_NAME);
            zip.putNextEntry(favoritesFileEntry);
            zip.write(favoritesJson.getBytes(StandardCharsets.UTF_8));
            zip.putNextEntry(favoriteReasonsFileEntry);
            zip.write(favoriteReasonsJson.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static ListenableFuture<Output> restoreFavorites(ListeningExecutorService executorService,
                                                            InputStream inputStream) {
        return executorService.submit(() -> {
            try (final var zip = new ZipInputStream(inputStream)) {
                final var favoritesFileEntry = zip.getNextEntry();
                if (favoritesFileEntry == null || !favoritesFileEntry.getName().equals(FAVORITE_ITEMS_FILE_NAME)) {
                    throw new RestoreException("Wrong or malformed zip file");
                }
                final var favoritesJsonString = CharStreams.toString(new BufferedReader(new InputStreamReader(zip)));
                final var favoriteReasonsFileEntry = zip.getNextEntry();
                if (favoriteReasonsFileEntry == null || !favoriteReasonsFileEntry.getName().equals(FAVORITE_REASONS_FILE_NAME)) {
                    throw new RestoreException("Wrong or malformed zip file");
                }
                final var favoriteReasonsJsonString = CharStreams.toString(new BufferedReader(new InputStreamReader(zip)));
                return new Output(
                        FavoriteItem.SERIALIZER.listFromJson(new JSONArray(favoritesJsonString)),
                        FavoriteReason.SERIALIZER.listFromJson(new JSONArray(favoriteReasonsJsonString))
                );
            }
        });
    }

}
