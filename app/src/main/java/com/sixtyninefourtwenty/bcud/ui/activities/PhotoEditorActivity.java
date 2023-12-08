package com.sixtyninefourtwenty.bcud.ui.activities;

import static java.util.Objects.requireNonNull;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.DialogFragment;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.MaterialColorPicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.Futures;
import com.sixtyninefourtwenty.bcud.MyApplication;
import com.sixtyninefourtwenty.bcud.R;
import com.sixtyninefourtwenty.bcud.databinding.ActivityPhotoEditorBinding;
import com.sixtyninefourtwenty.bcud.databinding.DialogImgEditAddTextBinding;
import com.sixtyninefourtwenty.bcud.databinding.DialogImgEditBrushSettingsBinding;
import com.sixtyninefourtwenty.bcud.databinding.DialogImgEditExportBinding;
import com.sixtyninefourtwenty.bcud.utils.BitmapExport;
import com.sixtyninefourtwenty.bcud.utils.MyOnPhotoEditorListener;
import com.sixtyninefourtwenty.bcud.utils.OnSaveBitmapFuture;
import com.sixtyninefourtwenty.bcud.utils.fragments.BaseViewBindingDialogFragment;
import com.sixtyninefourtwenty.common.misc.NoAutoDismissAlertDialogBuilder;
import com.sixtyninefourtwenty.common.utils.FutureCallbackLambdas;
import com.sixtyninefourtwenty.javastuff.concurrent.FutureContainer;
import com.sixtyninefourtwenty.javastuff.concurrent.LifecycleAwareFutureContainer;
import com.sixtyninefourtwenty.stuff.Bundles;
import com.sixtyninefourtwenty.stuff.Snackbars;
import com.sixtyninefourtwenty.stuff.Views;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder;
import ja.burhanrashid52.photoeditor.shape.ShapeType;

public final class PhotoEditorActivity extends AppCompatActivity {

    private ActivityPhotoEditorBinding binding;
    private Bitmap.CompressFormat formatToExport = null;
    private Bitmap bitmapToExport = null;
    private int qualityToExport = -1;
    private View currentTextView;
    private PhotoFilter currFilter = PhotoFilter.NONE;
    private FutureContainer futureContainer;

