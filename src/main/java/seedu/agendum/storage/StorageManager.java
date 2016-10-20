package seedu.agendum.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.agendum.commons.core.ComponentManager;
import seedu.agendum.commons.core.LogsCenter;
import seedu.agendum.commons.events.model.LoadDataRequestEvent;
import seedu.agendum.commons.events.model.SaveLocationChangedEvent;
import seedu.agendum.commons.events.model.ToDoListChangedEvent;
import seedu.agendum.commons.events.storage.DataSavingExceptionEvent;
import seedu.agendum.commons.events.storage.LoadDataCompleteEvent;
import seedu.agendum.commons.exceptions.DataConversionException;
import seedu.agendum.commons.util.StringUtil;
import seedu.agendum.model.ReadOnlyToDoList;
import seedu.agendum.model.ToDoList;
import seedu.agendum.model.UserPrefs;

/**
 * Manages storage of ToDoList data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ToDoListStorage toDoListStorage;
    private UserPrefsStorage userPrefsStorage;

    public StorageManager(ToDoListStorage toDoListStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.toDoListStorage = toDoListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String toDoListFilePath, String userPrefsFilePath) {
        this(new XmlToDoListStorage(toDoListFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ ToDoList methods ==============================

    @Override
    public String getToDoListFilePath() {
        return toDoListStorage.getToDoListFilePath();
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList() throws DataConversionException, IOException {
        return readToDoList(toDoListStorage.getToDoListFilePath());
    }

    @Override
    public Optional<ReadOnlyToDoList> readToDoList(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return toDoListStorage.readToDoList(filePath);
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList) throws IOException {
        saveToDoList(toDoList, toDoListStorage.getToDoListFilePath());
    }

    @Override
    public void saveToDoList(ReadOnlyToDoList toDoList, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        toDoListStorage.saveToDoList(toDoList, filePath);
    }

    @Override
    public void setToDoListFilePath(String filePath){
        assert StringUtil.isValidPathToFile(filePath);
        toDoListStorage.setToDoListFilePath(filePath);
        logger.info("Setting todo list file path to: " + filePath);
    }

    @Override
    @Subscribe
    public void handleToDoListChangedEvent(ToDoListChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveToDoList(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Override
    @Subscribe
    public void handleSaveLocationChangedEvent(SaveLocationChangedEvent event) {
        String saveLocation = event.saveLocation;
        setToDoListFilePath(saveLocation);
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
    }
    

    @Override
    @Subscribe
    public void handleLoadDataRequestEvent(LoadDataRequestEvent event) {
        
        setToDoListFilePath(event.loadLocation);
        
        Optional<ReadOnlyToDoList> toDoListOptional;
        ReadOnlyToDoList loadedData;
        try {
            toDoListOptional = readToDoList();
            loadedData = toDoListOptional.get();
            logger.info("Loading successful - " + LogsCenter.getEventHandlingLogMessage(event));
        } catch (DataConversionException e) {
            logger.warning("Loading unsuccessful - Data file not in the correct format. Loading empty ToDoList");
            loadedData = new ToDoList();
        } catch (IOException e) {
            logger.warning("Loading unsuccessful - Problem while reading from the file. Loading empty ToDoList");
            loadedData = new ToDoList();
        }

        raise(new LoadDataCompleteEvent(loadedData));
    }
}
