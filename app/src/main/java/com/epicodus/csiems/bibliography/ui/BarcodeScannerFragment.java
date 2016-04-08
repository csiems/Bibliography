package com.epicodus.csiems.bibliography.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerFragment extends DialogFragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;


    public BarcodeScannerFragment() {
        // Required empty public constructor
    }

    public static BarcodeScannerFragment newInstance() {
        return new BarcodeScannerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
//        SearchDisplayFragment searchDisplayFragment = new SearchDisplayFragment();
        Bundle args = new Bundle();
        args.putString("query", rawResult.toString());
//        searchDisplayFragment.setArguments(args);
        dismiss();
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_content_layout, searchDisplayFragment)
//                .addToBackStack(null)
//                .commit();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(BarcodeScannerFragment.this);
            }
        }, 5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
