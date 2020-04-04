package se.ecutb.todoapplication.dto;

import se.ecutb.todoapplication.constants.messages.ValidationMessages;
import se.ecutb.todoapplication.entity.AppUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TodoItemFormDto {

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    @Size(min = 2, max = 255, message = ValidationMessages.TASK_TITLE_MESSAGE)
    private String title;

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    @Size(min = 5, max = 255, message = ValidationMessages.TASK_DESCRIPTION_MESSAGE)
    private String description;

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    private LocalDate deadline;

    @NotBlank(message = ValidationMessages.FIELD_REQUIRED_MESSAGE)
    @Size(min = 5, max = 10000, message = ValidationMessages.TASK_REWARD_MESSAGE)
    private double reward;

    private AppUser assignee;

    boolean isDone;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public AppUser getAssignee() {
        return assignee;
    }

    public void setAssignee(AppUser assignee) {
        this.assignee = assignee;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}
