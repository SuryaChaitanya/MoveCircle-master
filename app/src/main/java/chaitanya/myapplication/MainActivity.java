package chaitanya.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final int SPEECH_REQUEST_CODE = 0;
    int x, y,height,width;
    float radius;

    ImageButton ib;
    ImageView circle;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ib = (ImageButton) findViewById(R.id.imageButton);

        circle = (ImageView) findViewById(R.id.imageView);

        width=this.getResources().getDisplayMetrics().widthPixels;
        height=this.getResources().getDisplayMetrics().heightPixels;

        Paint paint = new Paint();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        x=120;
        y=120;
        radius=100;

        Canvas canvas = new Canvas(bmp);
        canvas.drawCircle(x, y, radius, paint);
        circle.setImageBitmap(bmp);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            if(spokenText.equals("left"))x-=10;
            else if(spokenText.equals("right"))x+=10;
            else if(spokenText.equals("up"))y-=10;
            else if(spokenText.equals("down"))y+=10;
            else if(spokenText.equals("bigger")){
                if (radius<width/2)
                    radius+=10;
                else
                    Toast.makeText(this, "Attained max size", Toast.LENGTH_LONG).show();
            }
            else if(spokenText.equals("smaller")){
                if(radius>10)
                    radius-=10;
                else
                    Toast.makeText(this, "Attained min size", Toast.LENGTH_LONG).show();
            }

            else Toast.makeText(this, "Invalid command", Toast.LENGTH_LONG).show();


            Start();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void Start()
    {

        if(x>radius&&y>radius&&x<width&&y<height) {

            Paint paint = new Paint();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawCircle(x, y, radius, paint);
            circle.setImageBitmap(bmp);
        }
        else
        {
            Toast.makeText(this,"Getting out of range",Toast.LENGTH_LONG).show();
        }
    }

    public void click(View v)
    {
        if(v.getId()==R.id.imageButton)
        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        }

        Start();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),

                Uri.parse("android-app://chaitanya.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();


        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",

                Uri.parse("http://host/path"),

                Uri.parse("android-app://chaitanya.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
