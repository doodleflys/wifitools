package com.mx.wifichat.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mx.wifichat.R;
import com.mx.wifichat.util.LogUtil;

/**
 * Created by MX on 2015/6/16.
 */
public class TransferFragment extends Fragment {
    public static final String TAG = "TransferFragment";

    private static final int FILE_SELECT_REQUEST_CODE = 5;

    private Context mContext;
    private Button btnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_transfer, null);
        btnSend = (Button) view.findViewById(R.id.button_file_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_REQUEST_CODE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_REQUEST_CODE && data != null) {
            Uri uri = data.getData();
            Cursor cursor = null;
            cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            String[] columnNames = cursor.getColumnNames();
            int columns = columnNames.length;
            int i, index;
            if (cursor.moveToFirst()) {
                for (i = 0; i < columns; i++) {
                    index = cursor.getColumnIndex(columnNames[i]);
                    LogUtil.d(TAG, index + "-" + columnNames[i] + ":" + cursor.getString(index));
                }
            }
        } else {
            Toast.makeText(mContext, "获取文件失败", Toast.LENGTH_SHORT).show();
        }
    }
}
