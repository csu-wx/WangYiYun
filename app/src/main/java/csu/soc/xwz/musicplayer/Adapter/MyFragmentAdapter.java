package csu.soc.xwz.musicplayer.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import csu.soc.xwz.musicplayer.fragment.BroadcastFragment;
import csu.soc.xwz.musicplayer.fragment.LibraryFragment;
import csu.soc.xwz.musicplayer.fragment.MineFragment;

public class MyFragmentAdapter extends FragmentStateAdapter {

    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new LibraryFragment();
            case 2:
                return new BroadcastFragment();
        }
        MineFragment mineFragment = new MineFragment();
        Log.d("fragment","主fragment已经创建");
        return mineFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
