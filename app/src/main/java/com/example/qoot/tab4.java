package com.example.qoot;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab4 extends Fragment {

    GridView gridView;
    ArrayList <Request> request;
    public tab4() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tab4, container, false);
        gridView=view.findViewById(R.id.grid_Request2);
        request=new ArrayList<Request>();
       // Request request4=new Request("Wedding","Pending","1111","2222", );
       // Request request2=new Request("bbq","Accepted","3333","4444", );
       // Request request3=new Request("Other","Accepted","5555","6666", );
       // request.add(request4);
        //request.add(request2);
        //request.add(request3);
     //   MyBrowseRequestAdapter2 myRequestAdapter2=new MyBrowseRequestAdapter2(getActivity(),R.layout.activity_browse_single_request_scheduled,request);
      ///  gridView.setAdapter(myRequestAdapter2);
        return view;
    }

}

class MyBrowseRequestAdapter2 extends BaseAdapter {
    private Context context;
    ArrayList<Request> request;
    int layoutResourseId;

    MyBrowseRequestAdapter2(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }
    public MyBrowseRequestAdapter2(Context context, int activity_browse_single_request_scheduled, ArrayList<Request> request)
    {
        this.request=request;
        this.context=context;
        this.layoutResourseId= activity_browse_single_request_scheduled;
        int size=getCount();
        // Toast.makeText(context,"size :", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getCount() {
        return request.size();
    }
    @Override
    public Object getItem(int position) {
        return request.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_browse_single_request_scheduled, null);
        TextView eventType=(TextView) view.findViewById(R.id.EventType2);
        TextView status=(TextView) view.findViewById(R.id.status2);
        eventType.setText(request.get(position).EventType);
        status.setText(request.get(position).Status);
        return view;
    }
}
