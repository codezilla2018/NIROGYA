package codezilla.foss.nirogya.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.adapters.ExpandableListAdapter;

public class InstructionsActivity extends Activity {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar_color));
        }
        // get the listview
        expandableListView = findViewById(R.id.instructions_list);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expandableListView.setAdapter(listAdapter);
        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });
        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        // Listview on child click listener
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_1));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_2));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_3));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_4));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_5));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_6));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_7));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_8));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_9));
        listDataHeader.add(getResources().getString(R.string.instructions_activity_header_10));

        // Adding child data
        List<String> instruction1 = new ArrayList<>();
        instruction1.add(getResources().getString(R.string.instructions_activity_header_1_child_data));
        List<String> instruction2 = new ArrayList<>();
        instruction2.add(getResources().getString(R.string.instructions_activity_header_2_child_data));
        List<String> instruction3 = new ArrayList<>();
        instruction3.add(getResources().getString(R.string.instructions_activity_header_3_child_data));
        List<String> instruction4 = new ArrayList<>();
        instruction4.add(getResources().getString(R.string.instructions_activity_header_4_child_data));
        List<String> instruction5 = new ArrayList<>();
        instruction5.add(getResources().getString(R.string.instructions_activity_header_5_child_data));
        List<String> instruction6 = new ArrayList<>();
        instruction6.add(getResources().getString(R.string.instructions_activity_header_6_child_data));
        List<String> instruction7 = new ArrayList<>();
        instruction7.add(getResources().getString(R.string.instructions_activity_header_7_child_data));
        List<String> instruction8 = new ArrayList<>();
        instruction8.add(getResources().getString(R.string.instructions_activity_header_8_child_data));
        List<String> instruction9 = new ArrayList<>();
        instruction9.add(getResources().getString(R.string.instructions_activity_header_9_child_data));
        List<String> instruction10 = new ArrayList<>();
        instruction10.add(getResources().getString(R.string.instructions_activity_header_10_child_data));

        listDataChild.put(listDataHeader.get(0), instruction1);
        listDataChild.put(listDataHeader.get(1), instruction2);
        listDataChild.put(listDataHeader.get(2), instruction3);
        listDataChild.put(listDataHeader.get(3), instruction4);
        listDataChild.put(listDataHeader.get(4), instruction5);
        listDataChild.put(listDataHeader.get(5), instruction6);
        listDataChild.put(listDataHeader.get(6), instruction7);
        listDataChild.put(listDataHeader.get(7), instruction8);
        listDataChild.put(listDataHeader.get(8), instruction9);
        listDataChild.put(listDataHeader.get(9), instruction10);

    }
}
