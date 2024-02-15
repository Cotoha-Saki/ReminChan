package xyz.cotoha.program.reminchan.navigation;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.cotoha.program.reminchan.R;
import xyz.cotoha.program.reminchan.fragment.ChatFragment;
import xyz.cotoha.program.reminchan.fragment.HomeFragment;

public class BottomNavManager implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final FragmentManager fragmentManager;
    private final int containerId;

    public BottomNavManager(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    public void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Fragment selectedFragment = null;

        if (itemId == R.id.navigation_home) {
            // ホームフラグメントのインスタンスを作成
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.navigation_chat) {
            // チャットフラグメントのインスタンスを作成
            selectedFragment = new ChatFragment();
        }
        // 他のナビゲーションアイテムについても同様に処理

        if (selectedFragment != null) {
            fragmentManager.beginTransaction().replace(containerId, selectedFragment).commit();
        }

        return true; // 選択したアイテムに対応するフラグメントの表示が成功した場合は true を返すべきです
    }
}
