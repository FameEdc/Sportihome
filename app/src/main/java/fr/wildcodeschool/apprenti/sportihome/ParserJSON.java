package fr.wildcodeschool.apprenti.sportihome;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import fr.wildcodeschool.apprenti.sportihome.Model.AddressModel;
import fr.wildcodeschool.apprenti.sportihome.Model.CommentModel;
import fr.wildcodeschool.apprenti.sportihome.Model.FacebookModel;
import fr.wildcodeschool.apprenti.sportihome.Model.GoogleModel;
import fr.wildcodeschool.apprenti.sportihome.Model.HomeModel;
import fr.wildcodeschool.apprenti.sportihome.Model.IdentityModel;
import fr.wildcodeschool.apprenti.sportihome.Model.LocModel;
import fr.wildcodeschool.apprenti.sportihome.Model.LogInModel;
import fr.wildcodeschool.apprenti.sportihome.Model.OwnerModel;
import fr.wildcodeschool.apprenti.sportihome.Model.PlaceModel;
import fr.wildcodeschool.apprenti.sportihome.Model.PriceModel;
import fr.wildcodeschool.apprenti.sportihome.Model.RateModel;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotCommentModel;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotModel;
import fr.wildcodeschool.apprenti.sportihome.Model.SpotRatingModel;

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

    public static OwnerModel getOwner(String jsonStr){

        if (jsonStr != null) {
            try {
                JSONObject owner = new JSONObject(jsonStr);
                OwnerModel mOwner = parserOwner(owner);
                return mOwner;

            }catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;


    }

    public static LogInModel getLogIn(String jsonStr){
        if (jsonStr != null) {
            try {
                JSONObject login = new JSONObject(jsonStr);
                LogInModel mLogIn = parserLogIn(login);
                return mLogIn;

            }catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    private static PlaceModel parserPlace(JSONObject place) throws JSONException{
        if (place != null) {

            try {

                PlaceModel mPlace = new PlaceModel();

                if (!place.isNull("_id") && place.has("_id")){
                    String place_id = place.getString("_id");
                    mPlace.set_id(place_id);
                }

                if (!place.isNull("isActive") && place.has("isActive")){
                    boolean place_isActive = place.getBoolean("isActive");
                    mPlace.setActive(place_isActive);
                }

                if (!place.isNull("owner") && place.has("owner")){
                    JSONObject place_owner = place.getJSONObject("owner");
                    OwnerModel mPOwner = parserOwner(place_owner);
                    mPlace.setOwner(mPOwner);
                }
/*
                if (!place.isNull("latitude") && place.has("latitude")){
                    double place_latitude = place.getInt("latitude");
                    mPlace.setLatitude(place_latitude);
                }

                if (!place.isNull("longitude") && place.has("longitude")){
                    double place_longitude = place.getInt("longitude");
                    mPlace.setLongitude(place_longitude);
                }*/

                if (!place.isNull("name") && place.has("name")){
                    String place_name = place.getString("name");
                    mPlace.setName(place_name);
                }

                if (!place.isNull("about") && place.has("about")){
                    String place_about = place.getString("about");
                    mPlace.setAbout(place_about);
                }

                if (!place.isNull("__v") && place.has("__v")){
                    int place_v = place.getInt("__v");
                    mPlace.set__v(place_v);
                }

                if (!place.isNull("comments") && place.has("comments")){
                    JSONArray place_comments = place.getJSONArray("comments");
                    ArrayList<CommentModel> pComments = new ArrayList<CommentModel>(place_comments.length());
                    if (place_comments.length() != 0){
                        for (int k=0; k < place_comments.length() ; k++){
                            JSONObject comment = place_comments.getJSONObject(k);
                            pComments.add(parserPlaceComment(comment));
                            //pComments[k] = parserPlaceComment(comment);
                        }
                        mPlace.setComments(pComments);
                    }
                }

                if (!place.isNull("rating") && place.has("rating")){
                    JSONObject place_rating = place.getJSONObject("rating");
                    RateModel mPRatings = parserPlaceRating(place_rating);
                    mPlace.setRating(mPRatings);
                }

                if (!place.isNull("home") && place.has("home")){
                    JSONObject place_home = place.getJSONObject("home");
                    HomeModel mPHome = parserHome(place_home);
                    mPlace.setHome(mPHome);
                }

                if (!place.isNull("address") && place.has("address")){
                    JSONObject place_address = place.getJSONObject("address");
                    AddressModel mPAddress = parserAddress(place_address);
                    mPlace.setAddress(mPAddress);
                }

                if (!place.isNull("loc") && place.has("loc")){
                    JSONObject place_loc = place.getJSONObject("loc");
                    LocModel mPLoc = parserLoc(place_loc);
                    mPlace.setLoc(mPLoc);
                }

                if (!place.isNull("pictures") && place.has("pictures")){
                    JSONArray place_pictures = place.getJSONArray("pictures");
                    String[] pPictures = new String[place_pictures.length()];
                    for (int m=0 ; m < place_pictures.length() ; m++){
                        pPictures[m] = place_pictures.getString(m);
                    }
                    mPlace.setPictures(pPictures);
                }

                if (!place.isNull("creation") && place.has("creation")){
                    String place_creation = place.getString("creation");
                    mPlace.setCreation(place_creation);
                }

                if (!place.isNull("hobbies") && place.has("hobbies")){
                    JSONArray place_hobbies = place.getJSONArray("hobbies");
                    String[] pHobbies = new String[place_hobbies.length()];
                    if (place_hobbies.length() != 0){
                        for (int n=0 ; n < place_hobbies.length() ; n++){
                            pHobbies[n] = place_hobbies.getString(n);
                        }
                    }
                    mPlace.setHobbies(pHobbies);
                }

                if (!place.isNull("private") && place.has("private")){
                    boolean place_private = place.getBoolean("private");
                    mPlace.setmPrivate(place_private);
                }

                if (!place.isNull("finished") && place.has("finished")){
                    boolean place_finished = place.getBoolean("finished");
                    mPlace.setFinished(place_finished);
                }

                if (!place.isNull("step") && place.has("step")){
                    int place_step = place.getInt("step");
                    mPlace.setStep(place_step);
                }

                //return Place Object on Collection
                return mPlace;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Place Object is Null.");
        }

        return null;
    }

    private static SpotModel parserSpot(JSONObject spot) throws JSONException{
        if (spot != null) {

            try {

                SpotModel mSpot = new SpotModel();

                if (!spot.isNull("_id") && spot.has("_id")){
                    String place_step = spot.getString("_id");
                    mSpot.set_id(place_step);
                }

                if (!spot.isNull("owner") && spot.has("owner")){
                    JSONObject spot_owner = spot.getJSONObject("owner");
                    OwnerModel mSOwner = parserOwner(spot_owner);
                    mSpot.setOwner(mSOwner);
                }

                if (!spot.isNull("hobby") && spot.has("hobby")){
                    String spot_hobby = spot.getString("hobby");
                    mSpot.setHobby(spot_hobby);
                }

                if (!spot.isNull("name") && spot.has("name")){
                    String spot_name = spot.getString("name");
                    mSpot.setName(spot_name);
                }
/*
                if (!spot.isNull("latitude") && spot.has("latitude")){
                    double spot_latitude = spot.getInt("latitude");
                    mSpot.setLatitude(spot_latitude);
                }

                if (!spot.isNull("longitude") && spot.has("longitude")){
                    double spot_longitude = spot.getInt("longitude");
                    mSpot.setLongitude(spot_longitude);
                }*/

                if (!spot.isNull("about") && spot.has("about")){
                    String spot_about = spot.getString("about");
                    mSpot.setAbout(spot_about);
                }

                if (!spot.isNull("__v") && spot.has("__v")){
                    int spot_v = spot.getInt("__v");
                    mSpot.set__v(spot_v);
                }

                if(!spot.isNull("modification") && spot.has("modification")){
                    String spot_modification = spot.getString("modification");
                    mSpot.setModification(spot_modification);
                }

                if(!spot.isNull("comments") && spot.has("comments")){
                    JSONArray spot_comments = spot.getJSONArray("comments");
                    ArrayList<SpotCommentModel> sComments = new ArrayList<SpotCommentModel>(spot_comments.length());
                    if (spot_comments.length() != 0){
                        for (int k=0; k < spot_comments.length() ; k++){
                            JSONObject comment = spot_comments.getJSONObject(k);
                            sComments.add(parserSpotComment(comment));
                            //sComments[k] = parserSpotComment(comment);
                        }
                        mSpot.setComments(sComments);
                    }
                }

                if(!spot.isNull("rating") && spot.has("rating")){
                    JSONObject spot_rating = spot.getJSONObject("rating");
                    SpotRatingModel mSRatings = parserSpotRating(spot_rating);
                    mSpot.setRating(mSRatings);
                }

                if(!spot.isNull("address") && spot.has("address")){
                    JSONObject spot_address = spot.getJSONObject("address");
                    AddressModel mSAddress = parserAddress(spot_address);
                    mSpot.setAddress(mSAddress);
                }

                if (!spot.isNull("loc") && spot.has("loc")){
                    JSONObject spot_loc = spot.getJSONObject("loc");
                    LocModel mSLoc = parserLoc(spot_loc);
                    mSpot.setLoc(mSLoc);
                }

                if(!spot.isNull("pictures") && spot.has("pictures")){
                    JSONArray spot_pictures = spot.getJSONArray("pictures");
                    String[] sPictures = new String[spot_pictures.length()];
                    for (int m=0 ; m < spot_pictures.length() ; m++){
                        sPictures[m] = spot_pictures.getString(m);
                    }
                    mSpot.setPictures(sPictures);
                }

                if(!spot.isNull("creation") && spot.has("creation")){
                    String spot_creation = spot.getString("creation");
                    mSpot.setCreation(spot_creation);
                }

                return mSpot;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Spot Object is Null.");
        }

        return null;

    }

    private static OwnerModel parserOwner(JSONObject owner) throws JSONException {
        if (owner != null) {

            try {

                OwnerModel mOwner = new OwnerModel();

                if (!owner.isNull("_id") && owner.has("_id")){
                    String id = owner.getString("_id");
                    mOwner.set_id(id);
                }

                if (!owner.isNull("email") && owner.has("email")){
                    String email = owner.getString("email");
                    mOwner.setEmail(email);
                }

                if (!owner.isNull("__v") && owner.has("__v")){
                    int v = owner.getInt("__v");
                    mOwner.set__v(v);
                }

                if (!owner.isNull("avatar") && owner.has("avatar")){
                    String avatar = owner.getString("avatar");
                    mOwner.setAvatar(avatar);
                }

                if (!owner.isNull("isAdmin") && owner.has("isAdmin")){
                    boolean isAdmin = owner.getBoolean("isAdmin");
                    mOwner.setAdmin(isAdmin);
                }

                if (!owner.isNull("isAdmin") && owner.has("isAdmin")){
                    boolean isAdmin = owner.getBoolean("isAdmin");
                    mOwner.setAdmin(isAdmin);
                }

                if (!owner.isNull("hobbies") && owner.has("hobbies")){
                    JSONArray hobbies = owner.getJSONArray("hobbies");
                    String[] oHobbies = new String[hobbies.length()];
                    for (int i=0; i<hobbies.length();i++){
                        oHobbies[i] = hobbies.getString(i);
                    }
                    mOwner.setHobbies(oHobbies);
                }

                if (!owner.isNull("engagement") && owner.has("engagement")){
                    String engagement = owner.getString("engagement");
                    mOwner.setEngagement(engagement);
                }

                if (!owner.isNull("identity") && owner.has("identity")){
                    JSONObject identity = owner.getJSONObject("identity");
                    IdentityModel mOIdentity = parserIdentity(identity);
                    mOwner.setIdentity(mOIdentity);
                }

                if (!owner.isNull("identity") && owner.has("identity")){
                    JSONObject identity = owner.getJSONObject("identity");
                    IdentityModel mOIdentity = parserIdentity(identity);
                    mOwner.setIdentity(mOIdentity);
                }

                if (!owner.isNull("facebook") && owner.has("facebook")){
                    JSONObject facebook = owner.getJSONObject("facebook");
                    FacebookModel mPCFacebook = parserFacebook(facebook);
                    mOwner.setFacebook(mPCFacebook);
                }

                if (!owner.isNull("google") && owner.has("google")){
                    JSONObject google = owner.getJSONObject("google");
                    GoogleModel mPCGoogle = parserGoogle(google);
                    mOwner.setGoogle(mPCGoogle);
                }

                if (!owner.isNull("isValidate") && owner.has("isValidate")){
                    Boolean isValidate = owner.getBoolean("isValidate");
                    mOwner.setValidate(isValidate);
                }

                if (!owner.isNull("creation") && owner.has("creation")){
                    String creation = owner.getString("creation");
                    mOwner.setCreation(creation);
                }

                return mOwner;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Owner Object is Null.");
        }

        return null;

    }

    private static IdentityModel parserIdentity(JSONObject identity) throws JSONException{
        if (identity != null) {

            try {

                IdentityModel mIdentity = new IdentityModel();

                if (!identity.isNull("firstName") && identity.has("firstName")){
                    String firstname = identity.getString("firstName");
                    mIdentity.setFirstName(firstname);
                }

                if (!identity.isNull("lastName") && identity.has("lastName")){
                    String lastname = identity.getString("lastName");
                    mIdentity.setLastName(lastname);
                }

                if (!identity.isNull("birthday") && identity.has("birthday")){
                    String birthday = identity.getString("birthday");
                    mIdentity.setBirthday(birthday);
                }

                if (!identity.isNull("mobilePhone") && identity.has("mobilePhone")){
                    String mobilePhone = identity.getString("mobilePhone");
                    mIdentity.setMobilePhone(mobilePhone);
                }

                if (!identity.isNull("phone") && identity.has("phone")){
                    String phone = identity.getString("phone");
                    mIdentity.setPhone(phone);
                }

                if (!identity.isNull("about") && identity.has("about")){
                    String about = identity.getString("about");
                    mIdentity.setAbout(about);
                }

                return mIdentity;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Identity Object is Null.");
        }

        return null;

    }

    private static FacebookModel parserFacebook(JSONObject facebook) throws JSONException{
        if (facebook != null) {

            try {

                FacebookModel mFacebook = new FacebookModel();

                if (!facebook.isNull("id") && facebook.has("id")){
                    String f_id = facebook.getString("id");
                    mFacebook.setId(f_id);
                }

                if (!facebook.isNull("token") && facebook.has("token")){
                    String token = facebook.getString("token");
                    mFacebook.setToken(token);
                }

                if (!facebook.isNull("name") && facebook.has("name")){
                    String name = facebook.getString("name");
                    mFacebook.setName(name);
                }

                if (!facebook.isNull("avatar") && facebook.has("avatar")){
                    String f_avatar = facebook.getString("avatar");
                    mFacebook.setAvatar(f_avatar);
                }

                if (!facebook.isNull("email") && facebook.has("email")){
                    String f_email = facebook.getString("email");
                    mFacebook.setEmail(f_email);
                }

                return mFacebook;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Facebook Object is Null.");
        }

        return null;

    }

    private static GoogleModel parserGoogle(JSONObject google) throws JSONException{
        if (google != null) {

            try {

                GoogleModel mGoogle = new GoogleModel();

                if (!google.isNull("id") && google.has("id")){
                    String og_id = google.getString("id");
                    mGoogle.setId(og_id);
                }

                if (!google.isNull("avatar") && google.has("avatar")){
                    String og_avatar = google.getString("avatar");
                    mGoogle.setAvatar(og_avatar);
                }

                if (!google.isNull("token") && google.has("token")){
                    String og_token = google.getString("token");
                    mGoogle.setToken(og_token);
                }

                if (!google.isNull("name") && google.has("name")){
                    String og_name = google.getString("name");
                    mGoogle.setName(og_name);
                }

                if (!google.isNull("email") && google.has("email")){
                    String og_email = google.getString("email");
                    mGoogle.setName(og_email);
                }

                return mGoogle;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Google Object is Null.");
        }

        return null;

    }

    private static CommentModel parserPlaceComment(JSONObject comment) throws JSONException{
        if (comment != null) {

            try {

                CommentModel mComment = new CommentModel();

                if (!comment.isNull("_id") && comment.has("_id")){
                    String pc_id = comment.getString("_id");
                    mComment.set_id(pc_id);
                }

                if (!comment.isNull("owner") && comment.has("owner")){
                    JSONObject pc_owner = comment.getJSONObject("owner");
                    OwnerModel mPCOwner = parserOwner(pc_owner);
                    mComment.setOwner(mPCOwner);
                }

                if (!comment.isNull("date") && comment.has("date")){
                    String pc_date = comment.getString("date");
                    mComment.setDate(pc_date);
                }

                if (!comment.isNull("comment") && comment.has("comment")){
                    String pc_content = comment.getString("comment");
                    mComment.setComment(pc_content);
                }

                if (!comment.isNull("cleanness") && comment.has("cleanness")){
                    int pc_cleanness = comment.getInt("cleanness");
                    mComment.setCleanness(pc_cleanness);
                }

                if (!comment.isNull("location") && comment.has("location")){
                    int pc_location = comment.getInt("location");
                    mComment.setLocation(pc_location);
                }

                if (!comment.isNull("valueForMoney") && comment.has("valueForMoney")){
                    int pc_valueForMoney = comment.getInt("valueForMoney");
                    mComment.setValueForMoney(pc_valueForMoney);
                }

                return mComment;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Place Comment Object is Null.");
        }

        return null;

    }

    private static SpotCommentModel parserSpotComment(JSONObject comment) throws JSONException{
        if (comment != null) {

            try {

                SpotCommentModel mSpotComment = new SpotCommentModel();

                if (!comment.isNull("_id") && comment.has("_id")){
                    String sc_id = comment.getString("_id");
                    mSpotComment.set_id(sc_id);
                }

                if (!comment.isNull("owner") && comment.has("owner")){
                    JSONObject sc_owner = comment.getJSONObject("owner");
                    OwnerModel mSCOwner = parserOwner(sc_owner);
                    mSpotComment.setOwner(mSCOwner);
                }

                if (!comment.isNull("date") && comment.has("date")){
                    String sc_date = comment.getString("date");
                    mSpotComment.setDate(sc_date);
                }

                if (!comment.isNull("comment") && comment.has("comment")){
                    String sc_content = comment.getString("comment");
                    mSpotComment.setComment(sc_content);
                }

                if (!comment.isNull("difficulty") && comment.has("difficulty")){
                    int sc_difficulty = comment.getInt("difficulty");
                    mSpotComment.setDifficulty(sc_difficulty);
                }

                if (!comment.isNull("beauty") && comment.has("beauty")){
                    int sc_beauty = comment.getInt("beauty");
                    mSpotComment.setBeauty(sc_beauty);
                }

                if (!comment.isNull("quality") && comment.has("quality")){
                    int sc_quality = comment.getInt("quality");
                    mSpotComment.setQuality(sc_quality);
                }

                return mSpotComment;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Spot Comment Object is Null.");
        }

        return null;

    }

    private static RateModel parserPlaceRating(JSONObject rating) throws JSONException{
        if (rating != null) {

            try {

                RateModel mRatings = new RateModel();

                if (!rating.isNull("valueForMoney") && rating.has("valueForMoney")){
                    int pr_valueForMoney = rating.getInt("valueForMoney");
                    mRatings.setValueForMoney(pr_valueForMoney);
                }

                if (!rating.isNull("location") && rating.has("location")){
                    int pr_location = rating.getInt("location");
                    mRatings.setLocation(pr_location);
                }

                if (!rating.isNull("cleanness") && rating.has("cleanness")){
                    int pr_cleanness = rating.getInt("cleanness");
                    mRatings.setCleanness(pr_cleanness);
                }

                if (!rating.isNull("overallRating") && rating.has("overallRating")){
                    int pr_overallRating = rating.getInt("overallRating");
                    mRatings.setOverallRating(pr_overallRating);
                }

                if (!rating.isNull("numberOfRatings") && rating.has("numberOfRatings")){
                    int pr_numbersOfRatings = rating.getInt("numberOfRatings");
                    mRatings.setNumberOfRatings(pr_numbersOfRatings);
                }

                return mRatings;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Place Rating Object is Null.");
        }

        return null;

    }

    private static SpotRatingModel parserSpotRating(JSONObject rating) throws JSONException{
        if (rating != null) {

            try {

                SpotRatingModel mSRatings = new SpotRatingModel();

                if (!rating.isNull("difficulty") && rating.has("difficulty")){
                    int sr_difficulty = rating.getInt("difficulty");
                    mSRatings.setDifficulty(sr_difficulty);
                }

                if (!rating.isNull("beauty") && rating.has("beauty")){
                    int sr_beauty = rating.getInt("beauty");
                    mSRatings.setBeauty(sr_beauty);
                }

                if (!rating.isNull("quality") && rating.has("quality")){
                    int sr_quality = rating.getInt("quality");
                    mSRatings.setQuality(sr_quality);
                }

                if (!rating.isNull("overallRating") && rating.has("overallRating")){
                    int sr_overallRating = rating.getInt("overallRating");
                    mSRatings.setOverallRating(sr_overallRating);
                }

                if (!rating.isNull("numberOfRatings") && rating.has("numberOfRatings")){
                    int sr_numbersOfRatings = rating.getInt("numberOfRatings");
                    mSRatings.setNumberOfRatings(sr_numbersOfRatings);
                }

                return mSRatings;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Spot Rating Object is Null.");
        }

        return null;

    }

    private static HomeModel parserHome(JSONObject home) throws JSONException{
        if (home != null) {

            try {

                HomeModel mPHome = new HomeModel();

                if (!home.isNull("travellers") && home.has("travellers")){
                    int ph_travellers = home.getInt("travellers");
                    mPHome.setTravellers(ph_travellers);
                }

                if (!home.isNull("propertyType") && home.has("propertyType")){
                    String ph_property = home.getString("propertyType");
                    mPHome.setPropertyType(ph_property);
                }

                if (!home.isNull("price") && home.has("price")){
                    JSONObject ph_price = home.getJSONObject("price");
                    PriceModel mPrice = parserPrice(ph_price);
                    mPHome.setPrice(mPrice);
                }

                return mPHome;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Home Object is Null.");
        }

        return null;

    }

    private static PriceModel parserPrice(JSONObject price) throws JSONException{
        if (price != null) {

            try {

                PriceModel mPrice = new PriceModel();

                if (!price.isNull("highSeason") && price.has("highSeason")){
                    int php_highSeason = price.getInt("highSeason");
                    mPrice.setHightSeason(php_highSeason);
                }else{
                    mPrice.setHightSeason(0);
                }

                if (!price.isNull("lowSeason") && price.has("lowSeason")){
                    int php_lowSeason = price.getInt("lowSeason");
                    mPrice.setLowSeason(php_lowSeason);
                }else{
                    mPrice.setLowSeason(0);
                }

                return mPrice;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Price Object is Null.");
        }

        return null;

    }

    private static AddressModel parserAddress(JSONObject address) throws JSONException{
        if (address != null) {

            try {

                AddressModel mAddress = new AddressModel();

                if(!address.isNull("street_number") && address.has("street_number")){
                    int sa_street_number = address.getInt("street_number");
                    mAddress.setStreet_number(sa_street_number);
                }

                if(!address.isNull("postal_code") && address.has("postal_code")){
                    String sa_postal_code = address.getString("postal_code");
                    mAddress.setPostal_code(sa_postal_code);
                }

                if(!address.isNull("route") && address.has("route")){
                    String sa_route = address.getString("route");
                    mAddress.setRoute(sa_route);
                }

                if (!address.isNull("administrative_area_level_1") && address.has("administrative_area_level_1")){
                    String sa_admin_area_lvl_1 = address.getString("administrative_area_level_1");
                    mAddress.setAdministrative_area_level_1(sa_admin_area_lvl_1);
                }

                if (!address.isNull("country") && address.has("country")){
                    String sa_country = address.getString("country");
                    mAddress.setCountry(sa_country);
                }

                if (!address.isNull("locality") && address.has("locality")){
                    String sa_locality = address.getString("locality");
                    mAddress.setLocality(sa_locality);
                }

                return mAddress;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Address Object is Null.");
        }

        return null;

    }

    private static LocModel parserLoc(JSONObject loc) throws JSONException{
        if (loc != null) {

            try {

                LocModel mLoc = new LocModel();

                if(!loc.isNull("type") && loc.has("type")){
                    String type = loc.getString("type");
                    mLoc.setType(type);
                }

                if(!loc.isNull("coordinates") && loc.has("coordinates")){
                    JSONArray coordinates = loc.getJSONArray("coordinates");
                    double[] pCoordinates = new double[coordinates.length()];
                    for (int i=0; i<coordinates.length();i++){
                        pCoordinates[i] = coordinates.getDouble(i);
                    }
                    mLoc.setCoordinates(pCoordinates);
                }

                return mLoc;

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Loc Object is Null.");
        }
        return null;
    }

    private static LogInModel parserLogIn(JSONObject login) throws JSONException{
        if (login != null){
            try{
                LogInModel mLogIn = new LogInModel();

                if(!login.isNull("success") && login.has("success")){
                    boolean success = login.getBoolean("success");
                    mLogIn.setSuccess(success);
                }

                if(!login.isNull("user") && login.has("user")){
                    JSONObject user = login.getJSONObject("user");
                    OwnerModel mUser = parserOwner(user);
                    mLogIn.setUser(mUser);
                }

                if(!login.isNull("token") && login.has("token")){
                    String token = login.getString("token");
                    mLogIn.setToken(token);
                }

                return mLogIn;

            }catch (final JSONException e){
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Loc Object is Null.");
        }

        return null;
    }

}
