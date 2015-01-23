package tw.alphacamp.tripphotoapp;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import tw.alphacamp.tripphotoapp.object.Photo;

/**
 * Created by edward_chiang on 15/1/24.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, ParseSettings.APP_ID, ParseSettings.CLIENT_KEY);
        ParseObject.registerSubclass(Photo.class);
        ParseUser.enableAutomaticUser();

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
