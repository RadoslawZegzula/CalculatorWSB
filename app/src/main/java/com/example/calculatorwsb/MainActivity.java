package com.example.calculatorwsb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    ImageButton imageButton;
    int stan = 0;
    int mikrofonNieaktywny = Color.parseColor("#FF0000");
    int mikrofonAktywny = Color.parseColor("#00FF00");
    SpeechRecognizer speechRecognizer;
    Double zmiennaA;
    Double zmiennaB;
    Double zmiennaWynik;
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
        imageButton = findViewById(R.id.button);
        resultTextView = findViewById(R.id.text_view_result);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        imageButton.setOnClickListener(view -> {
            if (stan == 0) {
                if(resultTextView.length()>0){
                    onClearButtonClicked();
                }
                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
                imageButton.setColorFilter(mikrofonAktywny);
                speechRecognizer.startListening(speechRecognizerIntent);
                stan = 1;
            } else {
                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                imageButton.setColorFilter(mikrofonNieaktywny);
                speechRecognizer.stopListening();
                stan = 0;
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {


            }

            @Override
            public void onError(int i) {
                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                imageButton.setColorFilter(mikrofonNieaktywny);
                stan = 0;
            }

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                final String regex = "^[\\d+\\-*\\./\" \\\"]+";
                String tekst = data.get(0);
                if (tekst.matches(regex)) {
                    String[] dane = tekst.split("\\s+");
                    zmiennaA = Double.valueOf(dane[0]);
                    zmiennaB = Double.valueOf(dane[2]);
                    switch (dane[1]) {
                        case "+":
                            zmiennaWynik = zmiennaA + zmiennaB;
                            resultTextView.setText(String.format("%.2f", zmiennaWynik));
                            break;
                        case "-":
                            zmiennaWynik = zmiennaA - zmiennaB;
                            resultTextView.setText(String.format("%.2f", zmiennaWynik));
                            break;
                        case "/":
                            zmiennaWynik = zmiennaA / zmiennaB;
                            resultTextView.setText(String.format("%.2f", zmiennaWynik));
                            break;
                        case "*":
                            zmiennaWynik = zmiennaA * zmiennaB;
                            resultTextView.setText(String.format("%.2f", zmiennaWynik));
                            break;
                    }
                } else {
                    resultTextView.setText("Powtórz...");
                }

                imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                imageButton.setColorFilter(mikrofonNieaktywny);
                speechRecognizer.stopListening();
                stan = 0;
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Pozwolenie udzielone", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Odmowa pozwolenia", Toast.LENGTH_SHORT);
            }
        }
    }


    //TODO: Od strony wybierania głosowego dodałem warunek sprawdzenia czy pole z wynikiem ma jakąś wartość.
    // Trzeba jeszcze dodać warunek aby kasować pole z wynikiem
    // w przypadku gdy wprowadzone zostało wybieranie głosowe w wyniku czego został wynik na ekranie
    // Obecnie po wybieraniu głosowym pozostaje wynik, następnie jak klikam na cyferki w celu wprowadzenia
    // obliczeń ręcznie wynik po wybieraniu głosowym nie jest kasowany.

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
        } catch (Exception e) {
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

        resultTextView = (TextView) findViewById(R.id.text_view_result);
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
        } catch (Exception e) {

        }
        mediaPlayer.start();
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
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

