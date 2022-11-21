package edu.northeastern.numad22fa_team12.outfitTodayModel;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploadUtil {

    private StorageReference ref = FirebaseStorage.getInstance().getReference();

    public ImageUploadUtil(){

    }


    public String uploadAndGetDownLoadUrl(Uri uri){
        final StorageReference reff = ref.child(System.currentTimeMillis()+"."+String.valueOf(uri));
        final String[] url = new String[1];
        reff.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reff.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url[0] = String.valueOf(uri);
                    }
                });
            }
        });
        return url[0];
    }
}
