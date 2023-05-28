
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonPlay;
    private Button buttonPause;
    private Button buttonStop;
    private Button buttonBass;
    private BassBoost bassBoost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);
        buttonStop = findViewById(R.id.buttonStop);
        buttonBass = findViewById(R.id.buttonBass);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start playback logic here
                Toast.makeText(MainActivity.this, "Play button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pause playback logic here
                Toast.makeText(MainActivity.this, "Pause button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop playback logic here
                Toast.makeText(MainActivity.this, "Stop button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBassBoost();
            }
        });

        // Set background image
        getWindow().setBackgroundDrawableResource(R.drawable.song_background);

        // Access internal and external storage for songs
        String internalPath = getFilesDir().getAbsolutePath();
        String externalPath = getExternalFilesDir(null).getAbsolutePath();
    }

    private void toggleBassBoost() {
        if (bassBoost == null) {
            BassBoost.Settings settings = new BassBoost.Settings();
            bassBoost = new BassBoost(0, 0);
            bassBoost.setProperties(settings);
            bassBoost.setEnabled(true);
            Toast.makeText(MainActivity.this, "Bass Boost Enabled", Toast.LENGTH_SHORT).show();
        } else {
            bassBoost.setEnabled(!bassBoost.getEnabled());
            if (bassBoost.getEnabled()) {
                Toast.makeText(MainActivity.this, "Bass Boost Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Bass Boost Disabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bassBoost != null) {
            bassBoost.release();
            bassBoost = null;
        }
    }
}
