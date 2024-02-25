package com.example.medswap.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.BlackLevelPattern;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.medswap.R;
import com.example.medswap.REPO.SharedViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class ScanFragment extends Fragment {
    private ImageButton capture, toggleFlash, flipCamera;
    private PreviewView previewView;
    private ProgressBar progressBar;
    private int cameraFacing = CameraSelector.LENS_FACING_BACK;
    private static final int REQUEST_PICK_IMAGE = 101;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private StorageReference storageReference;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance() {
        return new ScanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        previewView = view.findViewById(R.id.cameraPreview);
        capture = view.findViewById(R.id.capture);
        toggleFlash = view.findViewById(R.id.toggleFlash);
        progressBar = view.findViewById(R.id.cameraImgUploadProgressBar);
        flipCamera = view.findViewById(R.id.flipCamera);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // Explain to the user why you need the permission
                // You might want to show a dialog explaining the need for camera permission
            }
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera(cameraFacing);
        }

        flipCamera.setOnClickListener(view12 -> {
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }
            startCamera(cameraFacing);
        });

        ImageButton selectFromGalleryButton = view.findViewById(R.id.gallery);
        selectFromGalleryButton.setOnClickListener(view1 -> openGallery());

        return view;
    }

    public void startCamera(int cameraFacing) {
        int rotation = previewView.getDisplay() != null ? previewView.getDisplay().getRotation() : Surface.ROTATION_0;
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(requireContext());

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(rotation).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                capture.setOnClickListener(view -> {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Request storage permission
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Explain to the user why you need the permission
                            // You might want to show a dialog explaining the need for storage permission
                        }
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        capture.setClickable(false);
                        takePicture(imageCapture);
                    }
                });

                toggleFlash.setOnClickListener(view -> setFlashIcon(camera));

                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    public void takePicture(ImageCapture imageCapture) {
        File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                uploadImageToFirebase(file);
                SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                sharedViewModel.setImageFile(file);

                // Display the image, for example, by navigating to a new fragment
                displayImage();
                // Enable capture button and restart the camera preview
                capture.setClickable(true);

                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show());
                capture.setClickable(true);
                startCamera(cameraFacing);
            }
        });
    }

    private void displayImage() {
        // Perform actions to display the image or navigate to a new fragment
        // For example, you might want to show the image in an ImageView or navigate to a preview fragment
        BlankFragment previewFragment = new BlankFragment();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_home_screen, previewFragment)
                .addToBackStack(null)
                .commit();
    }


    private void setFlashIcon(Camera camera) {
        requireActivity().runOnUiThread(() -> {
            if (camera.getCameraInfo().hasFlashUnit()) {
                if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                    camera.getCameraControl().enableTorch(true);
                    toggleFlash.setImageResource(R.drawable.baseline_flash_off_24);
                } else {
                    camera.getCameraControl().enableTorch(false);
                    toggleFlash.setImageResource(R.drawable.baseline_flash_on_24);
                }
            } else {
                Toast.makeText(requireContext(), "Flash is not available currently", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private void showToastOnUiThread(final String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show());
    }

    private File uploadImageToFirebase(File imageFile) {
        requireActivity().runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        if (isNetworkAvailable()) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            String imageName = "images/" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = storageRef.child(imageName);

            imageRef.putFile(Uri.fromFile(imageFile))
                    .addOnSuccessListener(taskSnapshot -> {
                        requireActivity().runOnUiThread(() -> {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                        showToastOnUiThread("Uploaded Successfully");
                        if (imageFile.exists()) {
                            imageFile.delete();
                        }

                    })
                    .addOnFailureListener(exception -> {
                        exception.printStackTrace();
                        requireActivity().runOnUiThread(() -> {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            showToastOnUiThread("Failed to upload image to Firebase");
                        });
                    });
        } else {
            requireActivity().runOnUiThread(() -> {
                if (progressBar != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
                showToastOnUiThread("No internet connection");
            });
        }

        return imageFile;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            if (isNetworkAvailable()) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                String imageName = "images/" + System.currentTimeMillis() + ".jpg";
                StorageReference imageRef = storageRef.child(imageName);

                SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                File selectedImageFile = new File(selectedImageUri.getPath());
                sharedViewModel.setImageFile(selectedImageFile);

                // Display the image, for example, by navigating to a new fragment


                ProgressDialog uploadDialog = new ProgressDialog(requireContext());
                uploadDialog.setTitle("Uploading Image");
                uploadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                uploadDialog.setCancelable(false);
                uploadDialog.show();

                imageRef.putFile(selectedImageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                uploadDialog.dismiss();
                                displayImage();
                                Toast.makeText(requireContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(exception -> {
                            exception.printStackTrace();
                            uploadDialog.dismiss();
                            Toast.makeText(requireContext(), "Failed to upload image to Firebase", Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        })
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            int currentProgress = (int) progress;
                            uploadDialog.setProgress(currentProgress);
                            uploadDialog.setMessage("Uploading... " + currentProgress + "%");
                        });
            } else {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}
