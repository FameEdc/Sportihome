package fr.wildcodeschool.apprenti.sportihome.Adapters;

/**
 * Created by chantome on 24/11/2016.
 */
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
import fr.wildcodeschool.apprenti.sportihome.Activities.PlaceActivity;
import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.Model.PlaceModel;
import fr.wildcodeschool.apprenti.sportihome.R;

public class PlacesAdapter extends ArrayAdapter<PlaceModel> {

    private final Activity activity;
    private final ArrayList<PlaceModel> mesPlaces;

    public PlacesAdapter(Activity activity, ArrayList<PlaceModel> mesPlaces) {
        super(activity, R.layout.fragment_search, mesPlaces);

        this.activity=activity;
        this.mesPlaces = mesPlaces;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.places_list_item, null,true);
        final PlaceModel maPlace = mesPlaces.get(position);
        final Context context = rowView.getContext();

        //CHECK & LOAD DATAS

        //Avatar
        CircleImageView imgAvatar = (CircleImageView) rowView.findViewById(R.id.avatar);
        String user,avatarUrl;
        //on verifie d'abord si c'est un avatar du site
        user = maPlace.getOwner().get_id();

        if (maPlace.getOwner().getAvatar() != null){

            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+maPlace.getOwner().getAvatar();
            avatarUrl = avatarUrl.replace(" ","%20");
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);

        }else{
            //sinon c'est que l'avatar viens soit de facebook soit de google

            if (maPlace.getOwner().getGoogle() != null){
                if (maPlace.getOwner().getGoogle().getAvatar() != null){
                    //avatar google
                    avatarUrl = maPlace.getOwner().getGoogle().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }

            if (maPlace.getOwner().getFacebook() != null){
                if (maPlace.getOwner().getFacebook().getAvatar() != null){
                    //avatar facebook
                    avatarUrl = maPlace.getOwner().getFacebook().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }
        }

        //Price
        if (maPlace.getHome().getPrice().getLowSeason() != 0){
            TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
            int price = maPlace.getHome().getPrice().getLowSeason();
            txtPrice.setText(price+" €");
        }

        //Picture
        if (maPlace.getFirstPicture() != null){
            ImageView imgPlace = (ImageView) rowView.findViewById(R.id.place_img);
            String picture = maPlace.getFirstPicture();
            String pictureUrl = "https://sportihome.com/uploads/places/"+maPlace.get_id()+"/thumb/"+picture;
            Picasso.with(context).load(pictureUrl).fit().centerCrop().into(imgPlace);
        }

        //Engagement
        if (maPlace.getOwner().getEngagement() != null){
            ImageView imgSas = (ImageView) rowView.findViewById(R.id.sas);
            String engagement = maPlace.getOwner().getEngagement();
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
        }

        //Name
        if (mesPlaces.get(position).getName() != null){
            TextView txtName = (TextView) rowView.findViewById(R.id.name);
            String name = mesPlaces.get(position).getName();
            txtName.setText(name);
        }

        //Hobbies
        if (mesPlaces.get(position).getHobbies() != null){
            CustomFontTextView txtSports = (CustomFontTextView) rowView.findViewById(R.id.sports);
            String[] hobbies = mesPlaces.get(position).getHobbies();
            String strHobbies="";

            for (int i=0 ; i < hobbies.length ; i++){
                String stringName = "img_"+hobbies[i];
                stringName = stringName.replace("-","_");
                strHobbies += getStringResourceByName(stringName, context);
            }
            txtSports.setText(strHobbies);
        }

        //Overall Rating
        if (mesPlaces.get(position).getRating().getOverallRating() != 0){
            RatingBar ratePlace = (RatingBar) rowView.findViewById(R.id.ratingBar);
            int rating = mesPlaces.get(position).getRating().getOverallRating();
            ratePlace.setRating(rating);
        }

        //Count Rating
        if(mesPlaces.get(position).getRating().getNumberOfRatings() != 0){
            TextView txtRatingCount = (TextView) rowView.findViewById(R.id.rating_count);
            int countRatings = mesPlaces.get(position).getRating().getNumberOfRatings();
            txtRatingCount.setText("("+String.valueOf(countRatings+")"));
        }

        //EVENTS
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ENVOYER ID hergement à PlaceActivity
                Intent intent = new Intent(activity,PlaceActivity.class);
                intent.putExtra("place_id",maPlace.get_id());
                activity.startActivity(intent);
            }
        });

        return rowView;
    }

    public void addListItemsToAdapter(ArrayList<PlaceModel> list){
        mesPlaces.addAll(list);
        this.notifyDataSetChanged();
    }

    public int getCount(){
        return mesPlaces.size();
    }

    private String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }
}
