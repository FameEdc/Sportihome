package fr.wildcodeschool.apprenti.sportihome.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.Activities.SpotActivity;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotModel;
import fr.wildcodeschool.apprenti.sportihome.R;

public class SpotsAdapter extends ArrayAdapter<SpotModel> {

    private final Activity activity;
    private final ArrayList<SpotModel> mesSpots;

    public SpotsAdapter(Activity activity, ArrayList<SpotModel> mesSpots) {
        super(activity, R.layout.fragment_search,mesSpots);

        this.activity=activity;
        this.mesSpots = mesSpots;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.spots_list_item, null,true);
        final SpotModel monSpot = mesSpots.get(position);

        final Context context = rowView.getContext();

        //CHECK & LOAD DATAS

        //Avatar
        CircleImageView imgAvatar = (CircleImageView) rowView.findViewById(R.id.avatar);
        String user,avatarUrl;
        //on verifie d'abord si c'est un avatar du site
        user = monSpot.getOwner().get_id();
        if (monSpot.getOwner().getAvatar() != null){
            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+monSpot.getOwner().getAvatar();
            avatarUrl = avatarUrl.replace(" ","%20");
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
        }else{
            //sinon c'est que l'avatar viens soit de facebook soit de google

            if (monSpot.getOwner().getGoogle() != null){
                if (monSpot.getOwner().getGoogle().getAvatar() != null){
                    //avatar google
                    avatarUrl = monSpot.getOwner().getGoogle().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }

            if (monSpot.getOwner().getFacebook() != null){
                if (monSpot.getOwner().getFacebook().getAvatar() != null){
                    //avatar facebook
                    avatarUrl = monSpot.getOwner().getFacebook().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }
        }

        //Picture
        if (monSpot.getFirstPicture() != null){
            ImageView imgSpot = (ImageView) rowView.findViewById(R.id.spot_img);
            String picture = monSpot.getFirstPicture();
            String pictureUrl = "https://sportihome.com/uploads/spots/"+monSpot.get_id()+"/thumb/"+picture;
            Picasso.with(context).load(pictureUrl).fit().centerCrop().into(imgSpot);
        }

        //Name
        if (monSpot.getName() != null){
            TextView txtName = (TextView) rowView.findViewById(R.id.name);
            String name = monSpot.getName();
            txtName.setText(name);
        }

        //Hobby
        if (monSpot.getHobby() != null){
            CustomFontTextView txtSport = (CustomFontTextView) rowView.findViewById(R.id.sport);
            String hobby = monSpot.getHobby();
            String stringName = "img_"+hobby;
            stringName = stringName.replace("-","_");
            String strHobby = getStringResourceByName(stringName,context);
            txtSport.setText(strHobby);
        }

        //Rating
        if (monSpot.getRating().getOverallRating() != 0){
            RatingBar ratePlace = (RatingBar) rowView.findViewById(R.id.ratingBar);
            int rating = monSpot.getRating().getOverallRating();
            ratePlace.setRating(rating);
        }

        //Count Rating
        if (monSpot.getRating().getNumberOfRatings() != 0){
            TextView txtRatingCount = (TextView) rowView.findViewById(R.id.rating_count);
            int countRatings = monSpot.getRating().getNumberOfRatings();
            txtRatingCount.setText("("+String.valueOf(countRatings+")"));
        }

        //EVENTS
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ENVOYER ID spot à SpotActivity
                Intent intent = new Intent(activity,SpotActivity.class);
                intent.putExtra("spot_id",monSpot.get_id());
                activity.startActivity(intent);
            }
        });


        return rowView;
    }

    public void addListItemsToAdapter(ArrayList<SpotModel> list){
        mesSpots.addAll(list);
        this.notifyDataSetChanged();
    }

    public int getCount(){
        return mesSpots.size();
    }

    private String getStringResourceByName(String aString,Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

}
