package com.bui.angebotviewer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab3Fragment extends Fragment {

    //URL of Rest webservice
    final String res="http://10.0.2.2:8080/AngebotProduct/demo/Products/list";
    RecyclerView recyclerView;

    public tab3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView=(RecyclerView)inflater.inflate(R.layout.fragment_tab3,container,false);
        new HttpAsyncTask().execute(res);
        Log.d("thuyet","abc");
        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            /*// create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
              // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }
            */

            // create connection
            HttpURLConnection urlConnection = null;
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
            urlConnection.setConnectTimeout(60000);
            urlConnection.setReadTimeout(60000);
            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject json = null; // convert String to JSONObject
            JSONArray articles=null;
            try {
                //json = new JSONObject(result);
                articles = new JSONArray(result);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String[] names=new String[articles.length()];
            String[] images=new String[articles.length()];
            for(int i=0;i<articles.length();i++){
                try {
                    names[i]=articles.getJSONObject(i).getString("name");
                    images[i]=articles.getJSONObject(i).getString("imglink");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            CapImageAdapter capImageAdapter=new CapImageAdapter(names,images);
            recyclerView.setAdapter(capImageAdapter);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            /*int spanCount = 2; // 3 columns
            int spacing = 20; // 50px
            boolean includeEdge = false;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/



        }
    }




}
class CapImageAdapter extends RecyclerView.Adapter<CapImageAdapter.ViewHolder> {
    private String[] cap;
    private String[] ids;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    CapImageAdapter(String[] cap, String[] imageid) {
        this.cap = cap;
        this.ids = imageid;

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @Override
    public CapImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CapImageAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.info_img);
        new DownloadImageTask(imageView).execute(ids[position]);
        // Drawable drawable = cardView.getResources().getDrawable(ids[position]);
        //imageView.setImageDrawable(drawable);
        imageView.setContentDescription(cap[position]);
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(cap[position]);
    }

    @Override
    public int getItemCount() {
        return cap.length;
    }



}
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            mIcon11 = BitmapFactory.decodeStream(in,null,options);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}

