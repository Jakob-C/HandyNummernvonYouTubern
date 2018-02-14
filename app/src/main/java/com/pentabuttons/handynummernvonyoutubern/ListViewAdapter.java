package com.pentabuttons.handynummernvonyoutubern;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.penta.games.handynummernvonyoutuber.R;

public class ListViewAdapter extends ArrayAdapter<String> {

    private String[] text;
    private String[] bar;
    private Integer[] image;
    private int[]progresses;
    private Activity context;

    public ListViewAdapter(Activity context, String[] text, Integer[] image, int[]progresses) {
        super(context, R.layout.listview_single, text);


        this.context = context;
        this.text = text;
        this.progresses = progresses;
        this.image = image;




    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){


            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_single, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) r.getTag();


        }

        viewHolder.imageView.setImageResource(image[position]);
        viewHolder.textView.setText(text[position]);
        viewHolder.textView2.setText("Fortschritt: " + progresses[position] + "%");
        viewHolder.progressBar.setProgress(progresses[position]);


        return r;


    }
    class ViewHolder{

        TextView textView;
        TextView textView2;
        ImageView imageView;
        ProgressBar progressBar;
        ViewHolder(View v){
            textView = v.findViewById(R.id.item_text);
            textView2 = v.findViewById(R.id.bar);
            imageView = v.findViewById(R.id.item_image);
            progressBar = v.findViewById(R.id.progressBar);
        }

    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}