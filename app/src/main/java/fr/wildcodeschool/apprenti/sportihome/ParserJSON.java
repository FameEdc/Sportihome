package fr.wildcodeschool.apprenti.sportihome;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by chantome on 30/11/2016.
 */

public class ParserJSON extends AppCompatActivity{

    private final static String TAG = ParserJSON.class.getSimpleName();

    public static PlaceModel getPlace(String jsonStr){
        if (jsonStr != null) {
            try {

                JSONObject place = new JSONObject();
                return parserPlace(place);

            }catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    public static ArrayList<PlaceModel> getPlaces(String jsonStr){
        if (jsonStr != null) {

            ArrayList<PlaceModel> placesList = new ArrayList<PlaceModel>();

            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                // looping through All PLACES OBJ
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject place = jsonArray.getJSONObject(i);

                    placesList.add(parserPlace(place));

                }

                return placesList;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    private static PlaceModel parserPlace(JSONObject place) throws JSONException{
            //PLACE - JSON OBJ : "place_"
            String place_id = place.getString("_id");
            boolean place_isActive = place.getBoolean("isActive");
            JSONObject place_owner = place.getJSONObject("owner");
            double place_latitude = place.getInt("latitude");
            double place_longitude = place.getInt("longitude");
            String place_name = place.getString("name");
            String place_about = place.getString("about");
            int place_v = place.getInt("__v");
            String place_modification = place.getString("modification");
            JSONArray place_comments = place.getJSONArray("comments");
            JSONObject place_rating = place.getJSONObject("rating");
            JSONObject place_home = place.getJSONObject("home");
            JSONObject place_address = place.getJSONObject("address");
            JSONArray place_pictures = place.getJSONArray("pictures");
            String place_creation = place.getString("creation");
            JSONArray place_hobbies = place.getJSONArray("hobbies");
            boolean place_private = place.getBoolean("private");
            boolean place_finished = place.getBoolean("finished");
            int place_step = place.getInt("step");

            //================
            //PLACE OWNER - JSON OBJ : "po_"
            String po_id = place_owner.getString("_id");
            String po_email = place_owner.getString("email");
            int po_v = place_owner.getInt("__v");
            //JSONArray po_comments = place_owner.getJSONArray("comments");
            boolean po_isAdmin = place_owner.getBoolean("isAdmin");
            //JSONArray po_rating = place_owner.getJSONArray("rating");
            JSONArray po_hobbies = place_owner.getJSONArray("hobbies");
            String po_engagement = place_owner.getString("engagement");
            JSONObject po_identity = place_owner.getJSONObject("identity");
            boolean po_isValidate = place_owner.getBoolean("isValidate");
            String po_creation = place_owner.getString("creation");

            //PLACE OWNER COMMENTS - JSON ARRAY : poComments
            //CommentModel[] poComments = new CommentModel[po_comments.length()];//VIDE
                        /* on sait que poComments est vide mais dans le cas contraire utilise ca idem pour les autres.
                        if (po_comments.length() != 0){
                            for (int k=0 ; k < po_comments.length() ; k++){
                                poComments[k] = new CommentModel();
                            }
                        }*/

            //PLACE OWNER RATING - JSON ARRAY : poRatings
            //RateModel[] poRatings = new RateModel[po_rating.length()];//VIDE
                        /*
                        if (po_rating.length() != 0){
                            for (int k=0 ; k < po_rating.length() ; k++){
                                JSONObject rate = po_rating.getJSONObject(kk);

                                //PLACE OWNER RATING JSON OBJ : "por_"
                                int por_valueForMoney = rate.getInt("valueForMoney");
                                int por_location = rate.getInt("location");
                                int por_cleanness = rate.getInt("cleanness");
                                int por_overallRating = rate.getInt("overallRating");
                                int por_numbersOfRatings = rate.getInt("numberOfRatings");

                                poRatings[k] = new RateModel();
                            }
                        }*/

            //PLACE OWNER HOBBIES - JSON ARRAY : poHobbies
            String[] poHobbies = new String[po_hobbies.length()];
            for (int j=0 ; j < po_hobbies.length() ; j++){
                poHobbies[j] = po_hobbies.getString(j);
            }

            //PLACE OWNER IDENTITY - JSON OBJ : "poi_"
            String poi_firstName = po_identity.getString("firstName");
            String poi_lastName = po_identity.getString("lastName");

            IdentityModel mPOIdentity = new IdentityModel(poi_firstName,poi_lastName);

            if (!po_identity.isNull("birthday") && po_identity.has("birthday")){
                String poi_birthday = po_identity.getString("birthday");
                mPOIdentity.setBirthday(poi_birthday);
            }
            if (!po_identity.isNull("mobilePhone") && po_identity.has("mobilePhone")){
                String poi_mobilePhone = po_identity.getString("mobilePhone");
                mPOIdentity.setMobilePhone(poi_mobilePhone);
            }
            if (!po_identity.isNull("phone") && po_identity.has("phone")){
                String poi_phone = po_identity.getString("phone");
                mPOIdentity.setPhone(poi_phone);
            }

            OwnerModel mPOwner = new OwnerModel(po_id,po_email,po_v,po_isAdmin,poHobbies,po_engagement,mPOIdentity,po_isValidate,po_creation);

            //SETS
            if(place_owner.has("avatar")){
                String po_avatar = place_owner.getString("avatar");
                mPOwner.setAvatar(po_avatar);
            }
            else{
                mPOwner.setAvatar("");
            }

            //PLACE OWNER FACEBOOK - JSON OBJ : "pof_"
            FacebookModel mPCFacebook;
            if (place_owner.has("facebook")){
                JSONObject po_facebook = place_owner.getJSONObject("facebook");

                String pof_id = po_facebook.getString("id");
                String pof_token = po_facebook.getString("token");
                String pof_name = po_facebook.getString("name");
                String pof_email = po_facebook.getString("email");

                //Create object Facebook
                mPCFacebook = new FacebookModel(pof_id,pof_token,pof_name,pof_email);

                if(po_facebook.has("avatar")){
                    String pof_avatar = po_facebook.getString("avatar");
                    mPCFacebook.setAvatar(pof_avatar);
                }
                else{
                    mPCFacebook.setAvatar("");
                }
            }
            else{
                mPCFacebook = new FacebookModel();
            }
            mPOwner.setFacebook(mPCFacebook);

            //PLACE OWNER GOOGLE - JSON OBJ : "pog_"
            if (place_owner.has("google")){

                JSONObject po_google = place_owner.getJSONObject("google");

                String pog_id = po_google.getString("id");
                String pog_token = po_google.getString("token");
                String pog_name = po_google.getString("name");
                String pog_email = po_google.getString("email");
                //Create object Google
                GoogleModel mPCGoogle = new GoogleModel(pog_id,pog_token,pog_name,pog_email);
                mPOwner.setGoogle(mPCGoogle);
            }

            //===================
            //PLACE COMMENTS - JSON ARRAY : pComments
            CommentModel[] pComments = new CommentModel[place_comments.length()];
            if (place_comments.length() != 0){
                for (int k=0; k < place_comments.length() ; k++){

                    JSONObject comment = place_comments.getJSONObject(k);

                    //PLACE COMMENT - JSON OBJ : "pc_"
                    String pc_id = comment.getString("_id");
                    JSONObject pc_owner = comment.getJSONObject("owner");
                    String pc_date = comment.getString("date");
                    String pc_content = comment.getString("comment");
                    int pc_cleanness = comment.getInt("cleanness");
                    int pc_location = comment.getInt("location");
                    int pc_valueForMoney = comment.getInt("valueForMoney");

                    //PLACE COMMENT OWNER - JSON OBJ : "pco_"
                    String pco_id = pc_owner.getString("_id");
                    String pco_email = pc_owner.getString("email");
                    int pco_v = pc_owner.getInt("__v");
                    //JSONArray pco_comments = pc_owner.getJSONArray("comments");
                    boolean pco_isAdmin = pc_owner.getBoolean("isAdmin");
                    //JSONArray pco_rating = pc_owner.getJSONArray("rating");
                    JSONArray pco_hobbies = pc_owner.getJSONArray("hobbies");
                    String pco_engagement = pc_owner.getString("engagement");
                    JSONObject pco_identity = pc_owner.getJSONObject("identity");
                    boolean pco_isValidate = pc_owner.getBoolean("isValidate");
                    String pco_creation = pc_owner.getString("creation");

                    //PLACE COMMENT OWNER COMMENTS - JSON ARRAY : pcoComments
                    //CommentModel[] pcoComments = new CommentModel[pc_content.length()];//VIDE
                                /*
                                if (po_comments.length() != 0){
                                    for (int k=0 ; k < po_comments.length() ; k++){
                                        pcoComments[k] = new CommentModel();
                                    }
                                }*/

                    //PLACE COMMENT OWNER RATING - JSON ARRAY : pcoRatings
                    //RateModel[] pcoRatings = new RateModel[pco_rating.length()];//VIDE
                                /*
                                if (pco_rating.length() != 0){
                                    for (int kk=0 ; kk < pco_rating.length() ; kk++){
                                        // TABLEAU VIDE pas évident...
                                        JSONObject rate = pco_rating.getJSONObject(kk);

                                        //PLACE COMMENT OWNER RATING - JSON OBJ : "pcor_"
                                        int pcor_valueForMoney = rate.getInt("valueForMoney");
                                        int pcor_location = rate.getInt("location");
                                        int pcor_cleanness = rate.getInt("cleanness");
                                        int pcor_overallRating = rate.getInt("overallRating");
                                        int pcor_numbersOfRatings = rate.getInt("numberOfRatings");

                                        pcoRatings[kk] = new RateModel();
                                    }
                                }*/

                    //PLACE COMMENT OWNER HOBBIES - JSON ARRAY : pcoHobbies
                    String[] pcoHobbies = new String[po_hobbies.length()];
                    for (int l=0 ; l < pco_hobbies.length() ; l++){
                        pcoHobbies[l] = pco_hobbies.getString(l);
                    }

                    //PLACE COMMENT OWNER IDENTITY - JSON OBJ : "pcoi_"
                    String pcoi_firstName = pco_identity.getString("firstName");
                    String pcoi_lastName = pco_identity.getString("lastName");

                    IdentityModel mPCOIdentity = new IdentityModel(pcoi_firstName, pcoi_lastName);

                    if (!pco_identity.isNull("birthday") && pco_identity.has("birthday")){
                        String pcoi_birthday = pco_identity.getString("birthday");
                        mPCOIdentity.setBirthday(pcoi_birthday);
                    }
                    if (!pco_identity.isNull("mobilePhone") && pco_identity.has("mobilePhone")){
                        String pcoi_mobilePhone = pco_identity.getString("mobilePhone");
                        mPCOIdentity.setMobilePhone(pcoi_mobilePhone);
                    }
                    if (!pco_identity.isNull("phone") && pco_identity.has("phone")){
                        String pcoi_phone = pco_identity.getString("phone");
                        mPCOIdentity.setPhone(pcoi_phone);
                    }

                    OwnerModel mPCOwner = new OwnerModel(pco_id,pco_email,pco_v,pco_isAdmin,pcoHobbies,pco_engagement,mPCOIdentity,pco_isValidate,pco_creation);

                    //SETS
                    if(pc_owner.has("avatar")){
                        String pco_avatar = pc_owner.getString("avatar");
                        mPCOwner.setAvatar(pco_avatar);
                    }
                    else{
                        mPCOwner.setAvatar("");
                    }

                    //PLACE COMMENT OWNER FACEBOOK - JSON OBJ : "pcof_"
                    FacebookModel mPCOFacebook;
                    if (pc_owner.has("facebook")){

                        JSONObject pco_facebook = pc_owner.getJSONObject("facebook");

                        String pcof_id = pco_facebook.getString("id");
                        String pcof_token = pco_facebook.getString("token");
                        String pcof_name = pco_facebook.getString("name");
                        String pcof_email = pco_facebook.getString("email");

                        //set Facebook OBJ on Comment OBJ
                        mPCOFacebook = new FacebookModel(pcof_id,pcof_token,pcof_name,pcof_email);

                        if(pco_facebook.has("avatar")){
                            String pcof_avatar = pco_facebook.getString("avatar");
                            mPCOFacebook.setAvatar(pcof_avatar);
                        }
                        else{
                            mPCOFacebook.setAvatar("");
                        }
                    }
                    else{
                        mPCOFacebook = new FacebookModel();
                    }
                    mPCOwner.setFacebook(mPCOFacebook);

                    //PLACE COMMENT OWNER GOOGLE - JSON OBJ : "pcog_"
                    if (pc_owner.has("google")){

                        JSONObject pco_google = pc_owner.getJSONObject("google");

                        String pcog_id = pco_google.getString("id");
                        String pcog_token = pco_google.getString("token");
                        String pcog_name = pco_google.getString("name");
                        String pcog_email = pco_google.getString("email");

                        //set Google OBJ on Comment OBJ
                        GoogleModel mPCOGoogle = new GoogleModel(pcog_id,pcog_token,pcog_name,pcog_email);
                        mPCOwner.setGoogle(mPCOGoogle);
                    }

                    pComments[k] = new CommentModel(pc_id,pc_date,mPCOwner,pc_content,pc_cleanness,pc_location,pc_valueForMoney);

                }
            }

            //PLACE RATING - JSON OBJ : "pr_"
            int pr_valueForMoney = place_rating.getInt("valueForMoney");
            int pr_location = place_rating.getInt("location");
            int pr_cleanness = place_rating.getInt("cleanness");
            int pr_overallRating = place_rating.getInt("overallRating");
            int pr_numbersOfRatings = place_rating.getInt("numberOfRatings");
            RateModel mPRatings = new RateModel(pr_valueForMoney,pr_location,pr_cleanness,pr_overallRating,pr_numbersOfRatings);

            //PLACE HOME - JSON OBJ : "ph_"
            int ph_travellers = place_home.getInt("travellers");
            String ph_property = place_home.getString("propertyType");
            JSONObject ph_price = place_home.getJSONObject("price");

            //PLACE HOME PRICE - JSON OBJ : "php_"
            int php_highSeason = ph_price.getInt("highSeason");
            int php_lowSeason = ph_price.getInt("lowSeason");
            PriceModel mPHPrice = new PriceModel(php_highSeason,php_lowSeason);
            HomeModel mPHome = new HomeModel(ph_travellers,ph_property,mPHPrice);

            //PLACE ADDRESS - JSON OBJ : "pa_"
            String pa_postal = place_address.getString("postal_code");
            String pa_country = place_address.getString("country");
            String pa_admin_area_lvl_1 = place_address.getString("administrative_area_level_1");
            String pa_locality = place_address.getString("locality");
            String pa_route = place_address.getString("route");
            AddressModel mPAddress = new AddressModel(pa_postal,pa_country,pa_admin_area_lvl_1,pa_locality,pa_route);

            if(!place_address.isNull("street_number") && place_address.has("street_number")){
                int pa_street_number = place_address.getInt("street_number");
                mPAddress.setStreet_number(pa_street_number);
            }

            //PLACE PICTURES - JSON ARRAY : pPictures
            String[] pPictures = new String[place_pictures.length()];
            for (int m=0 ; m < place_pictures.length() ; m++){
                pPictures[m] = place_pictures.getString(m);
            }

            //PLACE HOBBIES - JSON ARRAY : pHobbies VIDE
            String[] pHobbies = new String[place_hobbies.length()];
            if (place_hobbies.length() != 0){
                for (int n=0 ; n < place_hobbies.length() ; n++){
                    pHobbies[n] = place_hobbies.getString(n);
                }
            }

            //===================
            //Add Place Object on Collection
            PlaceModel mPlace = new PlaceModel(place_id,place_isActive,mPOwner,place_latitude,place_longitude,place_name,place_about,place_v,place_modification,pComments,mPRatings,mPHome,mPAddress,pPictures,place_creation,pHobbies,place_private,place_finished,place_step);
            return mPlace;

    }


    /**
     * Start Parser for SpotsModel
     */
}
