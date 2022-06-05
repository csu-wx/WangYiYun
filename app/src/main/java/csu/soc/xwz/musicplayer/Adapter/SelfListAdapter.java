package csu.soc.xwz.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csu.soc.xwz.musicplayer.MineFragmentFolder.SelfList_Content;
import csu.soc.xwz.musicplayer.R;

public class SelfListAdapter extends ArrayAdapter {
    private final int resourceId;

    //适配器 参数内容：上下文，列表布局文件，数据源
    public SelfListAdapter(Context context, int resource, List<SelfList_Content> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent ){
        //获取当前实例
        SelfList_Content selfList_content = (SelfList_Content) getItem(position);
        //实例化一个对象
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        //获取布局内的视图
        ImageView selfImage =(ImageView) view.findViewById(R.id.self_image);
        TextView selfName= (TextView) view.findViewById(R.id.self_name);
        //为图片视图设置资源
        selfImage.setImageResource(selfList_content.getImgId());
        //为文本视图设置文本内容
        selfName.setText(selfList_content.getSelfName());
        return view;
    }
}
