package diana.cabinetmedical;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Diane on 6/13/2016.
 */
public class UserAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> items;
    ImageLoader imageLoader = MySingleton.getInstance().getImageLoader();

    public UserAdapter(Activity activity,List<User> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView= inflater.inflate(R.layout.custom_layout,null);
        }

            TextView nume = (TextView) convertView.findViewById(R.id.dr_nume);
            TextView id = (TextView) convertView.findViewById(R.id.dr_id);
            User i = items.get(position);
            id.setText(String.valueOf(i.getUserID()));
            nume.setText(i.getNumeDoctor());

        return convertView;
    }
}
