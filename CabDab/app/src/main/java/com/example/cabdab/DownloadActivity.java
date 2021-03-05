package com.example.cabdab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{
    private StorageReference storageRef;
    private StorageReference pathReference;
    private Button downloadBtn, showFile;
    private EditText inputFileName;
    private ImageView fileView;
    private FirebaseStorage storage;
    private StorageReference ref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        storageRef = FirebaseStorage.getInstance().getReference();

        pathReference = storageRef.child("images/Floor plan 1.png");
//        StorageReference gsReference = storage.getReferenceFromUrl("gs://esc-c1g5-2021.appspot.com/images/Floor plan 1.jpg");

        inputFileName = (EditText) findViewById(R.id.inputFileName);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);
        showFile = (Button) findViewById(R.id.showFile);
        fileView = (ImageView) findViewById(R.id.fileView);



        downloadBtn.setOnClickListener(this);
        showFile.setOnClickListener(this);

    }

    private void getFile(StorageReference fileRef){
        if (fileRef != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage(null);
            progressDialog.show();

            try {
                final File localFile = File.createTempFile("images", "jpg");

                fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        fileView.setImageBitmap(bmp);
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(DownloadActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        // percentage in progress dialog
                        progressDialog.setMessage("Downloaded " + ((int) progress) + "%...");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(DownloadActivity.this, "Upload file before downloading", Toast.LENGTH_LONG).show();
        }
    }
    private void downloadInMemory(StorageReference fileRef) {
        if (fileRef != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading...");
            progressDialog.setMessage(null);
            progressDialog.show();

            final long ONE_MEGABYTE = 1024 * 1024;
            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    fileView.setImageBitmap(bmp);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(DownloadActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(DownloadActivity.this, "Upload file before downloading", Toast.LENGTH_LONG).show();
        }
    }


    private void showFile() throws IOException {

    }


    @Override
    public void onClick(View v) {
        if (v == downloadBtn){
            ref = storageRef.child("images/Help.jpeg");
            downloadInMemory(ref);
        }
        else if (v == showFile){
            try {
                showFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}