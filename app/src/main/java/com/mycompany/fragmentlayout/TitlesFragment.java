package com.mycompany.fragmentlayout;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TitlesFragment extends ListFragment {

    boolean mDualPane;
    static int mCurCheckPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Populate list with our static array of titles0
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));

        //Check to see if we have a frame in which to embed the details fragment directly in the containing UI
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if(savedInstanceState != null){
            //restore last state for checked position
            mCurCheckPosition = savedInstanceState.getInt("curChoice");
        }
        else {
            mCurCheckPosition = 0;
        }
        if(mDualPane){
            //In dual pane mode, the list view highlights the selected item
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            //Make sure our UI is in the correct state
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        getListView().setItemChecked(mCurCheckPosition,true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        showDetails(position);
    }

    /* Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a whole new activity
     * in which it is displayed
     */

    void showDetails(int index){
        mCurCheckPosition = index;

        if(mDualPane){

            // we can display everything in-place with fragments, so update
            // the list to highlight te selected item and show the data


            //check what fragment is currently shown, replace it if needed
            DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);

            if(details == null || details.getShownIndex() != mCurCheckPosition){
                //make new fragment to show this selection
                details = DetailsFragment.newInstance(mCurCheckPosition);

                //Execute a transaction, replacing any existing fragment with this one inside the frame
                FragmentTransaction ft = getFragmentManager().beginTransaction();

                //if(index == 0){
                    ft.replace(R.id.details, details);

                //} else {
                //    ft.replace(R.id.details, details);
                //}

                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            //otherwise we need to launch a new activity to display
            // the dialog fragment with selected text

            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("index", mCurCheckPosition);
            startActivity(intent);

        }

    }

}
