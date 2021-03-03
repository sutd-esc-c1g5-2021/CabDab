package com.example.cabdab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SaveFileActivity extends AppCompatActivity implements SaveOverwriteDialog.SaveOverwriteListener {
    private final static String DUMMY_FILE_NAME = "test.txt";
    private EditText mEditText;
    private EditText mFileNameText;
    private TextView mTextView;
    private Button saveButton, loadButton;
    private String fileName,text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file);
        mEditText = findViewById(R.id.editText1);
        mFileNameText = findViewById(R.id.editText2);
        mTextView = findViewById(R.id.textView);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSave();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileName = mFileNameText.getText().toString();
                fileName = fileName.concat(".txt");
                load(fileName);
            }
        });
    }
    public void checkSave(){
        text = mEditText.getText().toString();
        fileName = mFileNameText.getText().toString();
        fileName = fileName.concat(".txt");
        if(isFilePresent(fileName)){
            showOverwriteDialog();
        }
        else{
            save(fileName, text);
        }
    }

    public void save(String fileName, String content){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(content.getBytes());
            mEditText.setText("SomeContent");
            Toast.makeText(this, "Save Successful", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void load(String fileName){
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String input;

            while((input = br.readLine())!=null){
                sb.append(input).append(" ");
            }
            mTextView.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        System.out.println("DialogConfirmed");
        save(fileName, text);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mEditText.setText("SomeContent");
    }

    public boolean isFilePresent(String filename){
        String path = getBaseContext().getFilesDir()+"/"+filename;
        File file = new File(path);
        return file.exists();
    }

    public void showOverwriteDialog(){
        DialogFragment dialog = new SaveOverwriteDialog();
        dialog.show(getSupportFragmentManager(),"SaveOverwriteDialog");
    }
}
