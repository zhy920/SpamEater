package io.spameater.ui.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.spameater.MsgVo;
import io.spameater.SMSBroadcastReceiver;
import io.spameater.WriteAndRead;
import io.spameater.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    List<MsgVo> list = new ArrayList<>();
    public final AdapterSideBar adapterSideBar = new AdapterSideBar(this.getActivity(), list);
    private FragmentHomeBinding binding;
    private boolean isOnScreen=false;
    public static ArrayList<String> addr= new ArrayList<>();
    public static ArrayList<String> body= new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) ;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        final RecyclerView recyclerViewHome = binding.recyclerViewHome;
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewHome.addItemDecoration(new DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL));
        recyclerViewHome.setAdapter(adapterSideBar);

        new Thread(()->{
            while (true) {
                while (addr.size() > 0) {
                    String Addr=addr.get(0);
                    String Body=body.get(0);
                    addr.remove(0);
                    body.remove(0);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            adapterSideBar.addData(-1, Addr, Body);

                        }
                    };
                    mHandler.post(runnable);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        WriteAndRead.creatSpamFolder(getContext());
        File directory = getContext().getFilesDir();
        File file = new File(directory+"/spam/");
        String[] files=file.list();

        for (String filepath:files){
            String[] string=WriteAndRead.read(Integer.valueOf(filepath),getContext());
            if (string!=null){
                addr.add(string[0]);
                body.add(string[1]);
            }
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        isOnScreen=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnScreen=false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}

