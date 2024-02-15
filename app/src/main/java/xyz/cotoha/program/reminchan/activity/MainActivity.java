package xyz.cotoha.program.reminchan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.cotoha.program.reminchan.R;
import xyz.cotoha.program.reminchan.fragment.HomeFragment;
import xyz.cotoha.program.reminchan.navigation.BottomNavManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        int fragmentContainerId = R.id.fragment_container;

        BottomNavManager bottomNavManager = new BottomNavManager(getSupportFragmentManager(), fragmentContainerId);
        bottomNavManager.setupBottomNavigation(bottomNav);

        if (savedInstanceState == null) {
            // アプリ起動時にHomeFragmentを表示
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        // BottomNavigationViewのセットアップロジックをここに追加
        // 例えば、BottomNavigationViewのアイテム選択リスナーを設定するなど
    }
}