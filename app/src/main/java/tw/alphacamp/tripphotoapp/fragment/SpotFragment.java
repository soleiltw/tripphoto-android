package tw.alphacamp.tripphotoapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import tw.alphacamp.tripphotoapp.R;
import tw.alphacamp.tripphotoapp.object.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 15/1/24.
 */
public class SpotFragment extends PlaceholderFragment {

    private List<Photo> photoList;

    private ListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = new ArrayList<>();
        listAdapter = new ListAdapter(getActivity(), R.layout.cell_album, photoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ListView gridView = (ListView)rootView.findViewById(R.id.list_view);
        gridView.setAdapter(listAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseQuery<Photo> photoParseQuery = ParseQuery.getQuery(Photo.class);
        photoParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        photoParseQuery.setMaxCacheAge(1000 * 60 * 5);
        photoParseQuery.orderByDescending("createdAt");
        photoParseQuery.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e == null) {
                    photoList.clear();
                    photoList.addAll(photos);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private class ListAdapter extends ArrayAdapter<Photo> {

        private LayoutInflater layoutInflater;

        /**
         * Constructor
         *
         * @param context  The current context.
         * @param resource The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.
         * @param objects  The objects to represent in the ListView.
         */
        public ListAdapter(Context context, int resource, List<Photo> objects) {
            super(context, resource, objects);
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rootView = convertView;
            if (rootView == null) {
                rootView = layoutInflater.inflate(R.layout.cell_spot, parent, false);
            }

            Photo currentPhoto = getItem(position);
            final ImageView imageView = (ImageView)rootView.findViewById(R.id.spot_image_view);
            if (currentPhoto.getImageFile() != null) {
                String imageFileUrl = currentPhoto.getImageFile().getUrl();
                Ion.with(getActivity())
                        .load(imageFileUrl)
                        .withBitmap()
                        .intoImageView(imageView);
            }

            TextView titleTextView = (TextView)rootView.findViewById(R.id.spot_title_text_view);
            titleTextView.setText(currentPhoto.getName());

            return rootView;
        }
    }
}
