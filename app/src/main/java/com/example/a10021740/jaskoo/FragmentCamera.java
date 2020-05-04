package com.example.a10021740.jaskoo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.example.a10021740.jaskoo.FragmentBad.badAdapter;
import static com.example.a10021740.jaskoo.FragmentBad.badCardList;
import static com.example.a10021740.jaskoo.FragmentBad.badRecyclerView;
import static com.example.a10021740.jaskoo.FragmentDiet.cardList;
import static com.example.a10021740.jaskoo.FragmentDiet.dietAdapter;
import static com.example.a10021740.jaskoo.FragmentDiet.dietRecyclerView;
import static com.example.a10021740.jaskoo.FragmentFavorites.favoriteCardsList;
import static com.example.a10021740.jaskoo.FragmentGood.goodAdapter;
import static com.example.a10021740.jaskoo.FragmentGood.goodCardList;
import static com.example.a10021740.jaskoo.FragmentGood.goodRecyclerView;

import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchAPI;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImageObject;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImagesModel;

import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

public class FragmentCamera extends Fragment {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    View view;
    TextView textView;
    JSONObject barcodeData;
    URL url;
    URLConnection urlConnection;
    InputStream inputStream;
    public static String JSONInfo = "";
    String qrNumber;
    String product = "";
    public static String JSONInfoImage = "";
    JSONObject imageData;
    String CustomSearchAPIKey = "AIzaSyAmpzIDzwsp9zJqOd8GHO0dtHZWbgqFBKo";


