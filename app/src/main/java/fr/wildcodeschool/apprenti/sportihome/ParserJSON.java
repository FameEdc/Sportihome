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
                JSONObject place = new JSONObject(jsonStr);
                PlaceModel maPlace = parserPlace(place);
                return maPlace;

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

    public static SpotModel getSpot(String jsonStr){
        if (jsonStr != null) {
            try {
                JSONObject spot = new JSONObject(jsonStr);
                SpotModel monSpot = parserSpot(spot);
                return monSpot;

            }catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    public static ArrayList<SpotModel> getSpots(String jsonStr){
        if (jsonStr != null) {

            ArrayList<SpotModel> spotsList = new ArrayList<SpotModel>();

            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                // looping through All PLACES OBJ
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject spot = jsonArray.getJSONObject(i);

                    spotsList.add(parserSpot(spot));

                }

                return spotsList;

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

    private static SpotModel parserSpot(JSONObject spot) throws JSONException{
        //PLACE - JSON OBJ : "spot_"
        String spot_id = spot.getString("_id");
        JSONObject spot_owner = spot.getJSONObject("owner");
        String spot_hobby = spot.getString("hobby");
        String spot_name = spot.getString("name");
        double spot_latitude = spot.getInt("latitude");
        double spot_longitude = spot.getInt("longitude");
        String spot_about = spot.getString("about");
        int spot_v = spot.getInt("__v");
        String spot_modification = spot.getString("modification");
        JSONArray spot_comments = spot.getJSONArray("comments");
        JSONObject spot_rating = spot.getJSONObject("rating");
        JSONObject spot_address = spot.getJSONObject("address");
        JSONArray spot_pictures = spot.getJSONArray("pictures");
        String spot_creation = spot.getString("creation");

        //================
        //SPOT OWNER - JSON OBJ : "so_"
        String so_id = spot_owner.getString("_id");
        String so_email = spot_owner.getString("email");
        int so_v = spot_owner.getInt("__v");
        //JSONArray so_comments = place_owner.getJSONArray("comments");
        boolean so_isAdmin = spot_owner.getBoolean("isAdmin");
        //JSONArray so_rating = place_owner.getJSONArray("rating");
        JSONArray so_hobbies = spot_owner.getJSONArray("hobbies");
        String so_engagement = spot_owner.getString("engagement");
        JSONObject so_identity = spot_owner.getJSONObject("identity");
        boolean so_isValidate = spot_owner.getBoolean("isValidate");
        String so_creation = spot_owner.getString("creation");

        //SPOT OWNER COMMENTS - JSON ARRAY : soComments
        //CommentModel[] soComments = new CommentModel[so_comments.length()];//VIDE
                        /* on sait que soComments est vide mais dans le cas contraire utilise ca idem pour les autres.
                        if (so_comments.length() != 0){
                            for (int k=0 ; k < so_comments.length() ; k++){
                                soComments[k] = new CommentModel();
                            }
                        }*/

        //SPOT OWNER RATING - JSON ARRAY : soRatings
        //RateModel[] soRatings = new RateModel[so_rating.length()];//VIDE
                        /*
                        if (so_rating.length() != 0){
                            for (int k=0 ; k < so_rating.length() ; k++){
                                JSONObject rate = so_rating.getJSONObject(kk);

                                //PLACE OWNER RATING JSON OBJ : "sor_"
                                int sor_valueForMoney = rate.getInt("valueForMoney");
                                int sor_location = rate.getInt("location");
                                int sor_cleanness = rate.getInt("cleanness");
                                int sor_overallRating = rate.getInt("overallRating");
                                int sor_numbersOfRatings = rate.getInt("numberOfRatings");

                                soRatings[k] = new RateModel();
                            }
                        }*/

        //SPOT OWNER HOBBIES - JSON ARRAY : soHobbies
        String[] soHobbies = new String[so_hobbies.length()];
        for (int j=0 ; j < so_hobbies.length() ; j++){
            soHobbies[j] = so_hobbies.getString(j);
        }

        //SPOT OWNER IDENTITY - JSON OBJ : "soi_"
        String soi_firstName = so_identity.getString("firstName");
        String soi_lastName = so_identity.getString("lastName");

        IdentityModel mSOIdentity = new IdentityModel(soi_firstName,soi_lastName);

        if (!so_identity.isNull("birthday") && so_identity.has("birthday")){
            String poi_birthday = so_identity.getString("birthday");
            mSOIdentity.setBirthday(poi_birthday);
        }
        if (!so_identity.isNull("mobilePhone") && so_identity.has("mobilePhone")){
            String poi_mobilePhone = so_identity.getString("mobilePhone");
            mSOIdentity.setMobilePhone(poi_mobilePhone);
        }
        if (!so_identity.isNull("phone") && so_identity.has("phone")){
            String poi_phone = so_identity.getString("phone");
            mSOIdentity.setPhone(poi_phone);
        }

        OwnerModel mSOwner = new OwnerModel(so_id,so_email,so_v,so_isAdmin,soHobbies,so_engagement,mSOIdentity,so_isValidate,so_creation);

        //SETS
        if(spot_owner.has("avatar")){
            String po_avatar = spot_owner.getString("avatar");
            mSOwner.setAvatar(po_avatar);
        }
        else{
            mSOwner.setAvatar("");
        }

        //SPOT OWNER FACEBOOK - JSON OBJ : "sof_"
        FacebookModel mSOFacebook;
        if (spot_owner.has("facebook")){
            JSONObject so_facebook = spot_owner.getJSONObject("facebook");

            String sof_id = so_facebook.getString("id");
            String sof_token = so_facebook.getString("token");
            String sof_name = so_facebook.getString("name");
            String sof_email = so_facebook.getString("email");

            //Create object Facebook
            mSOFacebook = new FacebookModel(sof_id,sof_token,sof_name,sof_email);

            if(so_facebook.has("avatar")){
                String pof_avatar = so_facebook.getString("avatar");
                mSOFacebook.setAvatar(pof_avatar);
            }
            else{
                mSOFacebook.setAvatar("");
            }
        }
        else{
            mSOFacebook = new FacebookModel();
        }
        mSOwner.setFacebook(mSOFacebook);

        //SPOT OWNER GOOGLE - JSON OBJ : "sog_"
        if (spot_owner.has("google")){

            JSONObject so_google = spot_owner.getJSONObject("google");

            String sog_id = so_google.getString("id");
            String sog_token = so_google.getString("token");
            String sog_name = so_google.getString("name");
            String sog_email = so_google.getString("email");
            //Create object Google
            GoogleModel mSOGoogle = new GoogleModel(sog_id,sog_token,sog_name,sog_email);
            mSOwner.setGoogle(mSOGoogle);
        }

        //===================
        //SPOT COMMENTS - JSON ARRAY : sComments
        CommentModel[] sComments = new CommentModel[spot_comments.length()];
        if (spot_comments.length() != 0){
            for (int k=0; k < spot_comments.length() ; k++){

                JSONObject comment = spot_comments.getJSONObject(k);

                //SPOT COMMENT - JSON OBJ : "sc_"
                String sc_id = comment.getString("_id");
                JSONObject sc_owner = comment.getJSONObject("owner");
                String sc_date = comment.getString("date");
                String sc_content = comment.getString("comment");
                int sc_cleanness = comment.getInt("cleanness");
                int sc_location = comment.getInt("location");
                int sc_valueForMoney = comment.getInt("valueForMoney");

                //SPOT COMMENT OWNER - JSON OBJ : "sco_"
                String sco_id = sc_owner.getString("_id");
                String sco_email = sc_owner.getString("email");
                int sco_v = sc_owner.getInt("__v");
                //JSONArray sco_comments = sc_owner.getJSONArray("comments");
                boolean sco_isAdmin = sc_owner.getBoolean("isAdmin");
                //JSONArray pco_rating = sc_owner.getJSONArray("rating");
                JSONArray sco_hobbies = sc_owner.getJSONArray("hobbies");
                String sco_engagement = sc_owner.getString("engagement");
                JSONObject sco_identity = sc_owner.getJSONObject("identity");
                boolean sco_isValidate = sc_owner.getBoolean("isValidate");
                String sco_creation = sc_owner.getString("creation");

                //SPOT COMMENT OWNER COMMENTS - JSON ARRAY : scoComments
                //CommentModel[] scoComments = new CommentModel[sc_content.length()];//VIDE
                                /*
                                if (so_comments.length() != 0){
                                    for (int k=0 ; k < so_comments.length() ; k++){
                                        scoComments[k] = new CommentModel();
                                    }
                                }*/

                //PLACE COMMENT OWNER RATING - JSON ARRAY : scoRatings
                //RateModel[] scoRatings = new RateModel[sco_rating.length()];//VIDE
                                /*
                                if (sco_rating.length() != 0){
                                    for (int kk=0 ; kk < sco_rating.length() ; kk++){
                                        // TABLEAU VIDE pas évident...
                                        JSONObject rate = sco_rating.getJSONObject(kk);

                                        //PLACE COMMENT OWNER RATING - JSON OBJ : "pcor_"
                                        int scor_valueForMoney = rate.getInt("valueForMoney");
                                        int scor_location = rate.getInt("location");
                                        int scor_cleanness = rate.getInt("cleanness");
                                        int scor_overallRating = rate.getInt("overallRating");
                                        int scor_numbersOfRatings = rate.getInt("numberOfRatings");

                                        scoRatings[kk] = new RateModel();
                                    }
                                }*/

                //SPOT COMMENT OWNER HOBBIES - JSON ARRAY : scoHobbies
                String[] scoHobbies = new String[so_hobbies.length()];
                for (int l=0 ; l < so_hobbies.length() ; l++){
                    scoHobbies[l] = sco_hobbies.getString(l);
                }

                //SPOT COMMENT OWNER IDENTITY - JSON OBJ : "scoi_"
                String scoi_firstName = sco_identity.getString("firstName");
                String scoi_lastName = sco_identity.getString("lastName");

                IdentityModel mSCOIdentity = new IdentityModel(scoi_firstName, scoi_lastName);

                if (!sco_identity.isNull("birthday") && sco_identity.has("birthday")){
                    String pcoi_birthday = sco_identity.getString("birthday");
                    mSCOIdentity.setBirthday(pcoi_birthday);
                }
                if (!sco_identity.isNull("mobilePhone") && sco_identity.has("mobilePhone")){
                    String pcoi_mobilePhone = sco_identity.getString("mobilePhone");
                    mSCOIdentity.setMobilePhone(pcoi_mobilePhone);
                }
                if (!sco_identity.isNull("phone") && sco_identity.has("phone")){
                    String pcoi_phone = sco_identity.getString("phone");
                    mSCOIdentity.setPhone(pcoi_phone);
                }

                OwnerModel mSCOwner = new OwnerModel(sco_id,sco_email,sco_v,sco_isAdmin,scoHobbies,sco_engagement,mSCOIdentity,sco_isValidate,sco_creation);

                //SETS
                if(sc_owner.has("avatar")){
                    String pco_avatar = sc_owner.getString("avatar");
                    mSCOwner.setAvatar(pco_avatar);
                }
                else{
                    mSCOwner.setAvatar("");
                }

                //SPOT COMMENT OWNER FACEBOOK - JSON OBJ : "scof_"
                FacebookModel mSCOFacebook;
                if (sc_owner.has("facebook")){

                    JSONObject sco_facebook = sc_owner.getJSONObject("facebook");

                    String scof_id = sco_facebook.getString("id");
                    String scof_token = sco_facebook.getString("token");
                    String scof_name = sco_facebook.getString("name");
                    String scof_email = sco_facebook.getString("email");

                    //set Facebook OBJ on Comment OBJ
                    mSCOFacebook = new FacebookModel(scof_id,scof_token,scof_name,scof_email);

                    if(sco_facebook.has("avatar")){
                        String pcof_avatar = sco_facebook.getString("avatar");
                        mSCOFacebook.setAvatar(pcof_avatar);
                    }
                    else{
                        mSCOFacebook.setAvatar("");
                    }
                }
                else{
                    mSCOFacebook = new FacebookModel();
                }
                mSCOwner.setFacebook(mSCOFacebook);

                //SPOT COMMENT OWNER GOOGLE - JSON OBJ : "scog_"
                if (sc_owner.has("google")){

                    JSONObject sco_google = sc_owner.getJSONObject("google");

                    String scog_id = sco_google.getString("id");
                    String scog_token = sco_google.getString("token");
                    String scog_name = sco_google.getString("name");
                    String scog_email = sco_google.getString("email");

                    //set Google OBJ on Comment OBJ
                    GoogleModel mPCOGoogle = new GoogleModel(scog_id,scog_token,scog_name,scog_email);
                    mSCOwner.setGoogle(mPCOGoogle);
                }

                sComments[k] = new CommentModel(sc_id,sc_date,mSCOwner,sc_content,sc_cleanness,sc_location,sc_valueForMoney);

            }
        }

        //SPOT RATING - JSON OBJ : "sr_"
        int sr_difficulty = spot_rating.getInt("difficulty");
        int sr_beauty = spot_rating.getInt("beauty");
        int sr_quality = spot_rating.getInt("quality");
        int sr_overallRating = spot_rating.getInt("overallRating");
        int sr_numbersOfRatings = spot_rating.getInt("numberOfRatings");
        SpotRatingModel mSRatings = new SpotRatingModel(sr_difficulty,sr_beauty,sr_quality,sr_overallRating,sr_numbersOfRatings);

        //SPOT ADDRESS - JSON OBJ : "sa_"
        String sa_postal = spot_address.getString("postal_code");
        String sa_country = spot_address.getString("country");
        String sa_admin_area_lvl_1 = spot_address.getString("administrative_area_level_1");
        String sa_locality = spot_address.getString("locality");
        String sa_route = spot_address.getString("route");
        AddressModel mSAddress = new AddressModel(sa_postal,sa_country,sa_admin_area_lvl_1,sa_locality,sa_route);

        if(!spot_address.isNull("street_number") && spot_address.has("street_number")){
            int pa_street_number = spot_address.getInt("street_number");
            mSAddress.setStreet_number(pa_street_number);
        }

        //SPOT PICTURES - JSON ARRAY : sPictures
        String[] sPictures = new String[spot_pictures.length()];
        for (int m=0 ; m < spot_pictures.length() ; m++){
            sPictures[m] = spot_pictures.getString(m);
        }

        //===================
        //Add Place Object on Collection
        SpotModel mSpot = new SpotModel(spot_id,mSOwner,spot_hobby,spot_name,spot_latitude,spot_longitude,spot_about,spot_v,spot_modification,sComments,mSRatings,mSAddress,sPictures,spot_creation);
        return mSpot;

    }

}
