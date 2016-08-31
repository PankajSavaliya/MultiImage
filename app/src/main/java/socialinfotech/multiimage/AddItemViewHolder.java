package socialinfotech.multiimage;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by pankaj on 18/12/15.
 */
public class AddItemViewHolder extends MainViewHolder {

    @InjectView(R.id.cover_image_view)
    SimpleDraweeView imageView;

    @InjectView(R.id.remove)
    ImageButton remove;



    public AddItemViewHolder(View itemView) {

        super(itemView);
        ButterKnife.inject(this, itemView);

    }


}
