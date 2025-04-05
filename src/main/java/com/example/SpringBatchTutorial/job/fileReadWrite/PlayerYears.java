package com.example.SpringBatchTutorial.job.fileReadWrite;

import java.time.Year;
import lombok.Data;
import lombok.ToString;
import org.springframework.batch.item.file.transform.FieldSet;

@Data
@ToString
public class PlayerYears {
    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;
    private int yearsExperience;

    public PlayerYears(Player player) {
        this.ID = player.getID();
        this.lastName = player.getLastName();
        this.firstName =  player.getFirstName();
        this.position = player.getPosition();
        this.birthYear = player.getBirthYear();
        this.debutYear = player.getDebutYear();
        this.yearsExperience = Year.now().getValue() - player.getDebutYear();
    }
}
