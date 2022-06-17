package csu.soc.xwz.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.pojo.SelfList_Content;

public class SelfAdapter extends RecyclerView.Adapter<SelfAdapter.ViewHolder> {
    private List<SelfList_Content> musicList;
    private Context context;

    public interface MyOnItemClickListener {
        void OnItemClickListener(View itemView);
    }

    static SelfAdapter.MyOnItemClickListener myOnItemClickListener;

    public void setMyOnItemClickListener(SelfAdapter.MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener =  myOnItemClickListener;
    }

    public SelfAdapter(Context c, ArrayList<SelfList_Content> selfArrayList) {
        this.context = c;
        this.musicList = selfArrayList;
    }
    //用于根据布局文件生成view
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView musicImage;
        TextView musicName;

        public ViewHolder(@NonNull View view) {
            super(view);
            musicImage = (ImageView)view.findViewById(R.id.music_image);
            musicName = (TextView)view.findViewById(R.id.music_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myOnItemClickListener != null) {
                        myOnItemClickListener.OnItemClickListener(view);
                    }
                }
            });
        }
    }

    public void addData(SelfList_Content selfList_content){
        musicList.add(selfList_content);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelfList_Content self = musicList.get(position);
        holder.musicImage.setImageResource(self.getImgId());
        holder.musicName.setText(self.getSelfName());
    }
    @Override
    public int getItemCount(){
        return musicList.size();
    }

}
