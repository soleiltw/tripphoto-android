package tw.alphacamp.tripphotoapp.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
public class AlbumFragment extends PlaceholderFragment {

    private AlbumAdapter albumAdapter;
    private List<Photo> photoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = new ArrayList<>();
        albumAdapter = new AlbumAdapter(getActivity(), R.layout.cell_album, photoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        GridView gridView = (GridView)rootView.findViewById(R.id.album_grid_view);
        gridView.setAdapter(albumAdapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                loadObjects(new LoadObjectCallback() {
                    @Override
                    public void didFinishLoad() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, true);

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadObjects(null, false);
    }

    private void loadObjects(final LoadObjectCallback loadObjectCallback, boolean clearCache) {
        ParseQuery<Photo> photoParseQuery = ParseQuery.getQuery(Photo.class);
        photoParseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        photoParseQuery.setMaxCacheAge(1000 * 60 * 5);

        if (clearCache) {
            photoParseQuery.clearCachedResult();
        }
        photoParseQuery.orderByDescending("createdAt");
        photoParseQuery.findInBackground(new FindCallback<Photo>() {
            @Override
            public void done(List<Photo> photos, ParseException e) {
                if (e == null) {
                    photoList.clear();
                    photoList.addAll(photos);
                    albumAdapter.notifyDataSetChanged();
                    if (loadObjectCallback != null) {
                        loadObjectCallback.didFinishLoad();
                    }
                }
            }
        });
    }

    private interface LoadObjectCallback {
        public void didFinishLoad();
    }

    private class AlbumAdapter extends ArrayAdapter<Photo> {

        private LayoutInflater layoutInflater;

        private int screenWidth;

        /**
         * Constructor
         *
         * @param context  The current context.
         * @param resource The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.
         * @param objects  The objects to represent in the ListView.
         */
        public AlbumAdapter(Context context, int resource, List<Photo> objects) {
            super(context, resource, objects);
            layoutInflater = LayoutInflater.from(context);

            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rootView = convertView;
            if (rootView == null) {
                rootView = layoutInflater.inflate(R.layout.cell_album, parent, false);
            }


            Photo currentPhoto = getItem(position);
            final ImageView imageView = (ImageView)rootView.findViewById(R.id.album_image_view);
            if (currentPhoto.getImageFile() != null) {
                String imageFileUrl = currentPhoto.getImageFile().getUrl();
                Ion.with(getActivity())
                        .load(imageFileUrl)
                        .withBitmap()
                        .resize(screenWidth / 2, screenWidth / 2 * 9 / 16)
                        .centerCrop()
                        .intoImageView(imageView);
            }

            return rootView;
        }
    }
}
