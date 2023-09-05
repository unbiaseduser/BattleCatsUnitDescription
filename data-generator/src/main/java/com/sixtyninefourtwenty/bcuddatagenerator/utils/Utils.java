package com.sixtyninefourtwenty.bcuddatagenerator.utils;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.sixtyninefourtwenty.bcuddatagenerator.MyApplication;
import com.sixtyninefourtwenty.bcuddatagenerator.R;
import com.sixtyninefourtwenty.common.annotations.NonNullTypesByDefault;
import com.sixtyninefourtwenty.common.utils.FutureCallbackLambdas;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NonNullTypesByDefault
public final class Utils {

    private Utils() {
    }

    public static ActivityResultLauncher<String> createImportTextLauncher(Fragment fragment,
                                                                          Supplier<ListeningExecutorService> executorService,
                                                                          Consumer<String> onSuccess,
                                                                          Runnable onFailure) {
        return fragment.registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                final var future = executorService.get().submit(() -> {
                    final var inputStream = fragment.requireContext().getContentResolver().openInputStream(result);
                    try (final var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        return CharStreams.toString(reader);
                    }
                });
                Futures.addCallback(
                        future,
                        new FutureCallbackLambdas<>(onSuccess, t -> onFailure.run()),
                        ContextCompat.getMainExecutor(fragment.requireContext())
                );
            }
        });
    }

    public static ActivityResultLauncher<String> createImportTextLauncher(Fragment fragment,
                                                                          Consumer<String> onSuccess) {
        return createImportTextLauncher(fragment,
                () -> MyApplication.get(fragment.requireContext()).getThreadPool(),
                onSuccess,
                () -> Toast.makeText(fragment.requireContext(), R.string.import_failed, Toast.LENGTH_SHORT).show());
    }

    public static ActivityResultLauncher<String> createExportTextLauncher(Fragment fragment,
                                                                          Supplier<ListeningExecutorService> executorService,
                                                                          Supplier<String> textToExport,
                                                                          Runnable onSuccess,
                                                                          Runnable onFailure) {
        return fragment.registerForActivityResult(new ActivityResultContracts.CreateDocument("application/json"), result -> {
            final var future = executorService.get().<@Nullable Void>submit(() -> {
                final var outputStream = fragment.requireContext().getContentResolver().openOutputStream(result);
                try (final var writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                    writer.write(textToExport.get());
                }
                return null;
            });
            Futures.addCallback(
                    future,
                    new FutureCallbackLambdas<>(v -> onSuccess.run(), t -> onFailure.run()),
                    ContextCompat.getMainExecutor(fragment.requireContext())
            );
        });
    }

    public static ActivityResultLauncher<String> createExportTextLauncher(Fragment fragment,
                                                                          Supplier<String> textToExport) {
        return createExportTextLauncher(fragment,
                () -> MyApplication.get(fragment.requireContext()).getThreadPool(),
                textToExport,
                () -> Toast.makeText(fragment.requireContext(), R.string.export_successful, Toast.LENGTH_SHORT).show(),
                () -> Toast.makeText(fragment.requireContext(), R.string.export_failed, Toast.LENGTH_SHORT).show());
    }
}
