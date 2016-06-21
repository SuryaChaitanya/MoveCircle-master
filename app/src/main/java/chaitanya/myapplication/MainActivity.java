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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends AppCompatActivity  {
    ImageButton ib;
    private static final int SPEECH_REQUEST_CODE = 0;
    TextView tv;

    Button right,left,up,down;
    int x, y,height,width;
    float radius;

    ImageView circle;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ib = (ImageButton) findViewById(R.id.imageButton);
        tv = (TextView) findViewById(R.id.textView);
        left=(Button)findViewById(R.id.button1);
        right=(Button)findViewById(R.id.button2);
        up=(Button)findViewById(R.id.button3);
        down=(Button)findViewById(R.id.button4);

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


            tv.setText("" + spokenText);

            if(spokenText.equals("left"))x-=10;
            else if(spokenText.equals("right"))x+=10;
            else if(spokenText.equals("up"))y-=10;
            else if(spokenText.equals("down"))y+=10;
            else Toast.makeText(this, "Invalid command", Toast.LENGTH_LONG).show();


            // Do something with spokenText
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


        /*
        if(v.getId()==R.id.button1)
        {
            Toast.makeText(this, "left button", Toast.LENGTH_SHORT).show();
            x=x-10;

        }
        else if(v.getId()==R.id.button2)
        {
            Toast.makeText(this, "right button", Toast.LENGTH_SHORT).show();
            x=x+10;

        }
        else if(v.getId()==R.id.button3)
        {
            Toast.makeText(this, "up button", Toast.LENGTH_SHORT).show();
            y=y+10;

        }
        else if(v.getId()==R.id.button4)
        {
            Toast.makeText(this, "down button", Toast.LENGTH_SHORT).show();
            y=y+10;
        }

        */

        if(v.getId()==R.id.imageButton)
        {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            // Start the activity, the intent will be populated with the speech text
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
