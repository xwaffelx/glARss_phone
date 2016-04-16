package glarss.face.phone.glarssphone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PICTURE = 1;
    static final int REQUEST_CHOOSE_PHOTO = 2;
    Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initActions();
    }

    private void initActions() {
        final Button next_button = (Button) findViewById(R.id.next_btn);
        assert next_button != null;
        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button photo_button = (Button) findViewById(R.id.take_photo_btn);
        assert photo_button != null;
        photo_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        final Button galery_button = (Button) findViewById(R.id.choose_photo_btn);
        assert galery_button != null;
        galery_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchChoosePhotoIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CHOOSE_PHOTO) {
            assert data != null;
            photoUri = data.getData();
        }

        if (requestCode == REQUEST_TAKE_PICTURE) {
            // do nothing
        }

        if (resultCode == RESULT_OK) {
            ImageView photoView = (ImageView)findViewById(R.id.photo_frame);

            assert photoView != null;


            try {
                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                Drawable drwbl  = Drawable.createFromStream(inputStream, photoUri.toString() );
                photoView.setImageDrawable(drwbl);
            } catch (FileNotFoundException e) {
                // Do nothing
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Ignored, as usual...
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }

    }

    private void dispatchChoosePhotoIntent() {
        Intent choosePhotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choosePhotoIntent, REQUEST_CHOOSE_PHOTO);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        photoUri = Uri.fromFile(image);
        return image;
    }
}
