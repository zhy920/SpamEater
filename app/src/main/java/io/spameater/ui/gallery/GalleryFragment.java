package io.spameater.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.spameater.MsgVo;
import io.spameater.R;
import io.spameater.WriteAndRead;
import io.spameater.databinding.FragmentGalleryBinding;
import io.spameater.ui.home.AdapterSideBar;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            binding = FragmentGalleryBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            final RecyclerView recyclerViewKeywords = binding.recyclerViewKeywords;
            List<MsgVo> list = new ArrayList<>();

//            list.add(msgVo);
            io.spameater.ui.gallery.AdapterSideBar adapterSideBar = new io.spameater.ui.gallery.AdapterSideBar(this.getActivity(), list);
            recyclerViewKeywords.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewKeywords.addItemDecoration(new DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL));
            recyclerViewKeywords.setAdapter(adapterSideBar);
            final Button button = binding.keywordsBtn;
            final EditText editText=binding.keywordsInput;
            button.setOnClickListener(v -> {
                String keyword=editText.getText().toString();
                try {
                    WriteAndRead.save(keyword,getContext());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                MsgVo msgVo=new MsgVo();
                msgVo.setContent(keyword);
                adapterSideBar.addData(msgVo);
            });
            String[] strings=WriteAndRead.read(getContext());
            if (strings!=null){
                for (String string: strings){
                    MsgVo msgVo=new MsgVo();
                    msgVo.setContent(string);
                    adapterSideBar.addData(msgVo);
                }
            }
            return root;
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}