    public FragmentCamera() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.camera_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        surfaceView =   view.findViewById(R.id.surfaceview);
        textView = view.findViewById(R.id.textview);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
        }
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE | Barcode.DATA_MATRIX).build();

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getContext()).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory();
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());
        final CameraSource mCameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    mCameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }


        }); // STARTS CAMERA
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            qrNumber = qrCodes.valueAt(0).displayValue;
                            new AsyncThread().execute(qrNumber);

                        }
                    });
                }
            }
        });


    }
    class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
        @Override
        public Tracker<Barcode> create(Barcode barcode) {
            return new MyBarcodeTracker();
        }
    }
    class MyBarcodeTracker extends Tracker<Barcode> {
        @Override
        public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode barcode) {
            if (detectionResults.getDetectedItems().size() == 0) {
            }
        }
    }
    public class AsyncThread extends AsyncTask<String, Void, Void> {


        String src;
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            JSONInfo = "{\"old_api_id\":null,\"item_id\":\"51d2f8f4cc9bff111580cbf9\",\"item_name\":\"Classic Potato Chips\",\"leg_loc_id\":null,\"brand_id\":\"51db37cb176fe9790a89992a\",\"brand_name\":\"Lay's\",\"item_description\":\"Classic\",\"updated_at\":\"2019-03-15T07:28:13.000Z\",\"nf_ingredient_statement\":\"Potatoes, Vegetable Oil (Sunflower, Corn and/or Canola Oil), and Salt. Gluten free.\",\"nf_water_grams\":null,\"nf_calories\":160,\"nf_calories_from_fat\":90,\"nf_total_fat\":10,\"nf_saturated_fat\":1.5,\"nf_trans_fatty_acid\":0,\"nf_polyunsaturated_fat\":null,\"nf_monounsaturated_fat\":null,\"nf_cholesterol\":0,\"nf_sodium\":170,\"nf_total_carbohydrate\":15,\"nf_dietary_fiber\":1,\"nf_sugars\":0.5,\"nf_protein\":2,\"nf_vitamin_a_dv\":null,\"nf_vitamin_c_dv\":6,\"nf_calcium_dv\":0,\"nf_iron_dv\":2,\"nf_refuse_pct\":null,\"nf_servings_per_container\":1,\"nf_serving_size_qty\":1,\"nf_serving_size_unit\":\"package\",\"nf_serving_weight_grams\":28,\"allergen_contains_milk\":null,\"allergen_contains_eggs\":null,\"allergen_contains_fish\":null,\"allergen_contains_shellfish\":null,\"allergen_contains_tree_nuts\":null,\"allergen_contains_peanuts\":null,\"allergen_contains_wheat\":null,\"allergen_contains_soybeans\":null,\"allergen_contains_gluten\":null,\"usda_fields\":null}";
            Log.d("JSONINFO", JSONInfo);
            try {
                barcodeData = new JSONObject(JSONInfo);
                product = barcodeData.getString("item_name");
                String calories = barcodeData.getString("nf_calories");
                String fats = barcodeData.getString("nf_total_fat");
                String proteins = barcodeData.getString("nf_protein");
                String carbs = barcodeData.getString("nf_total_carbohydrate");

                //guiding stars

                double satFat = 0;
                double cholesterol = 0;
                double sugars = 0;
                double sodium = 0;
                double fiber = 0;
                double calcium = 0;
                double iron = 0;
                double vita = 0;
                double vitc = 0;

                int guidingStars = 0;

                Log.d("satFat", barcodeData.getString("nf_saturated_fat"));

                if ((!barcodeData.isNull("nf_saturated_fat"))) {
                    satFat = Double.parseDouble(barcodeData.getString("nf_saturated_fat"));
                    if( satFat >= 1 && satFat<= 2)
                        guidingStars-=1;
                    if( satFat > 2 && satFat <= 3)
                        guidingStars-=2;
                    if( satFat > 3)
                        guidingStars-=3;
                    Log.d("HELLO BOYS", "asfjkhajkshfjkashfjkahsfjkhjasf");
                }
                if (!barcodeData.isNull("nf_cholesterol")) {
                    cholesterol = Integer.parseInt(barcodeData.getString("nf_cholesterol"));
                    if( cholesterol >= 15 && cholesterol<= 30)
                        guidingStars-=1;
                    if( cholesterol > 30 && cholesterol <= 45)
                        guidingStars-=2;
                    if( cholesterol > 45)
                        guidingStars-=3;
                }
                if (!barcodeData.isNull("nf_sugars")) {
                    sugars = Double.parseDouble(barcodeData.getString("nf_sugars"));
                    if( sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 >= 0 && sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100<= 10)
                        guidingStars-=1;
                    if( sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 > 10 && sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 <= 25)
                        guidingStars-=2;
                    if( sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 > 25 && sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 <= 40)
                        guidingStars-=3;
                    if (sugars*( (int)((double)4/(double)Integer.parseInt(calories)) )*100 >  40)
                        guidingStars-=10;
                }
                if (!barcodeData.isNull("nf_sodium")) {
                    sodium = Double.parseDouble(barcodeData.getString("nf_sodium"));
                    if( sodium >= 120 && sodium<= 240)
                        guidingStars-=1;
                    if( sodium > 240 && sodium <= 360)
                        guidingStars-=2;
                    if( sodium > 360 && sodium <= 600)
                        guidingStars-=3;
                    if ( sodium > 600)
                        guidingStars-=10;
                }
                if (!barcodeData.isNull("nf_dietary_fiber")) {
                    fiber = Double.parseDouble(barcodeData.getString("nf_dietary_fiber"));
                    if (fiber >= 1.25 && fiber <= 2.5)
                        guidingStars +=1;
                    if (fiber > 2.5 && fiber <= 3.75)
                        guidingStars +=2;
                    if (fiber > 3.75)
                        guidingStars+=2;
                }

                if (!barcodeData.isNull("nf_calcium_dv") && !barcodeData.isNull("nf_iron_dv") && !barcodeData.isNull("nf_vitamin_a_dv") && !barcodeData.isNull("nf_vitamin_c_dv")) {
                    calcium = Double.parseDouble(barcodeData.getString("nf_calcium_dv"));
                    iron = Double.parseDouble(barcodeData.getString("nf_iron_dv"));
                    vita = Double.parseDouble(barcodeData.getString("nf_vitamin_a_dv"));
                    vitc = Double.parseDouble(barcodeData.getString("nf_vitamin_c_dv"));


                    if ( (calcium >= 10 && iron >= 10) || (calcium >= 10 && vita >= 10) || (calcium >= 10 && vitc >= 10) || (iron >= 10 && vita >= 10) || (iron >= 10 && vitc >= 10) || (vita >= 10 && vitc >= 10) ){
                        guidingStars+=3;
                    }else {
                        if (calcium >= 5 && calcium < 10)
                            guidingStars += 1;
                        if (iron >= 5 && iron < 10)
                            guidingStars += 1;
                        if (vita >= 5 && vita < 10)
                            guidingStars += 1;
                        if (vitc >= 5 && vitc < 10)
                            guidingStars += 1;
                    }

                }

                Log.d("guided", guidingStars+"");

                imageData = new JSONObject(JSONInfoImage);
                JSONArray items = imageData.getJSONArray("items");
                JSONObject zero = items.getJSONObject(0);
                JSONObject item = zero.getJSONObject("pagemap");
                JSONArray cse = item.getJSONArray("cse_image");
                JSONObject bleh = cse.getJSONObject(0);
                String src = bleh.getString("src");

                Log.d("heyyy", src);




                Log.d("JSONInfoImage", JSONInfoImage);



                //images

                cardList.add(new Card(product, calories, proteins, carbs, fats, src, guidingStars));

                if (guidingStars <= 0)
                    badCardList.add(new Card(product, calories, proteins, carbs, fats, src, guidingStars));
                if (guidingStars > 0)
                    goodCardList.add(new Card(product, calories, proteins, carbs, fats, src, guidingStars));

                Log.d("BAD", badCardList+"");
                Log.d("CARD", cardList+"");



                if (product != null && product != "")
                    textView.setText("Product "+product+" added!");
                else
                    textView.setText("Item not found!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(String... strings) {

            JSONInfo = "";
            JSONInfoImage = "";

            /*try {
                Log.d("CHECK", qrNumber);
                url = new URL("https://api.nutritionix.com/v1_1/item?upc="+ qrNumber+ "&appId=1f87f14c&appKey=d2a3b8b4b8835bbbaf8c8ed936807202"); // URL HAS TO FIXED
                Log.d("url", "https://api.nutritionix.com/v1_1/item?upc="+ qrNumber+ "&appId=1f87f14c&appKey=d2a3b8b4b8835bbbaf8c8ed936807202");
                urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String st;
                while ((st = bufferedReader.readLine()) != null) {
                    Log.d("TAG", st);
                    JSONInfo += st;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){

            }*/

            if (product != null && product != ""){
                try {
                    JSONInfo = "{\"old_api_id\":null,\"item_id\":\"51d2f8f4cc9bff111580cbf9\",\"item_name\":\"Classic Potato Chips\",\"leg_loc_id\":null,\"brand_id\":\"51db37cb176fe9790a89992a\",\"brand_name\":\"Lay's\",\"item_description\":\"Classic\",\"updated_at\":\"2019-03-15T07:28:13.000Z\",\"nf_ingredient_statement\":\"Potatoes, Vegetable Oil (Sunflower, Corn and/or Canola Oil), and Salt. Gluten free.\",\"nf_water_grams\":null,\"nf_calories\":160,\"nf_calories_from_fat\":90,\"nf_total_fat\":10,\"nf_saturated_fat\":1.5,\"nf_trans_fatty_acid\":0,\"nf_polyunsaturated_fat\":null,\"nf_monounsaturated_fat\":null,\"nf_cholesterol\":0,\"nf_sodium\":170,\"nf_total_carbohydrate\":15,\"nf_dietary_fiber\":1,\"nf_sugars\":0.5,\"nf_protein\":2,\"nf_vitamin_a_dv\":null,\"nf_vitamin_c_dv\":6,\"nf_calcium_dv\":0,\"nf_iron_dv\":2,\"nf_refuse_pct\":null,\"nf_servings_per_container\":1,\"nf_serving_size_qty\":1,\"nf_serving_size_unit\":\"package\",\"nf_serving_weight_grams\":28,\"allergen_contains_milk\":null,\"allergen_contains_eggs\":null,\"allergen_contains_fish\":null,\"allergen_contains_shellfish\":null,\"allergen_contains_tree_nuts\":null,\"allergen_contains_peanuts\":null,\"allergen_contains_wheat\":null,\"allergen_contains_soybeans\":null,\"allergen_contains_gluten\":null,\"usda_fields\":null}";
                    barcodeData = new JSONObject(JSONInfo);
                    product = barcodeData.getString("item_name");
                    url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAmpzIDzwsp9zJqOd8GHO0dtHZWbgqFBKo&cx=002385303341138250100:mbe_agumuqk&num=1&q="+product);
                    Log.d("URL", url+"");// URL HAS TO FIXED
                    urlConnection = url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String st;
                    while ((st = bufferedReader.readLine()) != null) {
                        Log.d("TAG", st);
                        JSONInfoImage += st;

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e){

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

    }

//    public class AsyncThreadImage extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("dayo", "yo");
//            Log.d("dayo", JSONInfoImage);
//
//            try {
//                imageData = new JSONObject(JSONInfoImage);
//                JSONArray items = imageData.getJSONArray("items");
//                JSONObject zero = items.getJSONObject(0);
//                JSONObject item = zero.getJSONObject("pagemap");
//                JSONArray cse = item.getJSONArray("cse_image");
//                JSONObject bleh = cse.getJSONObject(0);
//                String src = bleh.getString("src");
//                ImageView imageView = new ImageView(getContext());
//                Picasso.get().load(src).into(imageView);
//
//                Log.d("heyyy", src);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            try {
//                url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAmpzIDzwsp9zJqOd8GHO0dtHZWbgqFBKo&cx=002385303341138250100:mbe_agumuqk&num=1&q="+product); // URL HAS TO FIXED
//                urlConnection = url.openConnection();
//                inputStream = urlConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String st;
//                while ((st = bufferedReader.readLine()) != null) {
//                    Log.d("TAG", st);
//                    JSONInfoImage += st;
//
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e){
//
//            }
//            return null;
//        }
//
//
//    }

}

