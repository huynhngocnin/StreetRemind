package vn.nin.app.streetremind.database;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.util.Log;

import java.util.HashMap;
import java.util.Map;

import vn.nin.app.streetremind.Module.Location;

/**
 * Created by ninhn on 2016/01/29.
 */
public class DatabaseHelper {
    public static final String TAG = "couchbaseevents";

    public static String addLocation(Database database, Location local) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("", "Big Party");
        map.put("location", "My House");
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


}
