package csu.soc.xwz.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import csu.soc.xwz.musicplayer.BroadcastFragment;
import csu.soc.xwz.musicplayer.LibraryFragment;
import csu.soc.xwz.musicplayer.MineFragment;

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
        return new MineFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
