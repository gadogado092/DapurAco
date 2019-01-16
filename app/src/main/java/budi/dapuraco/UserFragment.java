package budi.dapuraco;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment{
    private View v;
    private List<Users> usersList;
    private UsersAdapter usersAdapter;
    private FirebaseFirestore mFirebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.layoutuser, container, false);

        mFirebaseFirestore= FirebaseFirestore.getInstance();

        //init RecyclerView
        usersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(container.getContext(),usersList);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(usersAdapter);
        //add space item decoration and pass space you want to give
        //recyclerView.addItemDecoration(new Space(20));
        //finally set adapter
        //recyclerView.setAdapter(new Adapter(createDummyList(), getContext()));
        //getdata();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        usersList.clear();
        mFirebaseFirestore.collection("Users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    String userId=doc.getDocument().getId();

                    Users users= doc.getDocument().toObject(Users.class).WithId(userId);
                    usersList.add(users);
                    usersAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
