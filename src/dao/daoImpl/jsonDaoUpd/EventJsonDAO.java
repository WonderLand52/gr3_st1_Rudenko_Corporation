package dao.daoImpl.jsonDaoUpd;

import corporationmodules.Module;
import dao.daoImpl.jdbcDAOUpd.DBHandler;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;
import journals.CollectionJournal;
import journals.Journal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import recordImpl.Importance;
import recordImpl.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventJsonDAO implements AutoCloseable {

    private static EventJsonDAO instance;

    protected static final String FILE_NAME_JSON_EVENT = "events.json";
    private static final String KEY_EVENT_DATE = "date";
    private static final String KEY_EVENT_IMPORTANCE = "importance";
    private static final String KEY_EVENT_SOURCE = "source";
    private static final String KEY_EVENT_MESSAGE = "message";

    private JsonFileHandler handler = new JsonFileHandler();
    protected JSONArray eventsJson;

    private static boolean alive = false;
    protected static boolean changed = false;

    private EventJsonDAO(){}

    public static EventJsonDAO getInstance() {
        if(instance == null){
            instance = new EventJsonDAO();
            alive = true;
            return instance;
        }

        return instance;
    }

    {
        try {
            eventsJson = handler.getArrayObjectsFromJson(FILE_NAME_JSON_EVENT);
        } catch (DaoSystemException e) {
            e.printStackTrace();
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(Record rec) throws DaoSystemException, NoSuchEntityException {

        //create new Json object
        JSONObject newEvent = new JSONObject();

        newEvent.put(KEY_EVENT_DATE, rec.getTime().getTime().toString());
        newEvent.put(KEY_EVENT_IMPORTANCE, rec.getImportance().toString());
        newEvent.put(KEY_EVENT_SOURCE, rec.getSource());
        newEvent.put(KEY_EVENT_MESSAGE, rec.getMessage());

        eventsJson.add(newEvent);

        changed = true;
    }

    public void addModuleEvent(Module module, String message) {

        try {
            addEvent(new Record(Calendar.getInstance(),
                                Importance.TRIVIAL,
                                module.getClass().getSimpleName(),
                                message + module));
        } catch (DaoSystemException e) {
            e.printStackTrace();
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
        }
    }

    public Journal getEvents() throws DaoSystemException, NoSuchEntityException {

        Journal records = new CollectionJournal();
        for(Object obj: eventsJson) {
            JSONObject jsonObj = (JSONObject) obj;
            Calendar cal;
            try {
                cal = parseCalendar((String) jsonObj.get(KEY_EVENT_DATE));
            } catch (ParseException e) {
                throw new DaoSystemException("Error while parsing events: " + e.getMessage());
            }

            records.add(new Record(cal,
                                Importance.TRIVIAL,
                            (String) jsonObj.get(KEY_EVENT_SOURCE),
                        (String) jsonObj.get(KEY_EVENT_MESSAGE)));
        }

        return records;
    }

    private Calendar parseCalendar(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(dateStr));
        return cal;
    }

    public void writeAllEvents() throws DaoSystemException, NoSuchEntityException {
        JsonFileHandler.writeJson(eventsJson, FILE_NAME_JSON_EVENT, false);
    }

    @Override
    public void close() throws Exception {
        DBHandler.closeDAO();
    }

    public static boolean isAlive() {
        return alive;
    }

    public static boolean isChanged() {
        return changed;
    }

    public static void setAlive(boolean bool) {
        alive = bool;
    }
}
