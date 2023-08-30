package com.sixtyninefourtwenty;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteItem;
import com.sixtyninefourtwenty.bcud.objects.favorites.FavoriteReason;
import com.sixtyninefourtwenty.bcud.utils.FavoritesBackupRestore;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class FavoritesBackupRestoreTest {

    private static Path normalBackupFile;
    private static Path wrongBackupFile;
    private ListeningExecutorService executorService;

    @BeforeAll
    static void setupAll() throws Exception {
        normalBackupFile = Paths.get("normal_backup.zip");
        wrongBackupFile = Paths.get("wrong_backup.zip");
        try (final var output = new ZipOutputStream(Files.newOutputStream(wrongBackupFile))) {
            output.putNextEntry(new ZipEntry("some_file"));
            output.write("something".getBytes(StandardCharsets.UTF_8));
        }
    }

    @BeforeEach
    void setup() {
        executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    private ListenableFuture<@Nullable Void> backupFavorites(ListeningExecutorService executorService,
                                                             OutputStream outputStream,
                                                             List<FavoriteItem> items,
                                                             List<FavoriteReason> reasons) {
        return executorService.submit(() -> {
            FavoritesBackupRestore.backupFavorites(outputStream, FavoriteItem.SERIALIZER.listToJson(items).toString(), FavoriteReason.SERIALIZER.listToJson(reasons).toString());
            return null;
        });
    }

    @Test
    void backupRestoreSuccess() throws Exception {
        final var favoriteItems = List.of(new FavoriteItem(24), new FavoriteItem(25));
        final var favoriteReasons = List.of(new FavoriteReason(1, 24, "lorem"), new FavoriteReason(2, 25, "ipsum"));
        final var backupFuture = backupFavorites(
                executorService,
                Files.newOutputStream(normalBackupFile),
                favoriteItems,
                favoriteReasons
        );
        backupFuture.get();
        assertTrue(Files.exists(normalBackupFile));

        final var restoreFuture = FavoritesBackupRestore.restoreFavorites(
                executorService,
                Files.newInputStream(normalBackupFile)
        );
        final var output = restoreFuture.get();
        assertIterableEquals(favoriteItems, output.getItems());
        assertIterableEquals(favoriteReasons, output.getReasons());
    }

    @Test
    void restoreFailedFromInvalidZip() throws Exception {
        final var restoreFuture = FavoritesBackupRestore.restoreFavorites(
                executorService,
                Files.newInputStream(wrongBackupFile)
        );
        final var exception = assertThrows(ExecutionException.class, restoreFuture::get);
        final var actualException = assertInstanceOf(FavoritesBackupRestore.RestoreException.class, exception.getCause());
        assertNotNull(actualException);
        final var exceptionMessage = actualException.getMessage();
        assertNotNull(exceptionMessage);
        assertTrue(exceptionMessage.contains("Wrong or malformed zip file"));
    }

    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    @AfterAll
    static void tearDownAll() {
        try {
            Files.deleteIfExists(normalBackupFile);
        } catch (IOException e) {
            System.err.println("Cannot delete normal backup file: " + e);
        }
        try {
            Files.deleteIfExists(wrongBackupFile);
        } catch (IOException e) {
            System.err.println("Cannot delete wrong backup file: " + e);
        }
    }

}
