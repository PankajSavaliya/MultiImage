package socialinfotech.multiimage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by pankaj on 18/12/15.
 */
public class AddAdapter extends RecyclerView.Adapter<MainViewHolder> {
    private ArrayList<UploadInfo> groups = new ArrayList<UploadInfo>();
    private Context mContext;


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup vGroup0 = (ViewGroup) mInflater.inflate(R.layout.raw_add_image, viewGroup, false);
        return new AddItemViewHolder(vGroup0);

    }

    @Override
    public void onBindViewHolder(MainViewHolder viewHolder, final int i) {
        UploadInfo info = groups.get(i);


        AddItemViewHolder vhGroup0 = (AddItemViewHolder) viewHolder;
        vhGroup0.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(i);
            }
        });
        vhGroup0.imageView.setImageURI(Uri.fromFile(info.getmThumbnile()));

    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public AddAdapter(Activity context) {
        this.mContext = context;
        this.groups = new ArrayList<UploadInfo>();
    }

    public void add(UploadInfo sample) {
        this.groups.add(sample);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        this.groups.remove(position);
        notifyDataSetChanged();
    }

    public void Clear() {
        this.groups.clear();
        notifyDataSetChanged();
    }


    public ArrayList<UploadInfo> getData() {
        return groups;
    }
}