package csu.soc.xwz.musicplayer.MineFragmentFolder;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import csu.soc.xwz.musicplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelfBuildMusicFragment extends Fragment {


    public SelfBuildMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_build_music, container, false);
    }

}
