package org.netcs.service;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.netcs.model.sql.User;
import org.netcs.model.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Functions executed asynchronously.
 *
 * @author ichatz@gmail.com.
 */
@Service
public class MixPanelService {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(MixPanelService.class);

    private static final String MIXPANEL_IMPORT_URL_STRING = "http://api.mixpanel.com/import/";

    @Value("${mixpanel.token:none}")
    private String mixpanelProjectToken;//= "17bc9d687e9ba7d358f4e95531277a34";
    @Value("${mixpanel.apiKey:none}")
    private String mixpanelApiKey;//= "8b48557903fdd92730800f668c22aaa3";

    @Autowired
    UserRepository userRepository;

    private MessageBuilder messageBuilder;
    private MixpanelAPI mixpanel;

    @PostConstruct
    public void init() {
        messageBuilder = new MessageBuilder(mixpanelProjectToken);
        mixpanel = new MixpanelAPI();
    }

    /**
     * Log a message for a specific user.
     */
    @Async
    public void log(final User user, final String tag, final String level, final String text) {
        if ("none".equals(mixpanelProjectToken)) {
            LOGGER.warn("mixpanel logging is not available");
        } else {
            try {
                trackInMixPanel(user.getId(), tag, level, text);
            } catch (JSONException | IOException e) {
                LOGGER.error(e, e);
            }
        }
    }

    @Async
    public void updateUserProfile(final long userId, final String key, final Object value) {
        try {
            // Sets user 13793's "Plan" attribute to "Premium"
            // This creates a profile for 13793 if one does not
            // already exist.
            final JSONObject props = new JSONObject();
            props.put(key, value);
            final JSONObject update = messageBuilder.set(String.valueOf(userId), props);

            // Send the update to mixpanel
            mixpanel.sendMessage(update);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signup(final Long userId) {
        try {
            trackInMixPanel(userId, "$signup", null, null);
        } catch (IOException | JSONException e) {
            LOGGER.error(e, e);
        }
    }

    public void login(final Long userId) {
        try {
            trackInMixPanel(userId, "login", null, null);
        } catch (IOException | JSONException e) {
            LOGGER.error(e, e);
        }
    }

    /**
     * Track an event to Mixpanel using the current Date.
     */
    private void trackInMixPanel(final long userId, final String tag, final String level, final String text) throws JSONException, IOException {
        trackInMixPanel(userId, tag, level, text, new Date());
    }

    /**
     * Track an event to Mixpanel on a specific date.
     */
    private void trackInMixPanel(final long userId, final String tag, final String level, final String text, final Date date) throws JSONException, IOException {
        trackInMixPanel(userId, tag, level, text, date.getTime());
    }

    /**
     * Track an event to Mixpanel on a specific millisecond.
     */
    private void trackInMixPanel(final long userId, final String tag, final String level, final String text, final long time) throws JSONException, IOException {

        // You can send properties along with events
        final JSONObject props = new JSONObject();
        final int static1000 = 1000;
        if (level != null) {
            props.put("level", level);
        }
        if (text != null) {
            props.put("description", text);
        }
        props.put("time", time / static1000);

        final JSONObject planEvent =
                messageBuilder.event(String.valueOf(userId), tag, props);

        // Gather together a bunch of messages into a single
        // ClientDelivery. This can happen in a separate thread
        // or process from the call to MessageBuilder.event()
        final ClientDelivery delivery = new ClientDelivery();
        delivery.addMessage(planEvent);

        // Use an instance of MixpanelAPI to send the messages
        // to Mixpanel's servers.
        mixpanel.deliver(delivery);
    }

//    /**
//     * Importer for old events to Mixpanel on a specific date
//     */
//    private void importInMixPanel(final long userId, final String tag, final String level, final String text, final Date date) throws JSONException, IOException {
//        importInMixPanel(userId, tag, level, text, date.getTime());
//    }
//
//    /**
//     * Import old events to Mixpanel on a specific millisecond
//     */
//    private void importInMixPanel(final long userId, final String tag, final String level, final String text, final long time) throws JSONException, IOException {
//        // You can send properties along with events
//        final JSONObject props = new JSONObject();
//        if (level != null) {
//            props.put("level", level);
//        }
//        if (text != null) {
//            props.put("description", text);
//        }
//        props.put("time", time / 1000);
//
//        final JSONObject event =
//                messageBuilder.event(String.valueOf(userId), tag, props);
//
//        final String dataString = event.getJSONObject("message").toString();
//        final boolean resp = importData(dataString);
//        LOGGER.info(resp + " event: " + event);
//    }

    /**
     * Taken by the Mixpanel library. Sends an event to the Mixpanel api. Used only to import old (>5 days) data. To
     * track new events use {@see trackInMixPanel}.
     *
     * @param dataString the json event as a string.
     * @return true if the event was imported successfully
     */
    private boolean importData(final String dataString) throws IOException {
        final URL endpoint = new URL(MIXPANEL_IMPORT_URL_STRING);
        final URLConnection conn = endpoint.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf8");

        final byte[] utf8data;
        utf8data = dataString.getBytes(Charset.forName("UTF-8"));

        final String base64data = new String(Base64Coder.encode(utf8data));
        final String encodedData = URLEncoder.encode(base64data, "utf8");
        final String encodedQuery = "data=" + encodedData + "&api_key=" + mixpanelApiKey;

        OutputStream postStream = null;
        try {
            postStream = conn.getOutputStream();
            postStream.write(encodedQuery.getBytes(Charset.forName("UTF-8")));
        } finally {
            if (postStream != null) {
                try {
                    postStream.close();
                } catch (IOException e) {
                    LOGGER.info(e, e);
                    // ignore, in case we've already thrown
                }
            }
        }

        InputStream responseStream = null;
        String response = null;
        try {
            responseStream = conn.getInputStream();
            response = slurp(responseStream);
        } finally {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    LOGGER.info(e, e);
                    // ignore, in case we've already thrown
                }
            }
        }

        return ((response != null) && response.equals("1"));
    }

    private static final int BUFFER_SIZE = 256; // Small, we expect small responses.


    private String slurp(final InputStream in) throws IOException {
        final StringBuilder out = new StringBuilder();
        final InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));

        char[] readBuffer = new char[BUFFER_SIZE];
        int readCount = 0;
        do {
            readCount = reader.read(readBuffer);
            if (readCount > 0) {
                out.append(readBuffer, 0, readCount);
            }
        } while (readCount != -1);

        return out.toString();
    }
}


