import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private List<String> songPaths;
    private int currentSongIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        songPaths = new ArrayList<>();
        currentSongIndex = 0;

        Button playButton = findViewById(R.id.playButton);
        Button stopButton = findViewById(R.id.stopButton);
        Button nextButton = findViewById(R.id.nextButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songPaths.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No songs found", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mediaPlayer.isPlaying()) {
                    playSong(songPaths.get(currentSongIndex));
                } else {
                    mediaPlayer.pause();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                mediaPlayer.seekTo(0);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songPaths.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No songs found", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentSongIndex++;
                if (currentSongIndex >= songPaths.size()) {
                    currentSongIndex = 0;
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                playSong(songPaths.get(currentSongIndex));
            }
        });

        checkPermissionsAndReadSongs();
    }

    private void checkPermissionsAndReadSongs() {
        // Check if the app has permission to access external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if it has not been granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            // Read songs from internal and external storage
            readSongsFromInternalStorage();
            readSongsFromExternalStorage();
        }
    }

    private void readSongsFromInternalStorage() {
        File internalStorage = Environment.getExternalStorageDirectory();
        String internalStoragePath = internalStorage.getAbsolutePath();
        readSongsFromDirectory(internalStoragePath);
    }

    private void readSongsFromExternalStorage() {
        File externalStorage = getExternalFilesDir(null);
        if (externalStorage != null) {
            String externalStoragePath = externalStorage.getAbsolutePath();
            readSongsFromDirectory(externalStoragePath);
        }
    }

    private void readSongsFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isAudioFile(file.getName())) {
                        songPaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private boolean isAudioFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("mp3") || extension.equalsIgnoreCase("wav");
    }

    private void playSong(String songPath) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(songPath));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error playing song", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}