    private final ActivityResultLauncher<String> saveEditedImg = registerForActivityResult(new ActivityResultContracts.CreateDocument("image/*"), result -> {
        if (result != null) {
            if (bitmapToExport != null && formatToExport != null && qualityToExport != -1) {
                final var future = futureContainer.addAndReturn(BitmapExport.exportBitmap(
                        MyApplication.get(this).getThreadPool(),
                        result,
                        this,
                        new BitmapExport.Input(bitmapToExport, formatToExport, qualityToExport)
                ));
                Futures.addCallback(future,
                        new FutureCallbackLambdas<>(
                                success -> {
                                    if (success) {
                                        Snackbars.makeSnackbar(binding.getRoot(), R.string.export_success)
                                                .setAction(R.string.share, v -> startActivity(Intent.createChooser(
                                                        new Intent(Intent.ACTION_SEND)
                                                                .setType(getContentResolver().getType(result))
                                                                .putExtra(Intent.EXTRA_STREAM, result), null)))
                                                .show();
                                    } else {
                                        Snackbars.makeSnackbar(binding.getRoot(), R.string.failed_save_img).show();
                                    }
                                },
                                t -> Snackbars.makeSnackbar(binding.getRoot(), R.string.failed_save_img).show()
                        ),
                        ContextCompat.getMainExecutor(this)
                );
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        futureContainer = new LifecycleAwareFutureContainer(this);
        binding = ActivityPhotoEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final var srcImageView = binding.photoEditor.getSource();
        final var bitmap = IntentCompat.getParcelableExtra(getIntent(), "img_to_edit", Bitmap.class);
        if (bitmap != null) {
            srcImageView.setImageBitmap(bitmap);
        } else {
            srcImageView.setImageResource(R.mipmap.ic_launcher);
        }
        final var editor = new PhotoEditor.Builder(this, binding.photoEditor).build();
        editor.setFilterEffect(currFilter);
        editor.setOnPhotoEditorListener((MyOnPhotoEditorListener) (view, s, i) -> {
            currentTextView = view;
            AddEditTextDialog.newInstance(s, i).show(getSupportFragmentManager(), null);
        });
        binding.undo.setOnClickListener(v -> editor.undo());
        binding.redo.setOnClickListener(v -> editor.redo());
        binding.reset.setOnClickListener(v -> {
            if (!editor.isCacheEmpty()) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.reset)
                        .setMessage(R.string.reset_image_msg)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> editor.clearAllViews())
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            } else {
                Snackbar.make(binding.getRoot(), R.string.nothing_to_reset, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        binding.modeIndicator.setText(R.string.img_editor);
        binding.text.setOnClickListener(v -> AddEditTextDialog.newInstance("", 0).show(getSupportFragmentManager(), null));
        binding.brush.setOnClickListener(v -> {
            binding.modeIndicator.setText(R.string.brush);
            editor.setBrushDrawingMode(true);
        });
        binding.eraser.setOnClickListener(v -> {
            binding.modeIndicator.setText(R.string.eraser);
            editor.brushEraser();
        });
        binding.settings.setOnClickListener(v -> Views.toggleVisibility(binding.brushSettings));
        binding.brushSettings.setOnClickListener(v -> BrushSettingsDialog.newInstance(editor.getBrushSize(), editor.getBrushColor(), 0).show(getSupportFragmentManager(), null));
        binding.save.setOnClickListener(v -> editor.saveAsBitmap((OnSaveBitmapFuture) future -> Futures.addCallback(future,
                new FutureCallbackLambdas<>(
                        result -> {
                            bitmapToExport = result;
                            new ExportImageDialog().show(getSupportFragmentManager(), null);
                        },
                        t -> Snackbars.makeSnackbar(binding.getRoot(), R.string.failed_save_img).show()
                ),
                ContextCompat.getMainExecutor(this)))
        );
        binding.filter.setOnClickListener(v -> FiltersDialog.newInstance(currFilter).show(getSupportFragmentManager(), null));

        getSupportFragmentManager().setFragmentResultListener(AddEditTextDialog.ADD_TEXT_REQUEST_KEY, this, (requestKey, result) ->
                editor.addText(result.getString("new_text"), result.getInt("new_color")));
        getSupportFragmentManager().setFragmentResultListener(AddEditTextDialog.EDIT_TEXT_REQUEST_KEY, this, (requestKey, result) ->
                editor.editText(currentTextView, result.getString("new_text"), result.getInt("new_color")));
        getSupportFragmentManager().setFragmentResultListener(ExportImageDialog.EXPORT_IMG_REQUEST_KEY, this, (requestKey, result) -> {
            formatToExport = requireNonNull(Bundles.getSerializableCompat(result, "img_format", Bitmap.CompressFormat.class));
            qualityToExport = result.getInt("quality");
            saveEditedImg.launch(Long.toString(System.currentTimeMillis()));
        });
        getSupportFragmentManager().setFragmentResultListener(BrushSettingsDialog.BRUSH_SETTINGS_REQUEST_KEY, this, (requestKey, result) -> {
            final var builder = new ShapeBuilder();
            switch (result.getInt("shape_type")) {
                case 0 -> builder.withShapeType(ShapeType.Brush.INSTANCE);
                case 1 -> builder.withShapeType(ShapeType.Line.INSTANCE);
                case 2 -> builder.withShapeType(ShapeType.Oval.INSTANCE);
                case 3 -> builder.withShapeType(ShapeType.Rectangle.INSTANCE);
            }
            editor.setShape(builder.withShapeSize(result.getFloat("shape_size"))
                    .withShapeColor(result.getInt("shape_color"))
                    .withShapeOpacity(result.getInt("shape_opacity")));
        });
        getSupportFragmentManager().setFragmentResultListener(FiltersDialog.FILTER_REQUEST_KEY, this, (requestKey, result) -> {
            currFilter = requireNonNull(Bundles.getSerializableCompat(result, "new_filter", PhotoFilter.class));
            editor.setFilterEffect(currFilter);
        });
    }

    public static final class AddEditTextDialog extends BaseViewBindingDialogFragment<DialogImgEditAddTextBinding> {

        public static final String EDIT_TEXT_REQUEST_KEY = "edit_text_request";
        public static final String ADD_TEXT_REQUEST_KEY = "add_text_request";

        public static AddEditTextDialog newInstance(String text, int color) {
            final var dialog = new AddEditTextDialog();
            dialog.setArguments(Bundles.createBundle(bundle -> {
                bundle.putString("text", text);
                bundle.putInt("color", color);
            }));
            return dialog;
        }

        @Override
        protected @NonNull DialogImgEditAddTextBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogImgEditAddTextBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogImgEditAddTextBinding binding, @Nullable Bundle savedInstanceState) {
            final var args = requireArguments();
            final var currentText = args.getString("text");
            final var currentColor = args.getInt("color");
            final var areWeEditingText = !TextUtils.isEmpty(currentText);
            final var currentlySelectedColor = new int[1];
            currentlySelectedColor[0] = currentColor == 0 ? Color.BLACK : currentColor;
            binding.textInputLayout.setHint(areWeEditingText ? R.string.enter_text_to_replace : R.string.enter_text_to_add);
            binding.textInput.setText(currentText);
            binding.currentColor.setBackgroundColor(currentlySelectedColor[0]);
            binding.pickColor.setOnClickListener(v -> new ColorPickerDialog.Builder(requireContext())
                    .setDefaultColor(currentlySelectedColor[0])
                    .setColorListener((color, colorHex) -> {
                        binding.currentColor.setBackgroundColor(color);
                        currentlySelectedColor[0] = color;
                    })
                    .show());
            binding.pickColorSimple.setOnClickListener(v -> new MaterialColorPicker.Builder(requireContext())
                    .setTitle(R.string.color_picker_simple)
                    .setPositiveButton(android.R.string.ok)
                    .setNegativeButton(android.R.string.cancel)
                    .setColorListener((color, colorHex) -> {
                        binding.currentColor.setBackgroundColor(color);
                        currentlySelectedColor[0] = color;
                    })
                    .showBottomSheet(getChildFragmentManager()));
            return new NoAutoDismissAlertDialogBuilder(requireContext())
                    .setTitle(areWeEditingText ? R.string.edit_text : R.string.add_text)
                    .setView(binding.getRoot())
                    .setPositiveButton(android.R.string.ok, false, (dialog, which) -> {
                        if (Views.isBlank(binding.textInput)) {
                            binding.textInput.setError(getText(R.string.please_enter_text));
                        } else {
                            final var bundle = Bundles.createBundle(b -> {
                                b.putString("new_text", Views.getInput(binding.textInput));
                                b.putInt("new_color", currentlySelectedColor[0]);
                            });
                            if (areWeEditingText) {
                                requireActivity().getSupportFragmentManager().setFragmentResult(EDIT_TEXT_REQUEST_KEY, bundle);
                            } else {
                                requireActivity().getSupportFragmentManager().setFragmentResult(ADD_TEXT_REQUEST_KEY, bundle);
                            }
                            dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, true, null)
                    .create();
        }
    }

    public static final class BrushSettingsDialog extends BaseViewBindingDialogFragment<DialogImgEditBrushSettingsBinding> {

        private final int[] currentlySelectedColor = new int[1];
        public static final String BRUSH_SETTINGS_REQUEST_KEY = "brush_settings";

        public static BrushSettingsDialog newInstance(float brushSize, int brushColor, int brushOpacity) {
            final var dialog = new BrushSettingsDialog();
            dialog.setArguments(Bundles.createBundle(bundle -> {
                bundle.putFloat("brush_size", brushSize);
                bundle.putInt("brush_color", brushColor);
                bundle.putInt("brush_opacity", brushOpacity);
            }));
            return dialog;
        }

        @Override
        protected @NonNull DialogImgEditBrushSettingsBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogImgEditBrushSettingsBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogImgEditBrushSettingsBinding binding, @Nullable Bundle savedInstanceState) {
            final var args = requireArguments();
            final var brushSize = args.getFloat("brush_size");
            final var brushColor = args.getInt("brush_color");
            final var brushOpacity = args.getInt("brush_opacity");
            binding.brushSize.setValue(brushSize);
            binding.brushOpacity.setValue(brushOpacity);
            currentlySelectedColor[0] = brushColor;
            binding.currentColor.setBackgroundColor(brushColor);
            binding.pickColor.setOnClickListener(v -> new ColorPickerDialog.Builder(requireContext())
                    .setDefaultColor(currentlySelectedColor[0])
                    .setColorListener((color, colorHex) -> {
                        binding.currentColor.setBackgroundColor(color);
                        currentlySelectedColor[0] = color;
                    })
                    .show());
            binding.pickColorSimple.setOnClickListener(v -> new MaterialColorPicker.Builder(requireContext())
                    .setTitle(R.string.color_picker_simple)
                    .setPositiveButton(android.R.string.ok)
                    .setNegativeButton(android.R.string.cancel)
                    .setColorListener((color, colorHex) -> {
                        binding.currentColor.setBackgroundColor(color);
                        currentlySelectedColor[0] = color;
                    })
                    .showBottomSheet(getChildFragmentManager()));
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(binding.getRoot())
                    .setTitle(R.string.brush_settings)
                    .setPositiveButton(R.string.apply, (dialog, which) -> {
                        final var bundle = new Bundle();
                        if (binding.brush.isChecked()) {
                            bundle.putInt("shape_type", 0);
                        } else if (binding.line.isChecked()) {
                            bundle.putInt("shape_type", 1);
                        } else if (binding.oval.isChecked()) {
                            bundle.putInt("shape_type", 2);
                        } else if (binding.rectangle.isChecked()) {
                            bundle.putInt("shape_type", 3);
                        } else {
                            showToast(R.string.please_select_a_brush_type);
                            dismiss();
                            return;
                        }
                        bundle.putFloat("shape_size", binding.brushSize.getValue());
                        bundle.putInt("shape_opacity", (int) binding.brushOpacity.getValue());
                        bundle.putInt("shape_color", currentlySelectedColor[0]);
                        requireActivity().getSupportFragmentManager().setFragmentResult(BRUSH_SETTINGS_REQUEST_KEY, bundle);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
    }

    public static final class ExportImageDialog extends BaseViewBindingDialogFragment<DialogImgEditExportBinding> {

        public static final String EXPORT_IMG_REQUEST_KEY = "export_img_request";

        @Override
        protected @NonNull DialogImgEditExportBinding initBinding(@NonNull LayoutInflater inflater) {
            return DialogImgEditExportBinding.inflate(inflater);
        }

        @Override
        protected @NonNull Dialog initDialog(@NonNull DialogImgEditExportBinding binding, @Nullable Bundle savedInstanceState) {
            binding.png.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.compressionStr.setVisibility(View.GONE);
                    binding.compressionNote.setVisibility(View.GONE);
                    binding.compression.setVisibility(View.GONE);
                } else {
                    binding.compressionStr.setVisibility(View.VISIBLE);
                    binding.compressionNote.setVisibility(View.VISIBLE);
                    binding.compression.setVisibility(View.VISIBLE);
                }
            });
            binding.png.setChecked(true);
            return new MaterialAlertDialogBuilder(requireContext())
                    .setView(binding.getRoot())
                    .setTitle(R.string.export_edited_image)
                    .setPositiveButton(R.string.export, ((dialog, which) -> {
                        final var bundle = new Bundle();
                        if (binding.png.isChecked()) {
                            bundle.putSerializable("img_format", Bitmap.CompressFormat.PNG);
                        }
                        if (binding.jpeg.isChecked()) {
                            bundle.putSerializable("img_format", Bitmap.CompressFormat.JPEG);
                            bundle.putInt("quality", 100 - Math.round(binding.compression.getValue()));
                        }
                        if (binding.webp.isChecked()) {
                            bundle.putSerializable("img_format", Bitmap.CompressFormat.WEBP);
                            bundle.putInt("quality", 100 - Math.round(binding.compression.getValue()));
                        }
                        requireActivity().getSupportFragmentManager().setFragmentResult(EXPORT_IMG_REQUEST_KEY, bundle);
                        //WEBP_LOSSY and WEBP_LOSSLESS require android 11+
                    }))
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
    }

    public static final class FiltersDialog extends DialogFragment {

        public static final String FILTER_REQUEST_KEY = "filter_request_key";

        public static FiltersDialog newInstance(PhotoFilter filter) {
            final var dialog = new FiltersDialog();
            dialog.setArguments(Bundles.createBundle(bundle -> bundle.putSerializable("current_filter", filter)));
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final var currFilter = requireNonNull(Bundles.getSerializableCompat(requireArguments(), "current_filter", PhotoFilter.class));
            final var filterValues = PhotoFilter.values();
            int index = -1;
            for (final var filter : filterValues) {
                if (currFilter == filter) {
                    index = filter.ordinal();
                    break;
                }
            }
            final int[] option = {index};
            return new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.filters)
                    .setSingleChoiceItems(R.array.img_editor_filters, index, (dialog, which) -> option[0] = which)
                    .setPositiveButton(R.string.apply, (dialog, which) -> {
                        for (final var filter : filterValues) {
                            if (option[0] == filter.ordinal()) {
                                requireActivity().getSupportFragmentManager().setFragmentResult(FILTER_REQUEST_KEY, Bundles.createBundle(bundle -> bundle.putSerializable("new_filter", filter)));
                                break;
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
    }

}