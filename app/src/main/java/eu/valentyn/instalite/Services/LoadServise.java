package eu.valentyn.instalite.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import eu.valentyn.instalite.Adapter.PostAdapter;
import eu.valentyn.instalite.Model.Post;

import static java.security.AccessController.getContext;


public class LoadServise extends Service {

    public void startService(PostAdapter postAdapter, List<Post> postList, List<String> followingList, Context mContext) {
        LoadTask task = new LoadTask();

        // this is the same as calling task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        task.execute(new MyTaskParams(postAdapter, postList, followingList, mContext));
    }


    private void log(String msg) {
        Log.d("Load Service", msg);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private static class MyTaskParams{
        List<Post> postList;
        PostAdapter postAdapter;
        private List<String> followingList;
        Context mContext;

        public MyTaskParams(PostAdapter postAdapter, List<Post> postList, List<String> followingList, Context mContext){
            this.postList = postList;
            this.postAdapter = postAdapter;
            this.followingList = followingList;;
            this.mContext = mContext;
        }
    }



    private class LoadTask extends AsyncTask<MyTaskParams,Void, Void> /* Params, Progress, Result */ {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            log("LoadService: entered");
        }

        @Override
        protected Void doInBackground(MyTaskParams... myTaskParams) {


            for(MyTaskParams param : myTaskParams)
            {
                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        param.postList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);

                            for (String id : param.followingList) {
                                if (post.getPublisher().equals(id)){
                                    param.postList.add(post);
                                }
                            }
                        }

                        //SavePosts(param.postList, param.mContext);

                        param.postAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            log("LoadService: is finished");
        }

        private void log(String msg) {
            Log.d("LoadTask Msg:", msg);
        }

    }

    private static String PostsToString(List<Post> posts){
        StringBuilder str = new StringBuilder();

        for(Post item : posts){
            str.append(item.toString());
            str.append("\n");
        }
        return str.toString();
    }



    void SavePosts(List<Post> posts, Context mContext) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "Posts");

        for(Post post : posts){
            File PostDir = new File(mediaStorageDir, post.getPostid());
            String path =  PostDir.getPath();

            if (!PostDir.exists()) {
                if (!PostDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }

            //SavePostInfo(path +"/PostsInfo" + post.getPostid()  + ".txt", post.toString());
            //SavePicture(path, post, mContext);
            //SavePicture(path + "/" + post.getPostid() + ".jpg", post);
        }
    }


    public void SavePostInfo(String path, String text) {
        FileOutputStream fos = null;

        try
        {
            //fos = new FileOutputStream(path);
            fos = openFileOutput(path, MODE_PRIVATE);
            fos.write(text.getBytes());

            //mEditText.getText().clear();
            Log.wtf("write", "Saved Posts to /" + path);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String getFileExtension(Uri uri, Context mContext) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mContext.getContentResolver().getType(uri));

    }

    private static void SavePicture(String path, Post post, Context mContext){
        try {
            Bitmap picture = BitmapFactory.decodeStream(new URL(post.getImageurl()).openConnection().getInputStream());

            try{
                OutputStream pictureStream = null;
                pictureStream = new FileOutputStream(path);

                picture.compress(Bitmap.CompressFormat.JPEG,100, pictureStream);

                pictureStream.flush();
                pictureStream.close();

            }catch (IOException e)
            {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }


/*
    //String fileName = post.getDescription() + ".txt";
    String fileName = "test.txt";

    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    String text = "test";

    FileOutputStream fos = null;
                try {
        fos = new FileOutputStream(path);
        //fos = openFileOutput(path, MODE_PRIVATE);
        fos.write(text.getBytes());

        //mEditText.getText().clear();
        Log.wtf("write", "Saved Posts to /" + path);

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
/*
    public void load(View v) {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            //StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                String[] postItems = text.split(" ");

                sb.append(text).append("\n");
            }

            //mEditText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    */

