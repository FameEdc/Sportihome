package fr.wildcodeschool.apprenti.sportihome.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.wildcodeschool.apprenti.sportihome.Font.CustomFontTextView;
import fr.wildcodeschool.apprenti.sportihome.R;
import fr.wildcodeschool.apprenti.sportihome.SportNames;



public class ListSportsAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<SportNames> sportNamesList = null;
    private ArrayList<SportNames> arraylist;
    private String hobby;
    private ArrayList<String> mesSports = new ArrayList<String>();

    public ListSportsAdapter(Context context, List<SportNames> animalNamesList) {
        mContext = context;
        this.sportNamesList = animalNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SportNames>();
        this.arraylist.addAll(animalNamesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return sportNamesList.size();
    }

    @Override
    public Object getItem(int position) {
        return sportNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<String> getMesSports(){
        return  mesSports;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.list_sports_items, null);

        //CheckBox INIT
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear_sport);
        CheckBox sportCheckbox = new CheckBox(mContext);
        String imgSportName = sportNamesList.get(position).getSportName().replace(" ","_");
        String checkName = "checkbox_"+imgSportName.toLowerCase();
        sportCheckbox.setTag(checkName);

        if (mesSports.size() != 0){
            for (int i = 0; i < mesSports.size();i++){
                String prout = mesSports.get(i);
                if (imgSportName.equals(prout)){
                    sportCheckbox.setChecked(true);
                }
            }
        }


        ll.addView(sportCheckbox,0);

        CustomFontTextView sportImg = (CustomFontTextView) view.findViewById(R.id.sport_img);

        TextView sportName = (TextView)view.findViewById(R.id.sport_name);

        imgSportName = "img_"+imgSportName.toLowerCase();
        String charSportFont = getStringResourceByName(imgSportName, mContext);

        sportImg.setText(charSportFont);
        sportName.setText(sportNamesList.get(position).getSportName());

        sportCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckboxClicked(v);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sportNamesList.clear();
        if (charText.length() == 0) {
            sportNamesList.addAll(arraylist);
        } else {
            for (SportNames wp : arraylist) {
                if (wp.getSportName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    sportNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    private String getStringResourceByName(String aString, Context context) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    //Get values of checkboxes clicked
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getTag().toString()) {
            case "checkbox_alpinisme":
                hobby = view.getTag().toString().replace("checkbox_","");
                if(checked){addSport(mesSports,hobby);}else{removeSport(mesSports,hobby);}
                break;
            case "checkbox_apnee":
                hobby = view.getTag().toString().replace("checkbox_","");
                if(checked){addSport(mesSports,hobby);}else{removeSport(mesSports,hobby);}
                break;
            case "checkbox_basketball":
                hobby = view.getTag().toString().replace("checkbox_","");
                if(checked){addSport(mesSports,hobby);}else{removeSport(mesSports,hobby);}
                break;
            case "checkbox_bmx":
                hobby = view.getTag().toString().replace("checkbox_","");
                if(checked){addSport(mesSports,hobby);}else{removeSport(mesSports,hobby);}
                break;
            case "checkbox_canoe_kayak":
                hobby = view.getTag().toString().replace("checkbox_","");
                if(checked){addSport(mesSports,hobby);}else{removeSport(mesSports,hobby);}
                break;
        }
    }

    private void addSport(ArrayList<String> mesSports,String hobby){
        if(mesSports.size() != 0){
            int ln = mesSports.size();
            boolean find = false;
            for (int i = 0; i < ln; i++){
                String id = mesSports.get(i);
                if(find == true && id.equals(hobby)){
                    find = true;
                }
            }
            if (find == false){
                mesSports.add(hobby);
            }
        }else{
            mesSports.add(hobby);
        }
    }

    private void removeSport(ArrayList<String> mesSports,String hobby){
        if (mesSports.size() != 0){
            int ln = mesSports.size();
            boolean find = false;
            int rm_pos= -1;
            for (int i = 0; i < ln; i++){
                String id = mesSports.get(i);
                if(find == false && id.equals(hobby)){
                    find = true;
                    rm_pos = i;
                }
            }
            if (find == true && rm_pos != -1){
                mesSports.remove(rm_pos);
            }
        }else{
            //IMPOSSIBLE !!!
        }
    }
}
