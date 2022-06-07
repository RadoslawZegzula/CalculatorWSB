package com.example.calculatorwsb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    Button buttonAdd;
    Button buttonSubstrate;
    Button buttonMul;
    Button buttonDiv;
    Button buttonClear;
    Button buttonEqual;
    String result;
    String tmp;
    String operator;
    TextView resultTextView;

    public MainActivity() throws IOException {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControl();
        initControlListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initControlListener() {
        button0.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button1.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("1");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button2.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("2");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button3.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button4.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("4");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button5.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("5");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button6.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("6");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button7.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("7");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button8.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        button9.setOnClickListener(v -> {
            try {
                onNumberButtonClicked("9");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buttonClear.setOnClickListener(v -> onClearButtonClicked());
        buttonSubstrate.setOnClickListener(v -> onOperatorButtonClicked("-"));
        buttonAdd.setOnClickListener(v -> onOperatorButtonClicked("+"));
        buttonMul.setOnClickListener(v -> onOperatorButtonClicked("X"));
        buttonDiv.setOnClickListener(v -> onOperatorButtonClicked("/"));
        buttonEqual.setOnClickListener(v -> onEqualButtonClicked());

    }

    private void onEqualButtonClicked() {
        int res = 0;
        try {
            int number = Integer.parseInt(tmp);
            int number2 = Integer.parseInt(resultTextView.getText().toString());
            switch (operator) {
                case "+":
                    res = number + number2;
                    break;
                case "/":
                    res = number / number2;
                    break;
                case "-":
                    res = number - number2;
                    break;
                case "X":
                    res = number * number2;
                    break;
            }
            result = String.valueOf(res);
            resultTextView.setText(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onOperatorButtonClicked(String operator) {
        tmp = resultTextView.getText().toString();
        onClearButtonClicked();
        this.operator = operator;
    }

    private void onClearButtonClicked() {
        result = "";
        resultTextView.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onNumberButtonClicked(String pos) throws IOException {
        result = resultTextView.getText().toString();
        result = result + pos;
        resultTextView.setText(result);
        playSoundFromNetwork(pos);
        new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view)).execute("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");
    }



    private void initControl() {
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        buttonAdd = findViewById(R.id.buttonAdd);
        buttonClear = findViewById(R.id.buttonClear);
        buttonSubstrate = findViewById(R.id.buttonSub);
        buttonMul = findViewById(R.id.buttonMul);
        buttonDiv = findViewById(R.id.buttonDiv);
        buttonEqual = findViewById(R.id.buttonEqual);

        resultTextView = (TextView)findViewById(R.id.text_view_result);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void playSoundFromNetwork(String num) {
        String url = "https://evolution.voxeo.com/library/audio/prompts/numbers/" + num + ".wav";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
            new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        );
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch(Exception e) {
            //TODO: obsluga wyjatku
        }
        mediaPlayer.start();
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage=BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

