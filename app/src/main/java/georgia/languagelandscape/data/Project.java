/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The model data class representing a collection of recordings
 * This data class has been made static (i.e. a Project data type can not be passed around between
 * activities) because much of the work is handled by its corresponding
 * {@link georgia.languagelandscape.database.ProjectDataSource} class
 */
public class Project {

    private String projectID = null;
    private String fullName = null;
    private String shortName = null;
    private String description = null;
    private User owner = null;
    private List<User> contributors = null;
    private List<String> languages = null;
    private List<Recording> recordings = null;
    private List<String> recordingIDs = null;
    // TODO: could have image for the marker


    public Project() {
        if (projectID == null) {
            projectID = UUID.randomUUID().toString();
        }
        if (recordingIDs == null) {
            recordingIDs = new ArrayList<>();
        }
    }

    public Project(
            @Nullable String id,
            @NonNull String fullName,
            @Nullable String shortName,
            @Nullable String description,
            @Nullable User owner,
            @Nullable List<User> contributors,
            @Nullable List<String> languages,
            @Nullable List<Recording> recordings,
            List<String> recordingIDs) {

        if (id == null) {
            projectID = UUID.randomUUID().toString();
        }
        this.fullName = fullName;
        this.shortName = shortName;
        this.description = description;
        this.owner = owner;
        this.contributors = contributors;
        this.languages = languages;
        this.recordings = recordings;
        this.recordingIDs = recordingIDs;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
        recordingIDs.clear();
        if (recordings != null) {
            for (Recording recording : recordings) {
                recordingIDs.add(recording.getRecordingID());
            }
        }
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> contributors) {
        this.contributors = contributors;
    }

    public List<String> getRecordingIDs() {
        return recordingIDs;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID='" + projectID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", contributors=" + contributors +
                ", languages=" + languages +
                ", recordings=" + recordings +
                ", recordingIDs=" + recordingIDs +
                '}';
    }

    public void addRecording(Recording recording) {
        if (recordings == null) {
            recordings = new ArrayList<>();
        }
        recordings.add(recording);
        recordingIDs.add(recording.getRecordingID());
    }

    public int getRecordingCount() {
        return recordings.size();
    }
}
