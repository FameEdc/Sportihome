package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 03/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpotsExpandableListAdapter extends BaseExpandableListAdapter {
    //private final SparseArray<Group> groups;
    private final SparseArray<SpotsGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public SpotsExpandableListAdapter(Activity act, SparseArray<SpotsGroup> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).spot.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SpotModel monSpot = (SpotModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spots_list_item, null);
        }

        final Context context = convertView.getContext();

        ImageView imgSpot = (ImageView) convertView.findViewById(R.id.spot_img);
        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        CustomFontTextView txtSport = (CustomFontTextView) convertView.findViewById(R.id.sport);
        RatingBar ratePlace = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView txtRatingCount = (TextView) convertView.findViewById(R.id.rating_count);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);

        //Load datas
        String picture = monSpot.getFirstPicture();
        String pictureUrl = "https://sportihome.com/uploads/spots/"+monSpot.get_id()+"/thumb/"+picture;
        String name = monSpot.getName();
        String hobby = monSpot.getHobby();
        String stringName = "img_"+hobby;
        stringName = stringName.replace("-","_");
        String strHobby = getStringResourceByName(stringName,context);
        String user,avatarUrl="";
        if(!monSpot.getOwner().getAvatar().isEmpty()){
            user = monSpot.getOwner().get_id();
            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/image.jpeg";
        }
        else{
            if (!monSpot.getOwner().getFacebook().getAvatar().isEmpty()){
                avatarUrl = monSpot.getOwner().getFacebook().getAvatar();
            }
        }

        int rating = monSpot.getRating().getOverallRating();
        int countRatings = monSpot.getRating().getNumberOfRatings();

        Picasso.with(context).load(pictureUrl).fit().centerCrop().into(imgSpot);
        txtName.setText(name);
        txtSport.setText(strHobby);
        ratePlace.setRating(rating);
        txtRatingCount.setText("("+String.valueOf(countRatings+")"));
        if (!avatarUrl.isEmpty()){
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity, monSpot.get_id(), Toast.LENGTH_SHORT).show();
                //Quand je clic sur un hébergement
                //ENVOYER ID hergement à PlaceActivity
                Intent intent = new Intent(activity,SpotActivity.class);
                intent.putExtra("spot_id",monSpot.get_id());
                activity.startActivity(intent);
                //activity.finish();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).spot.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spots_list_group, null);
        }

        SpotsGroup group = (SpotsGroup) getGroup(groupPosition);

        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }
}
