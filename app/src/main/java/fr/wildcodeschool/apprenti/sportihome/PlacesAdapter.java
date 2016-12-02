package fr.wildcodeschool.apprenti.sportihome;

/**
 * Created by chantome on 24/11/2016.
 */
import android.app.Activity;
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

public class PlacesAdapter extends ArrayAdapter<PlaceModel> {

    private final Activity context;
    private final ArrayList<PlaceModel> mesPlaces;

    public PlacesAdapter(Activity context, ArrayList<PlaceModel> mesPlaces) {
        super(context, R.layout.activity_search, mesPlaces);

        this.context=context;
        this.mesPlaces = mesPlaces;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.places_list_item, null,true);
        PlaceModel maPlace = mesPlaces.get(position);

        ImageView imgPlace = (ImageView) rowView.findViewById(R.id.place_img);

        TextView txtPrice = (TextView) rowView.findViewById(R.id.price);
        ImageView imgSas = (ImageView) rowView.findViewById(R.id.sas);
        TextView txtName = (TextView) rowView.findViewById(R.id.name);
        CustomFontTextView txtSports = (CustomFontTextView) rowView.findViewById(R.id.sports);
        CircleImageView imgAvatar = (CircleImageView) rowView.findViewById(R.id.avatar);
        RatingBar ratePlace = (RatingBar) rowView.findViewById(R.id.ratingBar);
        TextView txtRatingCount = (TextView) rowView.findViewById(R.id.rating_count);

        int price = maPlace.getHome().getPrice().getLowSeason();
        String picture = maPlace.getFirstPicture();
        String engagement = maPlace.getOwner().getEngagement();
        String name = mesPlaces.get(position).getName();
        String[] hobbies = mesPlaces.get(position).getHobbies();
        String strHobbies="";
        int rating = mesPlaces.get(position).getRating().getOverallRating();
        int countRatings = mesPlaces.get(position).getRating().getNumberOfRatings();

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
            strHobbies += getStringResourceByName(stringName);
        }
        txtSports.setText(strHobbies);

        String user,avatarUrl="";
        if(!mesPlaces.get(position).getOwner().getAvatar().isEmpty()){
            user = mesPlaces.get(position).getOwner().get_id();
            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/image.jpeg";
        }
        else{
            if (!mesPlaces.get(position).getOwner().getFacebook().getAvatar().isEmpty()){
                avatarUrl = mesPlaces.get(position).getOwner().getFacebook().getAvatar();
            }
        }
        if (!avatarUrl.isEmpty()){
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
        }

        ratePlace.setRating(rating);

        txtRatingCount.setText("("+String.valueOf(countRatings+")"));


        return rowView;
    }

    private String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }
}
