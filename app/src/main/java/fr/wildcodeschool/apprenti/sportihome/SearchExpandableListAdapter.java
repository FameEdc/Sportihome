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

public class SearchExpandableListAdapter extends BaseExpandableListAdapter {
    //private final SparseArray<Group> groups;
    private final SparseArray<PlacesGroup> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public SearchExpandableListAdapter(Activity act, SparseArray<PlacesGroup> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).place.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final PlaceModel maPlace = (PlaceModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.places_list_item, null);
        }

        final Context context = convertView.getContext();

        ImageView imgPlace = (ImageView) convertView.findViewById(R.id.place_img);

        TextView txtPrice = (TextView) convertView.findViewById(R.id.price);
        ImageView imgSas = (ImageView) convertView.findViewById(R.id.sas);
        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        CustomFontTextView txtSports = (CustomFontTextView) convertView.findViewById(R.id.sports);
        CircleImageView imgAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
        RatingBar ratePlace = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView txtRatingCount = (TextView) convertView.findViewById(R.id.rating_count);

        int price = maPlace.getHome().getPrice().getLowSeason();
        String picture = maPlace.getFirstPicture();
        String engagement = maPlace.getOwner().getEngagement();
        String name = maPlace.getName();
        String[] hobbies = maPlace.getHobbies();
        String strHobbies="";
        int rating = maPlace.getRating().getOverallRating();
        int countRatings = maPlace.getRating().getNumberOfRatings();

        //Load datas
        String pictureUrl = "https://sportihome.com/uploads/places/"+maPlace.get_id()+"/thumb/"+picture;
        Picasso.with(context).load(pictureUrl).fit().centerCrop().into(imgPlace);

        txtPrice.setText(price+" €");

        switch (engagement){
            case "stay":
                imgSas.setImageResource(R.drawable.stay);
                break;
            case "stayshare":
                imgSas.setImageResource(R.drawable.stayshare);
                break;
            case "stayshareplay":
                imgSas.setImageResource(R.drawable.stayshareplay);
                break;
        }

        txtName.setText(name);

        for (int i=0 ; i < hobbies.length ; i++){
            String stringName = "img_"+hobbies[i];
            stringName = stringName.replace("-","_");
            strHobbies += getStringResourceByName(stringName,context);
        }
        txtSports.setText(strHobbies);

        String user,avatarUrl="";
        if(!maPlace.getOwner().getAvatar().isEmpty()){
            user = maPlace.getOwner().get_id();
            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/image.jpeg";
        }
        else{
            if (!maPlace.getOwner().getFacebook().getAvatar().isEmpty()){
                avatarUrl = maPlace.getOwner().getFacebook().getAvatar();
            }
        }
        if (!avatarUrl.isEmpty()){
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
        }

        ratePlace.setRating(rating);

        txtRatingCount.setText("("+String.valueOf(countRatings+")"));

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, maPlace.get_id(),
                        Toast.LENGTH_SHORT).show();
                //Quand je clic sur un hébergement
                //ENVOYER ID hergement à PlaceActivity
                Intent intent = new Intent(activity,PlaceActivity.class);
                intent.putExtra("place_id",maPlace.get_id());
                activity.startActivity(intent);
                //activity.finish();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).place.size();
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
            convertView = inflater.inflate(R.layout.places_list_group, null);
        }

        PlacesGroup group = (PlacesGroup) getGroup(groupPosition);

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
