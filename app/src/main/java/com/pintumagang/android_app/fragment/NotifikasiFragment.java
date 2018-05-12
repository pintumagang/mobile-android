package com.pintumagang.android_app.fragment;


import
android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pintumagang.android_app.R;
import com.pintumagang.android_app.config.SharedPrefManager;
import com.pintumagang.android_app.dao.FcmPushDao;
import com.pintumagang.android_app.entity.FcmPushInfo;
import com.pintumagang.android_app.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifikasiFragment extends Fragment {

    SQLiteDatabase db = null;
    private ListView pushListView = null;
    private FcmPuchAdapter pushAdapter = null;
    View rootView;
    ArrayList<FcmPushInfo> pushList = new ArrayList<FcmPushInfo>();


    public NotifikasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        int id_user = user.getId();
        pushList.clear();
        getFcmPushDbData(pushList,id_user);

        pushAdapter = new FcmPuchAdapter(getActivity().getApplicationContext(), R.layout.fcm_item, pushList);
        pushListView = (ListView) rootView.findViewById(R.id.recyclerViewNotifikasi);
        pushListView.setAdapter(pushAdapter);
        return rootView;
    }
    private void getFcmPushDbData(ArrayList<FcmPushInfo> pushList, int id_user) {
        FcmPushDao pushDao = new FcmPushDao(getActivity().getApplicationContext());

        String SQL_DB_SELECT_ALL
                = "SELECT " +
                "NO, ID_USER, TITLE, MESSAGE, strftime('%d/%m/%Y %H:%M', REG_DATE) AS REG_DATE " +
                "FROM FCM_PUSH_LOG " +
                "WHERE ID_USER = "+id_user+" " +
                "ORDER BY REG_DATE DESC";
        pushDao.selectAllDao(SQL_DB_SELECT_ALL, pushList);
    }

    public class FcmPuchAdapter extends ArrayAdapter<FcmPushInfo> {
        private ViewHolder mViewHolder = null;
        private LayoutInflater mInflater = null;

        class ViewHolder {
            public TextView no = null;
            public TextView title = null;
            public TextView message = null;
            public TextView regDate = null;
        }

        public FcmPuchAdapter(Context context, int resource, List<FcmPushInfo> info) {
            super(context, resource, info);

            this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(v == null) {
                mViewHolder = new ViewHolder();
                v = mInflater.inflate(R.layout.fcm_item, null);
                mViewHolder.title = (TextView) v.findViewById(R.id.textViewTitleNotifikasi);
                mViewHolder.message = (TextView) v.findViewById(R.id.textViewMessageNotifikasi);
                mViewHolder.regDate = (TextView) v.findViewById(R.id.textViewWaktuNotifikasi);

                v.setTag(mViewHolder);
            }else{
                mViewHolder = (ViewHolder) v.getTag();
            }

            mViewHolder.title.setText(getItem(position).getTitle());
            mViewHolder.message.setText(getItem(position).getMessage());
            mViewHolder.regDate.setText(getItem(position).getRegDate());

            return v;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public FcmPushInfo getItem(int position) {
            return super.getItem(position);
        }

    }

}
