package seedu.agendum.model;

import seedu.agendum.commons.core.UnmodifiableObservableList;
import seedu.agendum.commons.events.storage.LoadDataCompleteEvent;
import seedu.agendum.model.ModelManager.NoPreviousListFoundException;
import seedu.agendum.model.task.ReadOnlyTask;
import seedu.agendum.model.task.Task;
import seedu.agendum.model.task.UniqueTaskList;

import java.util.List;
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyToDoList newData);

    /** Returns the ToDoList */
    ReadOnlyToDoList getToDoList();
  
    /** Deletes the given task(s) */
    void deleteTasks(List<ReadOnlyTask> targets) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Updates the given task */
    void updateTask(ReadOnlyTask target, Task updatedTask)
            throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException;
       
    /** Marks the given task(s) as completed */
    void markTasks(List<ReadOnlyTask> targets)
            throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException;
    
    /** Unmarks the given task(s) */
    void unmarkTasks(List<ReadOnlyTask> targets)
            throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException;

    /** 
     * Restores the previous to-do list saved in the stack of previous lists.
     */
    void restorePreviousToDoList() throws NoPreviousListFoundException;

    /** 
     * Resets the main to-do list data to match the top list in the stack of previous lists
     */
    void resetDataToLastSavedList();
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Change the data storage location */
    void changeSaveLocation(String location);

    /** load the data from a file **/
    void loadFromLocation(String location);

    /** Updates the current todolist to the loaded data**/
    void handleLoadDataCompleteEvent(LoadDataCompleteEvent event);

    /** Turns on model syncing using to a thirds party syncing provider **/
    void activateModelSyncing();

    /** Turns off model syncing **/
    void deactivateModelSyncing();

}
