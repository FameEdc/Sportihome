package fr.wildcodeschool.apprenti.sportihome;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chantome on 04/01/2017.
 */

public class SpotCommentsAdapter extends ArrayAdapter<SpotCommentModel> {
    private final Activity activity;
    private final ArrayList<SpotCommentModel> mesComments;

    public SpotCommentsAdapter(Activity activity, ArrayList<SpotCommentModel> mesComments) {
        super(activity, R.layout.activity_spot, mesComments);

        this.activity=activity;
        this.mesComments = mesComments;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.spot_comment_item, null,true);
        final SpotCommentModel monComment = mesComments.get(position);
        final Context context = rowView.getContext();

        //CHECK & LOAD DATAS

        //Avatar
        CircleImageView imgAvatar = (CircleImageView) rowView.findViewById(R.id.img_avatar);
        String user,avatarUrl;
        //on verifie d'abord si c'est un avatar du site
        user = monComment.getOwner().get_id();

        if (monComment.getOwner().getAvatar() != null){

            avatarUrl = "https://sportihome.com/uploads/users/"+user+"/thumb/"+monComment.getOwner().getAvatar();
            avatarUrl = avatarUrl.replace(" ","%20");
            Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);

        }else{
            //sinon c'est que l'avatar viens soit de facebook soit de google

            if (monComment.getOwner().getGoogle() != null){
                if (monComment.getOwner().getGoogle().getAvatar() != null){
                    //avatar google
                    avatarUrl = monComment.getOwner().getGoogle().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }

            if (monComment.getOwner().getFacebook() != null){
                if (monComment.getOwner().getFacebook().getAvatar() != null){
                    //avatar facebook
                    avatarUrl = monComment.getOwner().getFacebook().getAvatar();
                    Picasso.with(context).load(avatarUrl).fit().centerCrop().into(imgAvatar);
                }
            }
        }

        //User name
        if (monComment.getOwner().getIdentity().getFirstName() != null){
            TextView txtUserName = (TextView) rowView.findViewById(R.id.name_user);
            String name = monComment.getOwner().getIdentity().getFirstName();
            txtUserName.setText(name);
        }

        //Comment
        if (monComment.getComment() != null){
            TextView txtComment = (TextView) rowView.findViewById(R.id.comment);
            String comment = monComment.getComment();
            txtComment.setText(comment);
        }

        //Rate Quality
        if (monComment.getQuality() != 0){
            RatingBar rateQuality = (RatingBar) rowView.findViewById(R.id.ratingBar_quality);
            int quality = monComment.getQuality();
            rateQuality.setRating(quality);
        }

        //Rate Beauty
        if (monComment.getBeauty() != 0){
            RatingBar rateBeauty = (RatingBar) rowView.findViewById(R.id.ratingBar_beauty);
            int beauty = monComment.getBeauty();
            rateBeauty.setRating(beauty);
        }

        //Rate Difficulty
        if (monComment.getDifficulty() != 0){
            RatingBar rateDifficulty = (RatingBar) rowView.findViewById(R.id.ratingBar_difficulty);
            int difficulty = monComment.getDifficulty();
            rateDifficulty.setRating(difficulty);
        }

        //Date
        if (monComment.getDate() != null){
            TextView txtDate = (TextView) rowView.findViewById(R.id.date);
            String date = monComment.getDate();
            txtDate.setText(date);
        }

        return rowView;
    }

}